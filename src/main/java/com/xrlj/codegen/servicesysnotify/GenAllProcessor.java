package com.xrlj.codegen.servicesysnotify;

import com.xrlj.codegen.Constants;
import com.xrlj.codegen.processor.AbstractGenAllProcessor;

public class GenAllProcessor extends AbstractGenAllProcessor {

    @Override
    public String baseProjectName() {
        return Constants.ServiceSysNotify.projectName;
    }

    @Override
    public String basePackage() {
        return Constants.ServiceSysNotify.projectPackage;
    }
}
