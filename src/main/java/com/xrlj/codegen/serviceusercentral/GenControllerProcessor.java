package com.xrlj.codegen.serviceusercentral;

import com.xrlj.codegen.Constants;
import com.xrlj.framework.core.processor.AbstractGenApiImplProcessor;

public class GenControllerProcessor extends AbstractGenApiImplProcessor {

    @Override
    public String gencodeProjectName() {
        return Constants.ServiceUsercentral.apiModuleName;
    }

    @Override
    public String javaFileOputProjectName() {
        return Constants.ServiceUsercentral.providerModuleName;
    }

    @Override
    public String packagePath() {
        return Constants.ServiceUsercentral.projectPackage.concat(".controller");
    }
}
