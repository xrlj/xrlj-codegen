package com.xrlj.codegen.processor;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.xrlj.framework.core.annotation.GenDomainField2DBFieldAnnotation;
import com.xrlj.utils.TableEntityMapperUtil;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.persistence.ManyToOne;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 只适合单表，表实体没任何关联设计的模式。
 */
public class GenDomainField2DBFieldProcessor extends AbstractProcessor {

    private Filer filer;
    private Types typeUtils;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        filer = processingEnv.getFiler(); // for creating file
        System.out.println(">>>processor init:" + filer.toString());
        typeUtils = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println(">>>> annotations.size:" + annotations.size());
        for (TypeElement element : annotations) {
            String eQualifeName = element.getQualifiedName().toString();
            System.out.println(">>>> 注解全路径：" + eQualifeName);
            String canonicalName = GenDomainField2DBFieldAnnotation.class.getCanonicalName();
            if (eQualifeName.equals(canonicalName)) {
                try {
                    Set<? extends Element> elements = roundEnv.getRootElements();
                    for (Element e : elements) {
                        if (e.getKind().isClass()) {
                            //类信息
                            String clazzName = e.getSimpleName().toString();
                            TypeMirror typeClazz = e.asType();

                            //类
                            TypeSpec.Builder sqlFieldClazz = TypeSpec.classBuilder(clazzName.concat("SqlField")).addModifiers(Modifier.PUBLIC,Modifier.FINAL).addJavadoc("表实体类映射的数据表字段名");
                            //============域
                            FieldSpec tableNameField = FieldSpec.builder(String.class,"TABLE_NAME", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                                    .initializer("$S", TableEntityMapperUtil.mapperToDB(clazzName)).build();
                            sqlFieldClazz.addField(tableNameField);
                            List<? extends Element> clazzInfo = e.getEnclosedElements();
                            for (Element e1 : clazzInfo) {
                                if (e1.getKind().isField()) {
                                    sqlFieldClazz.addField(addFieldSpec(e1));
                                }
                            }

                            //处理父类
                            List<? extends TypeMirror> supertypes = typeUtils.directSupertypes(typeClazz);
                            for (TypeMirror typeMirror : supertypes) {
                                Element e2 = typeUtils.asElement(typeMirror);
                                List<? extends Element> e3s = e2.getEnclosedElements();
                                for (Element e3 : e3s)
                                if (e3.getKind().isField()) {
                                   sqlFieldClazz.addField(addFieldSpec(e3));
                                }
                            }

                            //上一级包
                            Element e0 = e.getEnclosingElement();
                            String e0Name = e0.asType().toString();
                            //创建java类
                            JavaFile javaFile = JavaFile.builder(e0Name, sqlFieldClazz.build())
                                    .addFileComment("自动生成的代码，不要更改!")
                                    .build();
                            // 输出到.java文件
                            javaFile.writeTo(filer);
                            //输出到控制台
                            javaFile.writeTo(System.out);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(GenDomainField2DBFieldAnnotation.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private FieldSpec addFieldSpec(Element e) {
        String fieldName = e.getSimpleName().toString();
        ManyToOne annotation = e.getAnnotation(ManyToOne.class);
        if (annotation != null) {
            //添加多对一注解后，jpa自定创建表，该字段会自动加上关联表id，所以这里要处理下
            fieldName = fieldName.concat("Id");
        }
        FieldSpec fieldSpec = FieldSpec.builder(String.class,TableEntityMapperUtil.separationDomainField(fieldName),Modifier.PUBLIC,Modifier.STATIC,Modifier.FINAL).initializer("$S", TableEntityMapperUtil.mapperToDB(fieldName)).build();
       return fieldSpec;
    }


}
