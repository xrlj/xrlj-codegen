package com.xrlj.codegen.serviceabstemplate;

import com.xrlj.codegen.Constants;
import com.xrlj.codegen.processor.AbstractGenAllProcessor;

public class GenAllProcessor extends AbstractGenAllProcessor {

    @Override
    public String baseProjectName() {
        return Constants.ServiceAbsTemplate.projectName;
    }

    @Override
    public String basePackage() {
        return Constants.ServiceAbsTemplate.projectPackage;
    }
}
