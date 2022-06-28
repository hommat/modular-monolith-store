package com.mateuszziomek.modularmonolithstore.modules.user.domain.user;

import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.event.UserPasswordChangedDomainEvent;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.event.UserRegisteredDomainEvent;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.HashedPassword;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PasswordHashingAlgorithm;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PlainPassword;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class UserTest {
    private final static String USER_PASSWORD = "example-password";

    @Test
    void userCanBeRegister() {
        // Arrange
        var userUuid = UUID.randomUUID();
        var userId = new UserId(userUuid);
        var username = new Username("example-username");
        var password = new PlainPassword("example-password");
        var passwordHashingAlgorithm = new TestPasswordHashingAlgorithm();

        // Act
        var user = User.register(userId, username, password, passwordHashingAlgorithm);

        // Assert
        assertThat(user.id()).isEqualTo(userId);
        assertThat(user.username()).isEqualTo(username);
        assertThat(user.hashedPassword().value()).isEqualTo("hashed-example-password");
        assertThat(user.domainEvents().length()).isEqualTo(1);

        var userRegisteredEvent = (UserRegisteredDomainEvent) user.domainEvents().get(0);
        assertThat(userRegisteredEvent.username()).isEqualTo(username);
        assertThat(userRegisteredEvent.password().value()).isEqualTo("hashed-example-password");
    }

    @Test
    void userPasswordCanBeChanged() {
        // Arrange
        var user = registerUser();
        var newPassword = new PlainPassword("another-user-password");
        var passwordHashingAlgorithm = new TestPasswordHashingAlgorithm();

        // Act
        var result = user.changePassword(newPassword, passwordHashingAlgorithm);

        // Assert
        assertThat(result.isSuccess()).isTrue();

        assertThat(user.hashedPassword().value()).isEqualTo("hashed-another-user-password");
        assertThat(user.domainEvents().length()).isEqualTo(1);

        var userPasswordChangedEvent = (UserPasswordChangedDomainEvent) user.domainEvents().get(0);
        assertThat(userPasswordChangedEvent.password().value()).isEqualTo("hashed-another-user-password");
    }

    @Test
    void userPasswordMustBeChangedToDifferentOne() {
        // Arrange
        var user = registerUser();
        var newPassword = new PlainPassword(USER_PASSWORD);
        var passwordHashingAlgorithm = new TestPasswordHashingAlgorithm();

        // Act
        var result = user.changePassword(newPassword, passwordHashingAlgorithm);

        // Assert
        assertThat(result.isFailure()).isTrue();
    }

    private User registerUser() {
        var userId = new UserId(UUID.randomUUID());
        var username = new Username("example-username");
        var password = new PlainPassword(USER_PASSWORD);
        var passwordHashingAlgorithm = new TestPasswordHashingAlgorithm();

        var user = User.register(userId, username, password, passwordHashingAlgorithm);
        user.clearDomainEvents();

        return user;
    }

    private static class TestPasswordHashingAlgorithm implements PasswordHashingAlgorithm {
        @Override
        public HashedPassword hash(PlainPassword plainPassword) {
            return new HashedPassword("hashed-" + plainPassword.value());
        }
    }
}
