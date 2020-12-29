package com.xrlj.codegen.serviceabspayment;

import com.xrlj.codegen.Constants;
import com.xrlj.codegen.processor.AbstractGenAllProcessor;

public class GenAllProcessor extends AbstractGenAllProcessor {

    @Override
    public String baseProjectName() {
        return Constants.ServiceAbsPayment.projectName;
    }

    @Override
    public String basePackage() {
        return Constants.ServiceAbsPayment.projectPackage;
    }
}
