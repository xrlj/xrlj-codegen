package com.xrlj.codegen.servicesysfilesystem;

import com.xrlj.codegen.Constants.ServiceSysFilesystem;
import com.xrlj.codegen.processor.GenDBField2BeanProcessor;
import com.xrlj.utils.PrintUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GenDBFieldProcessor extends GenDBField2BeanProcessor {

    static {
        try {
            Class.forName(ServiceSysFilesystem.DRIVER);
        } catch (ClassNotFoundException e) {
            PrintUtil.println("加载数据库驱动失败……");
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(ServiceSysFilesystem.URL, ServiceSysFilesystem.USERNAME, ServiceSysFilesystem.PASSWORD);
        } catch (SQLException e) {
            PrintUtil.println("获取数据库连接失败");
            e.printStackTrace();
        }
        return conn;
    }

    @Override
    public String getJavaFileOutPackagePath() {
        return ServiceSysFilesystem.projectPackage.concat(".gen.db.tables");
    }
}
