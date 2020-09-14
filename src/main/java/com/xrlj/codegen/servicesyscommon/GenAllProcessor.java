package com.xrlj.codegen.servicesyscommon;

import com.xrlj.codegen.Constants;
import com.xrlj.codegen.processor.AbstractGenAllProcessor;

public class GenAllProcessor extends AbstractGenAllProcessor {

    @Override
    public String baseProjectName() {
        return Constants.Servicesyscommon.projectName;
    }

    @Override
    public String basePackage() {
        return Constants.Servicesyscommon.projectPackage;
    }
}
