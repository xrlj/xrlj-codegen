package com.xrlj.codegen.servicesysnotify;

import com.xrlj.codegen.Constants;
import com.xrlj.codegen.processor.AbstractGenApiImplProcessor;

public class GenControllerProcessor extends AbstractGenApiImplProcessor {

    @Override
    public String gencodeProjectName() {
        return Constants.ServiceSysNotify.apiModuleName;
    }

    @Override
    public String javaFileOputProjectName() {
        return Constants.ServiceSysNotify.providerModuleName;
    }

    @Override
    public String packagePath() {
        return Constants.ServiceSysNotify.projectPackage.concat(".controller");
    }
}
