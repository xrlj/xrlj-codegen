package com.xrlj.codegen.serviceauth;

import com.xrlj.codegen.Constants.ServiceAuth;
import com.xrlj.codegen.processor.AbstractGenApiImplProcessor;

public class GenControllerProcessor extends AbstractGenApiImplProcessor {

    @Override
    public String gencodeProjectName() {
        return ServiceAuth.apiModuleName;
    }

    @Override
    public String javaFileOputProjectName() {
        return ServiceAuth.providerModuleName;
    }

    @Override
    public String packagePath() {
        return ServiceAuth.projectPackage.concat(".controller");
    }
}
