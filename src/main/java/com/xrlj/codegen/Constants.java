package com.xrlj.codegen;

public interface Constants {

    interface Servicesyscommon {

        String projectName = "service-sys-common";
        String projectPackage = "com.xrlj.servicesyscommon";
        String apiModuleName = getModuleName(projectName, "api");
        String entitiesModuleName = getModuleName(projectName, "entities");
        String providerModuleName = getModuleName(projectName, "provider");

        String DRIVER = "com.mysql.cj.jdbc.Driver";
        String URL = "jdbc:mysql://120.79.2.30:3910/service_sys_common?useUnicode=true&characterEncoding=utf8";
        String USERNAME = "root";
        String PASSWORD = "123456";
    }

    interface ServiceAuth {

        String projectName = "service-auth";
        String projectPackage = "com.xrlj.serviceauth";
        String apiModuleName = Constants.getModuleName(projectName, "api");
        String providerModuleName = getModuleName(projectName, "provider");
    }

    interface ServiceSysNotify {

        String projectName = "service-sys-notify";
        String projectPackage = "com.xrlj.servicesysnotify";
        String apiModuleName = Constants.getModuleName(projectName, "api");
        String providerModuleName = getModuleName(projectName, "provider");
    }

    interface ServiceSysFilesystem {

        String projectName = "service-sys-filesystem";
        String projectPackage = "com.xrlj.servicesysfilesystem";
        String apiModuleName = Constants.getModuleName(projectName, "api");
        String entitiesModuleName = Constants.getModuleName(projectName, "entities");
        String providerModuleName = getModuleName(projectName, "provider");

        String DRIVER = "com.mysql.cj.jdbc.Driver";
        String URL = "jdbc:mysql://120.79.2.30:3910/service_filesystem?useUnicode=true&characterEncoding=utf8";
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
        String URL = "jdbc:mysql://120.79.2.30:3910/service_usercentral?useUnicode=true&characterEncoding=utf8";
        String USERNAME = "root";
        String PASSWORD = "123456";
    }

    interface ServiceSysOffDct {

        String projectName = "service-sys-offdct";
        String projectPackage = "com.xrlj.servicesysoffdct";
        String apiModuleName = Constants.getModuleName(projectName, "api");
        String providerModuleName = getModuleName(projectName, "provider");
    }

    static String getModuleName(String p, String m) {
        return p.concat("-").concat(m);
    }
}
