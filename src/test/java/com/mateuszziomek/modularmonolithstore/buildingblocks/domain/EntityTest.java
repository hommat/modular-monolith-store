package com.mateuszziomek.modularmonolithstore.buildingblocks.domain;

import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.Preconditions;

import static org.assertj.core.api.Assertions.*;

class EntityTest {
    @Test
    void checkingRuleReturnsSuccess() {
        // Arrange
        var rule = new SuccessRule();
        var sut = new TestEntity();

        // Act
        var result = sut.ruleCheckingAction(rule);

        // Assert
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void checkingRuleReturnsFailure() {
        // Arrange
        var rule = new FailureRule();
        var sut = new TestEntity();

        // Act
        var result = sut.ruleCheckingAction(rule);

        // Assert
        assertThat(result.isFailure()).isTrue();
    }

    private static class TestEntity extends Entity {
        public Try<Void> ruleCheckingAction(final BusinessRule rule) {
            Preconditions.notNull(rule, "Rule can't be null");

            return checkRule(rule);
        }
    }

    private static class SuccessRule implements BusinessRule {
        @Override
        public boolean isBroken() {
            return false;
        }

        @Override
        public String message() {
            return "Success";
        }
    }

    private static class FailureRule implements BusinessRule {
        @Override
        public boolean isBroken() {
            return true;
        }

        @Override
        public String message() {
            return "Failure";
        }
    }
}
