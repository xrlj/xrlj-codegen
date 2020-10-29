package com.xrlj.codegen.serviceebook;

import com.xrlj.codegen.Constants;
import com.xrlj.codegen.processor.AbstractGenAllProcessor;

public class GenAllProcessor extends AbstractGenAllProcessor {

    @Override
    public String baseProjectName() {
        return Constants.ServiceEBook.projectName;
    }

    @Override
    public String basePackage() {
        return Constants.ServiceEBook.projectPackage;
    }
}
