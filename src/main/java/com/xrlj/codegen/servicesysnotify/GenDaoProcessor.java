package com.xrlj.codegen.servicesysnotify;

import com.xrlj.codegen.Constants;
import com.xrlj.framework.core.processor.AbstractGenDaoProcessor;

public class GenDaoProcessor extends AbstractGenDaoProcessor {

    @Override
    public String gencodeProjectName() {
        return Constants.ServiceSysNotify.entitiesModuleName;
    }

    @Override
    public String javaFileOputProjectName() {
        return Constants.ServiceSysNotify.providerModuleName;
    }

    @Override
    public String daoPackagePath() {
        return Constants.ServiceSysNotify.projectPackage.concat(".dao");
    }

    @Override
    public String entitiesPackage() {
        return Constants.ServiceSysNotify.projectPackage.concat(".entities");
    }
}
