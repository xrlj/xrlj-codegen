package com.xrlj.codegen.processor;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.xrlj.framework.core.annotation.GenDBField2BeanAnnotation;
import com.xrlj.utils.PrintUtil;
import com.xrlj.utils.TableEntityMapperUtil;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 只适合单表，表实体没任何关联设计的模式。
 */
public abstract class GenDBField2BeanProcessor extends AbstractProcessor {

    private Filer filer;

    private Connection conn ;

    public abstract Connection getConnection();
    public abstract String getJavaFileOutPackagePath();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler(); // for creating file
        System.out.println(">>>>>processor init:" + filer.toString());
        conn  = getConnection();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println(">>>> annotations.size:" + annotations.size());
        for (TypeElement element : annotations) {
            String eQualifeName = element.getQualifiedName().toString();
            System.out.println(">>>> 注解全路径：" + eQualifeName);
            String canonicalName = GenDBField2BeanAnnotation.class.getCanonicalName();
            if (eQualifeName.equals(canonicalName)) { //保证只执行一次
                if (conn  == null) {
                    return false;
                }
                ResultSet rs = null;
                //获取所有表名称
                List<String> tableNames = new ArrayList<>();
                PreparedStatement pStemt = null;
                try {
                    //获取数据库的元数据
                    DatabaseMetaData db = conn.getMetaData();
                    //从元数据中获取到所有的表名
                    rs = db.getTables(conn.getCatalog(), conn.getCatalog(), null, new String[] { "TABLE" });
                    while(rs.next()) {
                        String tbName = rs.getString(3);
                        tableNames.add(tbName);
                    }
                    if (tableNames == null || tableNames.isEmpty()) {
                        return false;
                    }
                    for (String tableName: tableNames) {
                        PrintUtil.println(">>>>表名称：" + tableName);
                        String tableName1 = TableEntityMapperUtil.mapperToProperty(tableName);
                        //============类
                        TypeSpec.Builder sqlFieldClazz = TypeSpec.classBuilder(tableName1.concat("SqlField")).addModifiers(Modifier.PUBLIC,Modifier.FINAL).addJavadoc("表实体类映射的数据表字段名");

                        //============域
                        //表名称
                        FieldSpec tableNameField = FieldSpec.builder(String.class,"TABLE_NAME", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                                .initializer("$S", tableName).build();
                        sqlFieldClazz.addField(tableNameField);
                        //字段名
                        String tableSql = "SELECT * FROM " + tableName;
                        pStemt = conn.prepareStatement(tableSql);
                        //结果集元数据
                        ResultSetMetaData rsmd = pStemt.getMetaData();
                        //表列数
                        int size = rsmd.getColumnCount();
                        for (int i = 0; i < size; i++) {
                            String columnName = rsmd.getColumnName(i + 1);
                            FieldSpec columnNameField = FieldSpec.builder(String.class,columnName.toUpperCase(), Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                                    .initializer("$S", columnName).build();
                            sqlFieldClazz.addField(columnNameField);
                        }

                        //============创建java类文件到指定包下
                        if (getJavaFileOutPackagePath() == null || getJavaFileOutPackagePath().isEmpty()) {
                            PrintUtil.println(">>>>请先设置java文件输出包名！");
                        }
                        JavaFile javaFile = JavaFile.builder(getJavaFileOutPackagePath(), sqlFieldClazz.build())
                                .addFileComment("自动生成的代码，不要更改!")
                                .build();
                        // 输出到.java文件
                        javaFile.writeTo(filer);
                        //输出到控制台
                        javaFile.writeTo(System.out);
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (conn  != null) {
                        try {
                            rs.close();
                            if (pStemt != null) {
                                pStemt.close();
                            }
                            conn .close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(GenDBField2BeanAnnotation.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
