package com.xrlj.codegen.servicesysfilesystem;

import com.xrlj.codegen.Constants;
import com.xrlj.framework.core.processor.AbstractGenDaoProcessor;

public class GenDaoProcessor extends AbstractGenDaoProcessor {

    @Override
    public String gencodeProjectName() {
        return Constants.ServiceSysFilesystem.entitiesModuleName;
    }

    @Override
    public String javaFileOputProjectName() {
        return Constants.ServiceSysFilesystem.providerModuleName;
    }

    @Override
    public String daoPackagePath() {
        return Constants.ServiceSysFilesystem.projectPackage.concat(".dao");
    }

    @Override
    public String entitiesPackage() {
        return Constants.ServiceSysFilesystem.projectPackage.concat(".entities");
    }
}
