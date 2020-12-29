package com.xrlj.codegen.serviceabsproduct;

import com.xrlj.codegen.Constants;
import com.xrlj.codegen.processor.AbstractGenAllProcessor;

public class GenAllProcessor extends AbstractGenAllProcessor {

    @Override
    public String baseProjectName() {
        return Constants.ServiceAbsProduct.projectName;
    }

    @Override
    public String basePackage() {
        return Constants.ServiceAbsProduct.projectPackage;
    }
}
