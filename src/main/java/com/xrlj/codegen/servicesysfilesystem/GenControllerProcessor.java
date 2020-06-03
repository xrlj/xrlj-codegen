package com.xrlj.codegen.servicesysfilesystem;

import com.xrlj.codegen.Constants;
import com.xrlj.codegen.processor.AbstractGenApiImplProcessor;

public class GenControllerProcessor extends AbstractGenApiImplProcessor {

    @Override
    public String gencodeProjectName() {
        return Constants.ServiceSysFilesystem.apiModuleName;
    }

    @Override
    public String javaFileOputProjectName() {
        return Constants.ServiceSysFilesystem.providerModuleName;
    }

    @Override
    public String packagePath() {
        return Constants.ServiceSysFilesystem.projectPackage.concat(".controller");
    }
}
