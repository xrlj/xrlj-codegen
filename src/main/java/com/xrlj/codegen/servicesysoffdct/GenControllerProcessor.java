package com.xrlj.codegen.servicesysoffdct;

import com.xrlj.codegen.Constants;
import com.xrlj.codegen.processor.AbstractGenApiImplProcessor;

public class GenControllerProcessor extends AbstractGenApiImplProcessor {

    @Override
    public String gencodeProjectName() {
        return Constants.ServiceSysOffDct.apiModuleName;
    }

    @Override
    public String javaFileOputProjectName() {
        return Constants.ServiceSysOffDct.providerModuleName;
    }

    @Override
    public String packagePath() {
        return Constants.ServiceSysOffDct.projectPackage.concat(".controller");
    }
}
