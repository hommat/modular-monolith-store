package com.mateuszziomek.modularmonolithstore.buildingblocks.domain;

public class BusinessRuleException extends RuntimeException {
    private final transient BusinessRule businessRule;

    public BusinessRuleException(final BusinessRule businessRule) {
        super(businessRule.message());
        this.businessRule = businessRule;
    }

    public BusinessRule businessRule() {
        return businessRule;
    }
}
