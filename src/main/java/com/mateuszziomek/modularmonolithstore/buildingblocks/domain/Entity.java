package com.mateuszziomek.modularmonolithstore.buildingblocks.domain;

import com.google.common.base.Preconditions;
import io.vavr.control.Try;

public abstract class Entity {
    public Try<Void> checkRule(final BusinessRule businessRule) {
        Preconditions.checkNotNull(businessRule, "Business rule can't be null");

        return businessRule.isBroken() ? Try.failure(new BusinessRuleException(businessRule)) : Try.success(null);
    }
}
