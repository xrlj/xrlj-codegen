package com.xrlj.codegen.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.xrlj.framework.base.BaseController;
import com.xrlj.framework.base.BaseService;
import com.xrlj.framework.base.BaseServiceImpl;
import com.xrlj.framework.core.annotation.GenApiImplAnnotation;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.File;
import java.util.Collections;
import java.util.Set;

public abstract class AbstractGenApiImplProcessor extends AbstractProcessor {

    public abstract String gencodeProjectName();
    public abstract String javaFileOputProjectName();
    public abstract String packagePath();

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
            String canonicalName = GenApiImplAnnotation.class.getCanonicalName();
            if (eQualifeName.equals(canonicalName)) {
                try {
                    String srcMailJava = GenCodeUtils.fileOutputSrcMailJava(gencodeProjectName(),javaFileOputProjectName());
                    //==========生成代码
                    Set<? extends Element> elements = roundEnv.getRootElements();
                    for (Element e : elements) {
                        String clazzName = e.getSimpleName().toString();//类名称
                        if (e.getKind().isInterface()  && (clazzName != null && clazzName.endsWith("Api"))) {
                            Element e0 = e.getEnclosingElement();
                            String e0Name = e0.asType().toString(); //类所在包名称
                            System.out.println(">>>>>>:" + e0Name.concat(".").concat(clazzName));

                            //===========生成Controller
                            String controllerName = clazzName.substring(0,clazzName.lastIndexOf("Api")).concat("Controller");
                            boolean hasController = GenCodeUtils.containsJavaFile(srcMailJava, packagePath(),controllerName);
                            if (!hasController) { //api对应controller不存在，生成
                                ClassName baseController = ClassName.get(BaseController.class); //集成父类
                                ClassName implApi = ClassName.get(e0Name,clazzName); //实现接口
                                //添加注解
                                ClassName slf4jClazzName = ClassName.get("lombok.extern.slf4j","Slf4j");
                                ClassName restController = ClassName.get("org.springframework.web.bind.annotation", "RestController");
                                ClassName refreshScope = ClassName.get("org.springframework.cloud.context.config.annotation", "RefreshScope");

                                TypeSpec controller = TypeSpec.classBuilder(controllerName)
                                        .superclass(baseController)
                                        .addSuperinterface(implApi)
                                        .addAnnotation(slf4jClazzName)
                                        .addAnnotation(restController)
                                        .addAnnotation(refreshScope)
                                        .addModifiers(Modifier.PUBLIC)
                                        .build();

                                JavaFile controllerJavaFile = JavaFile.builder(packagePath(), controller)
                                        .build();
                                controllerJavaFile.writeTo(System.out); //输出到控制台
                                File file = new File(srcMailJava); //输出到文件
                                controllerJavaFile.writeTo(file);
                            }

                            //===========生成Service接口
                            String servicePackage = packagePath().substring(0, packagePath().indexOf(".controller")).concat(".service");
                            String serviceName = clazzName.substring(0,clazzName.lastIndexOf("Api")).concat("Service");
                            boolean hasService = GenCodeUtils.containsJavaFile(srcMailJava, servicePackage,serviceName);
                            if (!hasService) {
                                ClassName baseService = ClassName.get(BaseService.class);
                                TypeSpec service = TypeSpec.interfaceBuilder(serviceName)
                                        .addSuperinterface(baseService)
                                        .addModifiers(Modifier.PUBLIC)
                                        .addJavadoc("接口服务")
                                        .build();

                                JavaFile serviceJavaFile = JavaFile.builder(servicePackage, service)
                                        .addFileComment("自动生成。手动添加泛型实体。")
                                        .build();
                                serviceJavaFile.writeTo(System.out); //输出到控制台
                                File file1 = new File(srcMailJava); //输出到文件
                                serviceJavaFile.writeTo(file1);
                            }

                            //===========生成Service实现
                            String serviceImplName = serviceName.concat("Impl");
                            String serviceImplPackage = servicePackage.concat(".impl");
                            boolean hasServiceImpl = GenCodeUtils.containsJavaFile(srcMailJava, serviceImplPackage,serviceImplName);
                            if (!hasServiceImpl) {
                                //基类
                                ClassName baseServiceImpl = ClassName.get(BaseServiceImpl.class);
                                //接口
                                ClassName serviceInterface = ClassName.get(servicePackage, serviceName);
                                //注解
                                ClassName slf4jClazzName = ClassName.get("lombok.extern.slf4j", "Slf4j");
                                //注解
                                ClassName serviceClazzName = ClassName.get("org.springframework.stereotype", "Service");
                                //注解
                                ClassName refreshScope = ClassName.get("org.springframework.cloud.context.config.annotation", "RefreshScope");
                                TypeSpec serviceImpl = TypeSpec.classBuilder(serviceImplName)
                                        .superclass(baseServiceImpl)
                                        .addSuperinterface(serviceInterface)
                                        .addAnnotation(slf4jClazzName)
                                        .addAnnotation(serviceClazzName)
                                        .addAnnotation(refreshScope)
                                        .addModifiers(Modifier.PUBLIC)
                                        .addJavadoc("接口实现")
                                        .build();

                                JavaFile serviceImplJavaFile = JavaFile.builder(serviceImplPackage, serviceImpl)
                                        .addFileComment("自动生成。手动添加泛型实体。")
                                        .build();
                                serviceImplJavaFile.writeTo(System.out); //输出到控制台
                                File file = new File(srcMailJava);  //输出到文件
                                serviceImplJavaFile.writeTo(file);
                            }

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
        return Collections.singleton(GenApiImplAnnotation.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
