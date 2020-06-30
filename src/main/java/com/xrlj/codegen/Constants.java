package com.xrlj.codegen;

public interface Constants {

    String URL_PREFIX = "jdbc:mysql://192.168.0.3:3910/";
    String URL_SUFFIX = "?useUnicode=true&characterEncoding=utf8";

    static  String db_url(String tableName) {
        return URL_PREFIX.concat(tableName).concat(URL_SUFFIX);
    }

    interface Servicesyscommon {

        String projectName = "service-sys-common";
        String projectPackage = "com.xrlj.servicesyscommon";
        String apiModuleName = getModuleName(projectName, "api");
        String entitiesModuleName = getModuleName(projectName, "entities");
        String providerModuleName = getModuleName(projectName, "provider");

        String DRIVER = "com.mysql.cj.jdbc.Driver";
        String URL = Constants.db_url("service_common");
        String USERNAME = "root";
        String PASSWORD = "123456";
    }

    interface ServiceAuth {

        String projectName = "service-auth";
        String projectPackage = "com.xrlj.serviceauth";
        String apiModuleName = getModuleName(projectName, "api");
        String providerModuleName = getModuleName(projectName, "provider");
    }

    interface ServiceSysNotify {

        String projectName = "service-sys-notify";
        String projectPackage = "com.xrlj.servicesysnotify";
        String apiModuleName = getModuleName(projectName, "api");
        String entitiesModuleName = getModuleName(projectName, "entities");
        String providerModuleName = getModuleName(projectName, "provider");
        String DRIVER = "com.mysql.cj.jdbc.Driver";
        String URL = db_url("service_notify");
        String USERNAME = "root";
        String PASSWORD = "123456";
    }

    interface ServiceSysFilesystem {

        String projectName = "service-sys-filesystem";
        String projectPackage = "com.xrlj.servicesysfilesystem";
        String apiModuleName = getModuleName(projectName, "api");
        String entitiesModuleName = getModuleName(projectName, "entities");
        String providerModuleName = getModuleName(projectName, "provider");

        String DRIVER = "com.mysql.cj.jdbc.Driver";
        String URL = db_url("service_filesystem");
        String USERNAME = "root";
        String PASSWORD = "123456";
    }

    interface ServiceUsercentral {

        String projectName = "service-usercentral";
        String projectPackage = "com.xrlj.serviceusercentral";
        String apiModuleName = getModuleName(projectName, "api");
        String entitiesModuleName = getModuleName(projectName, "entities");
        String providerModuleName = getModuleName(projectName, "provider");

        String DRIVER = "com.mysql.cj.jdbc.Driver";
        String URL = db_url("service_usercentral");
        String USERNAME = "root";
        String PASSWORD = "123456";
    }

    interface ServiceSysOffDct {

        String projectName = "service-sys-offdct";
        String projectPackage = "com.xrlj.servicesysoffdct";
        String apiModuleName = getModuleName(projectName, "api");
        String providerModuleName = getModuleName(projectName, "provider");
    }

    static String getModuleName(String p, String m) {
        return p.concat("-").concat(m);
    }
}
