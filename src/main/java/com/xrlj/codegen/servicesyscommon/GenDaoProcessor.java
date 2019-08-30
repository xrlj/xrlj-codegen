package com.xrlj.codegen.servicesyscommon;

import com.xrlj.codegen.Constants;
import com.xrlj.framework.core.processor.AbstractGenDaoProcessor;

public class GenDaoProcessor extends AbstractGenDaoProcessor {

    @Override
    public String gencodeProjectName() {
        return Constants.Servicesyscommon.entitiesModuleName;
    }

    @Override
    public String javaFileOputProjectName() {
        return Constants.Servicesyscommon.providerModuleName;
    }

    @Override
    public String daoPackagePath() {
        return Constants.Servicesyscommon.projectPackage.concat(".dao");
    }

    @Override
    public String entitiesPackage() {
        return Constants.Servicesyscommon.projectPackage.concat(".entities");
    }
}
