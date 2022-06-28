package com.mateuszziomek.modularmonolithstore.buildingblocks.domain;

import static org.assertj.core.api.Assertions.*;

import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

class EntityTest {
    @Test
    void ruleCheckCanBeSuccess() {
        // Arrange
        var sut = new TestEntity(new BooleanBusinessRule(false));

        // Act
        var result = sut.businessOperation();

        // Assert
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void ruleCheckCanBeFailure() {
        // Arrange
        var sut = new TestEntity(new BooleanBusinessRule(true));

        // Act
        var result = sut.businessOperation();

        // Assert
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).isInstanceOf(BusinessRuleException.class);
    }

    private static class TestEntity extends Entity {
        private final BusinessRule businessRule;

        public TestEntity(BusinessRule businessRule) {
            this.businessRule = businessRule;
        }

        public Try<Void> businessOperation() {
            return checkRule(businessRule);
        }
    }

    private static class BooleanBusinessRule implements BusinessRule {
        private final boolean isBroken;

        public BooleanBusinessRule(final boolean isBroken) {
            this.isBroken = isBroken;
        }

        @Override
        public boolean isBroken() {
            return isBroken;
        }

        @Override
        public String message() {
            return "Some message";
        }
    }
}
