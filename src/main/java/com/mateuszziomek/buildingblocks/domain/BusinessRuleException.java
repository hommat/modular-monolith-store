package com.mateuszziomek.buildingblocks.domain;

public class BusinessRuleException extends RuntimeException {
    private final BusinessRule businessRule;

    public BusinessRuleException(final BusinessRule businessRule) {
        super(businessRule.message());
        this.businessRule = businessRule;
    }

    public BusinessRule businessRule() {
        return businessRule;
    }
}
