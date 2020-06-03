package com.xrlj.codegen.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.xrlj.framework.core.annotation.GenDaoAnnotation;
import com.xrlj.framework.dao.base.BaseDao;
import com.xrlj.framework.dao.base.BaseDaoImpl;
import com.xrlj.framework.dao.base.BaseMapper;
import com.xrlj.framework.dao.base.BaseRepository;
import org.apache.commons.io.FileUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class AbstractGenDaoProcessor extends AbstractProcessor {

    public abstract String gencodeProjectName();

    public abstract String javaFileOputProjectName();

    public abstract String daoPackagePath();

    public abstract String entitiesPackage();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println(">>>> annotations.size:" + annotations.size());
        for (TypeElement element : annotations) {
            String eQualifeName = element.getQualifiedName().toString();
            System.out.println(">>>> 注解全路径：" + eQualifeName);
            String canonicalName = GenDaoAnnotation.class.getCanonicalName();
            if (eQualifeName.equals(canonicalName)) {
                try {
                    String javaOutDirLaster = GenCodeUtils.fileOutputSrcMailJava(gencodeProjectName(), javaFileOputProjectName());

                    //==========生成代码
                    Set<? extends Element> elements = roundEnv.getRootElements();
                    for (Element e : elements) {
                        if (e.getKind().isClass()) {
                            String clazzName = e.getSimpleName().toString();//类名称
                            Element e0 = e.getEnclosingElement();
                            String e0Name = e0.asType().toString(); //类所在包名称

                            //不是实体包下的类跳过
                            if (!e0Name.equalsIgnoreCase(entitiesPackage())) {
                                continue;
                            }

                            //==============生成 jpa Repository start
                            ClassName superClassBR = ClassName.get(BaseRepository.class);
                            ClassName superJpa = ClassName.get("org.springframework.data.jpa.repository", "JpaSpecificationExecutor");
                            //处理继承以及泛型
                            ClassName entityClazzName = ClassName.get(e0Name, clazzName); //泛型
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

                            String repositoryPackagePath = daoPackagePath().concat(".").concat("repository");//jpa repository所在包
                            JavaFile javaFile = JavaFile.builder(repositoryPackagePath, jpayRepository)
                                    .addFileComment("JPA操作表")
                                    .build();

                            boolean b = GenCodeUtils.containsJavaFile(javaOutDirLaster, repositoryPackagePath, repositoryName);
                            if (!b) { //不存在，生成代码
                                javaFile.writeTo(System.out); //输出到控制台
                                File file = new File(javaOutDirLaster); //输出到文件
                                javaFile.writeTo(file);
                            }
                            //==============生成 jpa Repository end

                            //===========生成 MyBatis mapper start
                            ClassName superMapperClass = ClassName.get(BaseMapper.class);

                            String mapperName = clazzName.concat("Mapper"); //MyBatis mapper类名称
                            TypeSpec mapperTypeSpec = TypeSpec.interfaceBuilder(mapperName)
                                    .addSuperinterface(superMapperClass) //父类
                                    .addModifiers(Modifier.PUBLIC)
                                    .addJavadoc("简单、复杂查询皆可。复杂查询在xml文件中配置。")
                                    .build();

                            String mapperPackagePath = daoPackagePath().concat(".").concat("mapper");//MyBatis mapper所在包
                            JavaFile mapperJavaFile = JavaFile.builder(mapperPackagePath, mapperTypeSpec)
                                    .addFileComment("MyBatis操作表")
                                    .build();

                            boolean mb = GenCodeUtils.containsJavaFile(javaOutDirLaster, mapperPackagePath, mapperName);
                            if (!mb) { //不存在，生成代码
                                mapperJavaFile.writeTo(System.out); //输出到控制台
                                File file = new File(javaOutDirLaster); //输出到文件
                                mapperJavaFile.writeTo(file);
                            }
                            //生成*Mapper.xml文件
                            String mapperXmlPath = FileUtils.getFile(javaOutDirLaster).getParent().concat(File.separator).concat("resources").concat(File.separator).concat("mapper");
                            String mapperXmlNamePath = mapperXmlPath.concat(File.separator).concat(mapperName).concat(".xml");
                            File mapperXmlNameFile = new File(mapperXmlNamePath);
                            if (!mapperXmlNameFile.exists()) { //不存在
                                System.out.println(">>>文件不存在");
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

                            JavaFile daoJavaFile = JavaFile.builder(daoPackagePath(), dao)
                                    .addFileComment("数据库操作层，统一的出口。只在service业务层引入该接口，即可调用各种dao框架的操作接口。")
                                    .build();

                            boolean b1 = GenCodeUtils.containsJavaFile(javaOutDirLaster, daoPackagePath(), daoName);
                            if (!b1) { //不存在，生成代码
                                daoJavaFile.writeTo(System.out); //输出到控制台
                                File file1 = new File(javaOutDirLaster); //输出到文件
                                daoJavaFile.writeTo(file1);
                            }
                            //============生成Dao接口 end

                            //============生成Dao接口的实现类 start
                            ClassName baseDaoImpl = ClassName.get(BaseDaoImpl.class);
                            ParameterizedTypeName ptn4 = ParameterizedTypeName.get(baseDaoImpl, clazzRepository, clazzMapper, entityClazzName);

                            ClassName daoClazzName = ClassName.get(daoPackagePath(), daoName);
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

                            String daoImplPackagePath = daoPackagePath().concat(".").concat("impl");
                            JavaFile daoImplJavaFile = JavaFile.builder(daoImplPackagePath, daoImpl)
                                    .build();
                            boolean b2 = GenCodeUtils.containsJavaFile(javaOutDirLaster, daoImplPackagePath, daoImplName);
                            if (!b2) { //不存在，生成代码
                                daoImplJavaFile.writeTo(System.out); //输出到控制台
                                File file2 = new File(javaOutDirLaster); //输出到文件
                                daoImplJavaFile.writeTo(file2);
                            }
                            //============生成Dao接口的实现类 end
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(GenDaoAnnotation.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
