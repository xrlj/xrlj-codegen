package com.xrlj.codegen.processor;

import com.squareup.javapoet.*;
import com.xrlj.framework.base.BaseController;
import com.xrlj.framework.base.BaseService;
import com.xrlj.framework.base.BaseServiceImpl;
import com.xrlj.framework.core.annotation.GenAllAnnotation;
import com.xrlj.framework.core.annotation.GenDaoAnnotation;
import com.xrlj.framework.dao.base.BaseDao;
import com.xrlj.framework.dao.base.BaseDaoImpl;
import com.xrlj.framework.dao.base.BaseMapper;
import com.xrlj.framework.dao.base.BaseRepository;
import com.xrlj.utils.StringUtil;
import com.xrlj.utils.TableEntityMapperUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 以实体为基础，生成api，controller、service等等类。
 */
public abstract class AbstractGenAllProcessor extends AbstractProcessor {

    private String entitiesProjectName;
    private String apiProjectName;
    private String providerProjectName;
    private String apiOutputDir;
    private String providerOutputDir;
    private String entitiesPackage;
    private String apiPackage;
    private String controllerPackage;
    private String servicePackage;
    private String daoPackage;

    public abstract String baseProjectName();

    public abstract String basePackage();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        entitiesProjectName = baseProjectName().concat("-").concat("entities");
        apiProjectName = baseProjectName().concat("-").concat("api");
        providerProjectName = baseProjectName().concat("-").concat("provider");

        apiOutputDir = GenCodeUtils.fileOutputSrcMailJava(entitiesProjectName, apiProjectName);
        providerOutputDir = GenCodeUtils.fileOutputSrcMailJava(entitiesProjectName, providerProjectName);

        //package
        apiPackage = basePackage().concat(".api");
        entitiesPackage = basePackage().concat(".entities");
        controllerPackage = basePackage().concat(".controller");
        servicePackage = basePackage().concat(".service");
        daoPackage = basePackage().concat(".dao");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GEN CODE START");
        for (TypeElement element : annotations) {
            String eQualifeName = element.getQualifiedName().toString();
            System.out.println(">>>> 注解全路径：" + eQualifeName);
            String canonicalName = GenAllAnnotation.class.getCanonicalName();
            if (eQualifeName.equals(canonicalName)) {
                try {
                    //==========生成代码
                    Set<? extends Element> elements = roundEnv.getRootElements();
                    for (Element e : elements) {
                        if (e.getKind().isClass()) {
                            String clazzName = e.getSimpleName().toString();//类名称
                            Element e0 = e.getEnclosingElement();
                            String e0Name = e0.asType().toString(); //类所在包名称
                            //不是实体包下的类跳过
                            if (!e0Name.equalsIgnoreCase(entitiesPackage)) {
                                continue;
                            }
                            //生成dao包下面内容
                            genDao(clazzName);
                            //生成service内容
                            genService(clazzName);
                            //生成api
                            genApi(clazzName);
                            //生成controller
                            genController(clazzName);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GEN CODE END");
        return false;
    }

    /**
     * 生成api代码类。
     * @param clazzName
     * @throws IOException
     */
    private void genApi(String clazzName) throws IOException {
        String apiPackage = basePackage().concat(".api");
        String apiClazzName = clazzName.concat("Api"); //api 类名

        boolean exist = GenCodeUtils.containsJavaFile(apiOutputDir, apiPackage, apiClazzName);
        if (exist) { //已经存在java文件。
            return;
        }

        TypeSpec apiTypeSpec = TypeSpec.interfaceBuilder(apiClazzName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(RequestMapping.class)
                        .addMember("value", "$L", "\"".concat("/").concat(StringUtil.firstCharToLower(clazzName).concat("\"")))
                        .build())
                .addJavadoc("Controller 接口")
                .build();

        JavaFile apiJavaFile = JavaFile.builder(apiPackage, apiTypeSpec)
                .addFileComment("http请求接口")
                .build();

        apiJavaFile.writeTo(System.out); //输出到控制台
        File file = new File(apiOutputDir); //输出到文件
        apiJavaFile.writeTo(file);
    }

    /**
     * 生成api实现，即生成Controller。
     * @param clazzName
     */
    private void genController(String clazzName) throws IOException {
        String controllerName = clazzName.concat("Controller");
        boolean exist = GenCodeUtils.containsJavaFile(providerOutputDir, controllerPackage, controllerName);
        if (exist) {
            return;
        }

        //===开始生成controller类
        ClassName baseController = ClassName.get(BaseController.class); //集成父类
        ClassName implApi = ClassName.get(apiPackage, clazzName.concat("Api")); //实现接口
        //注解
        ClassName slf4jClazzName = ClassName.get("lombok.extern.slf4j","Slf4j");
        ClassName refreshScope = ClassName.get("org.springframework.cloud.context.config.annotation", "RefreshScope");

        TypeSpec controller = TypeSpec.classBuilder(controllerName)
                .superclass(baseController)
                .addSuperinterface(implApi)
                .addAnnotation(slf4jClazzName)
                .addAnnotation(RestController.class)
                .addAnnotation(refreshScope)
                .addModifiers(Modifier.PUBLIC)
                .build();

        JavaFile controllerJavaFile = JavaFile.builder(controllerPackage, controller)
                .build();
        controllerJavaFile.writeTo(System.out); //输出到控制台
        File file = new File(providerOutputDir); //输出到文件
        controllerJavaFile.writeTo(file);
    }

    /**
     * 生成service包下面内容
     * @param clazzName
     */
    private void genService(String clazzName) throws IOException {
        //========= 生成service接口
        String serviceName = clazzName.concat("Service");
        boolean exist = GenCodeUtils.containsJavaFile(providerOutputDir, servicePackage, serviceName);
        if (!exist) {
            ClassName baseService = ClassName.get(BaseService.class);
            ClassName entityClazzName = ClassName.get(entitiesPackage, clazzName);
            ParameterizedTypeName ptn = ParameterizedTypeName.get(baseService, entityClazzName);

            TypeSpec service = TypeSpec.interfaceBuilder(serviceName)
                    .addSuperinterface(ptn)
                    .addModifiers(Modifier.PUBLIC)
                    .addJavadoc("服务接口")
                    .build();

            JavaFile serviceJavaFile = JavaFile.builder(servicePackage, service)
                    .addFileComment("自动生成。")
                    .build();
            serviceJavaFile.writeTo(System.out); //输出到控制台
            File file1 = new File(providerOutputDir); //输出到文件
            serviceJavaFile.writeTo(file1);
        }

        //===========生成Service实现
        String serviceImplName = serviceName.concat("Impl");
        String serviceImplPackage = servicePackage.concat(".impl");
        boolean exist1 = GenCodeUtils.containsJavaFile(providerOutputDir, serviceImplPackage, serviceImplName);
        if (!exist1) {
            //基类
            ClassName baseServiceImpl = ClassName.get(BaseServiceImpl.class);
            ClassName daoClazzName = ClassName.get(daoPackage, clazzName.concat("Dao"));
            ClassName entityClazzName = ClassName.get(entitiesPackage, clazzName);
            ParameterizedTypeName ptn = ParameterizedTypeName.get(baseServiceImpl, daoClazzName, entityClazzName);
            //接口
            ClassName serviceInterface = ClassName.get(servicePackage, serviceName);
            //注解
            ClassName slf4jClazzName = ClassName.get("lombok.extern.slf4j", "Slf4j");

            TypeSpec serviceImpl = TypeSpec.classBuilder(serviceImplName)
                    .superclass(ptn)
                    .addSuperinterface(serviceInterface)
                    .addAnnotation(slf4jClazzName)
                    .addAnnotation(Service.class)
                    .addAnnotation(RefreshScope.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addJavadoc("服务接口实现")
                    .build();

            JavaFile serviceImplJavaFile = JavaFile.builder(serviceImplPackage, serviceImpl)
                    .addFileComment("自动生成。")
                    .build();
            serviceImplJavaFile.writeTo(System.out); //输出到控制台
            File file = new File(providerOutputDir);  //输出到文件
            serviceImplJavaFile.writeTo(file);
        }
    }

    /**
     * 生成dao包下面内容。
     * @param clazzName
     */
    private void genDao(String clazzName) throws IOException {
        //==============生成 jpa Repository start
        ClassName superClassBR = ClassName.get(BaseRepository.class);
        ClassName superJpa = ClassName.get("org.springframework.data.jpa.repository", "JpaSpecificationExecutor");
        //处理继承以及泛型
        ClassName entityClazzName = ClassName.get(entitiesPackage, clazzName); //泛型
        ClassName idClazzName = ClassName.get("java.lang", "Long"); //泛型
        ParameterizedTypeName ptn1 = ParameterizedTypeName.get(superClassBR, entityClazzName, idClazzName);
        ParameterizedTypeName ptn2 = ParameterizedTypeName.get(superJpa, entityClazzName);

        String repositoryName = clazzName.concat("Repository"); //jpa repository类名称
        TypeSpec jpayRepository = TypeSpec.interfaceBuilder(repositoryName)
                .addSuperinterface(ptn1)
                .addSuperinterface(ptn2)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("增、删、改、简单单表查询")
                .build();

        String repositoryPackagePath = daoPackage.concat(".repository");//jpa repository所在包
        JavaFile javaFile = JavaFile.builder(repositoryPackagePath, jpayRepository)
                .addFileComment("JPA操作表")
                .build();

        boolean b = GenCodeUtils.containsJavaFile(providerOutputDir, repositoryPackagePath, repositoryName);
        if (!b) { //不存在，生成代码
            javaFile.writeTo(System.out); //输出到控制台
            File file = new File(providerOutputDir); //输出到文件
            javaFile.writeTo(file);
        }
        //==============生成 jpa Repository end

        //===========生成 MyBatis mapper start
        ClassName superMapperClass = ClassName.get(BaseMapper.class);
        ParameterizedTypeName ptn = ParameterizedTypeName.get(superMapperClass, entityClazzName, idClazzName);

        String mapperName = clazzName.concat("Mapper"); //MyBatis mapper类名称
        TypeSpec mapperTypeSpec = TypeSpec.interfaceBuilder(mapperName)
                .addSuperinterface(ptn) //父类
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("简单、复杂查询皆可。复杂查询在xml文件中配置。")
                .build();

        String mapperPackagePath = daoPackage.concat(".mapper");//MyBatis mapper所在包
        JavaFile mapperJavaFile = JavaFile.builder(mapperPackagePath, mapperTypeSpec)
                .addFileComment("MyBatis操作表")
                .build();

        boolean mb = GenCodeUtils.containsJavaFile(providerOutputDir, mapperPackagePath, mapperName);
        if (!mb) { //不存在，生成代码
            mapperJavaFile.writeTo(System.out); //输出到控制台
            File file = new File(providerOutputDir); //输出到文件
            mapperJavaFile.writeTo(file);
        }
        //生成*Mapper.xml文件
        String mapperXmlPath = FileUtils.getFile(providerOutputDir).getParent().concat(File.separator).concat("resources").concat(File.separator).concat("mapper");
        String mapperXmlNamePath = mapperXmlPath.concat(File.separator).concat(mapperName).concat(".xml");
        File mapperXmlNameFile = new File(mapperXmlNamePath);
        if (!mapperXmlNameFile.exists()) { //不存在
            System.out.println(">>>文件不存在,则创建文件");
            List<String> list = new ArrayList<>();
            list.add("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            list.add("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >");
            list.add("<mapper namespace=\""+mapperPackagePath.concat(".").concat(mapperName)+"\" >");
            list.add("\r");
            list.add("</mapper>");
            FileUtils.writeLines(mapperXmlNameFile,list);
        }
        //===========生成 MyBatis mapper end

        //============生成Dao接口 start
        ClassName baseDao = ClassName.get(BaseDao.class);
        ClassName clazzRepository = ClassName.get(repositoryPackagePath, repositoryName);
        ClassName clazzMapper = ClassName.get(mapperPackagePath, mapperName);
        ParameterizedTypeName ptn3 = ParameterizedTypeName.get(baseDao, clazzRepository, clazzMapper, entityClazzName);
        String daoName = clazzName.concat("Dao");
        TypeSpec dao = TypeSpec.interfaceBuilder(daoName)
                .addSuperinterface(ptn3)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("JDBC,JOOQ操作表")
                .build();

        JavaFile daoJavaFile = JavaFile.builder(daoPackage, dao)
                .addFileComment("数据库操作层，统一的出口。只在service业务层引入该接口，即可调用各种dao框架的操作接口。")
                .build();

        boolean b1 = GenCodeUtils.containsJavaFile(providerOutputDir, basePackage().concat(".dao"), daoName);
        if (!b1) { //不存在，生成代码
            daoJavaFile.writeTo(System.out); //输出到控制台
            File file1 = new File(providerOutputDir); //输出到文件
            daoJavaFile.writeTo(file1);
        }
        //============生成Dao接口 end

        //============生成Dao接口的实现类 start
        ClassName baseDaoImpl = ClassName.get(BaseDaoImpl.class);
        ParameterizedTypeName ptn4 = ParameterizedTypeName.get(baseDaoImpl, clazzRepository, clazzMapper, entityClazzName);

        ClassName daoClazzName = ClassName.get(daoPackage, daoName);
        ClassName slf4jClazzName = ClassName.get("lombok.extern.slf4j", "Slf4j");
        ClassName repositoryClazzName = ClassName.get("org.springframework.stereotype", "Repository");

        String daoImplName = clazzName.concat("DaoImpl");
        TypeSpec daoImpl = TypeSpec.classBuilder(daoImplName)
                .superclass(ptn4)
                .addSuperinterface(daoClazzName)
                .addAnnotation(slf4jClazzName)
                .addAnnotation(repositoryClazzName)
                .addModifiers(Modifier.PUBLIC)
                .build();

        String daoImplPackagePath = daoPackage.concat(".").concat("impl");
        JavaFile daoImplJavaFile = JavaFile.builder(daoImplPackagePath, daoImpl)
                .build();
        boolean b2 = GenCodeUtils.containsJavaFile(providerOutputDir, daoImplPackagePath, daoImplName);
        if (!b2) { //不存在，生成代码
            daoImplJavaFile.writeTo(System.out); //输出到控制台
            File file2 = new File(providerOutputDir); //输出到文件
            daoImplJavaFile.writeTo(file2);
        }
        //============生成Dao接口的实现类 end
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(GenAllAnnotation.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
