package com.mateuszziomek.modularmonolithstore.modules.user.domain.user;

import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.AbstractAggregateRootTest;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.event.UserPasswordChangedDomainEvent;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.event.UserRegisteredDomainEvent;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.HashedPassword;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PasswordHashingAlgorithm;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PlainPassword;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class UserTest extends AbstractAggregateRootTest {
    private final static String DEFAULT_PASSWORD = "default_password";

    @Test
    void userCanBeRegistered() {
        // Arrange
        var userUUID = UUID.randomUUID();
        var userId = new UserId(userUUID);
        var username = new Username("Example username");
        var password = new PlainPassword("Example plain password");

        // Act
        var sut = User.register(userId, username, password, new TestPasswordHashingAlgorithm());

        // Assert
        assertThatNumberOfPendingEventsIs(sut, 1);

        var event = (UserRegisteredDomainEvent) sut.pendingDomainEvents().get(0);
        assertThat(event).isInstanceOf(UserRegisteredDomainEvent.class);
        assertThat(event.userId()).isEqualTo(new UserId(userUUID));
        assertThat(event.username()).isEqualTo(new Username("Example username"));
        assertThat(event.password()).isEqualTo(new HashedPassword("h_Example plain password"));
    }

    @Test
    void userPasswordCanBeChanged() {
        // Arrange
        var sut = registerUser();
        var password = new PlainPassword("changed password");

        // Act
        var result = sut.changePassword(password, new TestPasswordHashingAlgorithm());

        // Assert
        assertThat(result.isSuccess()).isTrue();
        assertThatNumberOfPendingEventsIs(sut, 1);

        var event = (UserPasswordChangedDomainEvent) sut.pendingDomainEvents().get(0);
        assertThat(event).isInstanceOf(UserPasswordChangedDomainEvent.class);
        assertThat(event.userId()).isEqualTo(sut.id());
        assertThat(event.password()).isEqualTo(new HashedPassword("h_changed password"));
    }

    @Test
    void userPasswordMustBeChangeToDifferentOne() {
        // Arrange
        var sut = registerUser();
        var password = new PlainPassword(DEFAULT_PASSWORD);

        // Act
        var result = sut.changePassword(password, new TestPasswordHashingAlgorithm());

        // Assert
        assertThat(result.isFailure()).isTrue();
        assertThatNumberOfPendingEventsIs(sut, 0);
    }

    private User registerUser() {
        var userId = new UserId(UUID.randomUUID());
        var username = new Username("Example username");
        var password = new PlainPassword(DEFAULT_PASSWORD);
        var passwordHashingAlgorithm = new TestPasswordHashingAlgorithm();

        var user = User.register(userId, username, password, passwordHashingAlgorithm);
        user.markDomainEventsAsCommitted();

        return user;
    }

    private static class TestPasswordHashingAlgorithm implements PasswordHashingAlgorithm {
        @Override
        public HashedPassword hash(PlainPassword plainPassword) {
            return new HashedPassword("h_" + plainPassword.value());
        }
    }
}