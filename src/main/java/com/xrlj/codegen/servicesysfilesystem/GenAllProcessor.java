package com.xrlj.codegen.servicesysfilesystem;

import com.xrlj.codegen.Constants;
import com.xrlj.codegen.processor.AbstractGenAllProcessor;

public class GenAllProcessor extends AbstractGenAllProcessor {

    @Override
    public String baseProjectName() {
        return Constants.ServiceSysFilesystem.projectName;
    }

    @Override
    public String basePackage() {
        return Constants.ServiceSysFilesystem.projectPackage;
    }
}
