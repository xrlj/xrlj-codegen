package com.xrlj.codegen.servicesyscommon;

import com.xrlj.codegen.Constants;
import com.xrlj.framework.core.processor.AbstractGenApiImplProcessor;

public class GenControllerProcessor extends AbstractGenApiImplProcessor {

    @Override
    public String gencodeProjectName() {
        return Constants.Servicesyscommon.apiModuleName;
    }

    @Override
    public String javaFileOputProjectName() {
        return Constants.Servicesyscommon.providerModuleName;
    }

    @Override
    public String packagePath() {
        return Constants.Servicesyscommon.projectPackage.concat(".controller");
    }
}
