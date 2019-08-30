package com.xrlj.codegen.serviceusercentral;

import com.xrlj.codegen.Constants;
import com.xrlj.framework.core.processor.AbstractGenDaoProcessor;

public class GenDaoProcessor extends AbstractGenDaoProcessor {

    @Override
    public String gencodeProjectName() {
        return Constants.ServiceUsercentral.entitiesModuleName;
    }

    @Override
    public String javaFileOputProjectName() {
        return Constants.ServiceUsercentral.providerModuleName;
    }

    @Override
    public String daoPackagePath() {
        return Constants.ServiceUsercentral.projectPackage.concat(".dao");
    }

    @Override
    public String entitiesPackage() {
        return Constants.ServiceUsercentral.projectPackage.concat(".entities");
    }
}
