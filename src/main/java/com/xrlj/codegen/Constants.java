package com.xrlj.codegen;

public interface Constants {

    String URL_PREFIX = "jdbc:mysql://xrlj-server:3910/";
    String URL_SUFFIX = "?useUnicode=true&characterEncoding=utf8";

    static  String db_url(String tableName) {
        return URL_PREFIX.concat(tableName).concat(URL_SUFFIX);
    }

    interface ServiceEBook {
        String projectName = "service-ebook";
        String projectPackage = "com.xrlj.serviceebook";
        String apiModuleName = getModuleName(projectName, "api");
        String entitiesModuleName = getModuleName(projectName, "entities");
        String providerModuleName = getModuleName(projectName, "provider");

        String DRIVER = "com.mysql.cj.jdbc.Driver";
        String URL = Constants.db_url("service_ebook");
        String USERNAME = "root";
        String PASSWORD = "123456";
    }

    interface ServiceAbsProduct {
        String projectName = "service-abs-product";
        String projectPackage = "com.xrlj.serviceabsproduct";
        String apiModuleName = getModuleName(projectName, "api");
        String entitiesModuleName = getModuleName(projectName, "entities");
        String providerModuleName = getModuleName(projectName, "provider");

        String DRIVER = "com.mysql.cj.jdbc.Driver";
        String URL = Constants.db_url("service_abs_product");
        String USERNAME = "root";
        String PASSWORD = "123456";
    }

    interface ServiceAbsPayment {
        String projectName = "service-abs-payment";
        String projectPackage = "com.xrlj.serviceabspayment";
        String apiModuleName = getModuleName(projectName, "api");
        String entitiesModuleName = getModuleName(projectName, "entities");
        String providerModuleName = getModuleName(projectName, "provider");

        String DRIVER = "com.mysql.cj.jdbc.Driver";
        String URL = Constants.db_url("service_abs_payment");
        String USERNAME = "root";
        String PASSWORD = "123456";
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
