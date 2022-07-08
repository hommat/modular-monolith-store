package com.mateuszziomek.modularmonolithstore.modules.user.domain.user;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.AggregateRoot;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.event.UserPasswordChangedDomainEvent;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.HashedPassword;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PasswordHashingAlgorithm;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PlainPassword;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.event.UserRegisteredDomainEvent;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.rule.PasswordMustBeChangedToDifferentOneRule;
import io.vavr.control.Try;

public class User extends AggregateRoot {
    private UserId userId;
    private Username username;
    private HashedPassword hashedPassword;

    public User() {
        // Required for event sourcing
    }

    public static User register(
            final UserId userId,
            final Username username,
            final PlainPassword plainPassword,
            final PasswordHashingAlgorithm passwordHashingAlgorithm
    ) {
        Preconditions.checkNotNull(userId, "User id can't be null");
        Preconditions.checkNotNull(username, "Username can't be null");
        Preconditions.checkNotNull(plainPassword, "Password can't be null");
        Preconditions.checkNotNull(passwordHashingAlgorithm, "Password hashing algorithm can't be null");

        final var user = new User();
        final var hashedPassword = passwordHashingAlgorithm.hash(plainPassword);
        user.raiseEvent(new UserRegisteredDomainEvent(userId, username, hashedPassword));
        return user;
    }

    public Try<User> changePassword(
            final PlainPassword newPlainPassword,
            final PasswordHashingAlgorithm passwordHashingAlgorithm
    ) {
        Preconditions.checkNotNull(newPlainPassword, "Password can't be null");
        Preconditions.checkNotNull(passwordHashingAlgorithm, "Password hashing algorithm can't be null");

        final var newHashedPassword = passwordHashingAlgorithm.hash(newPlainPassword);

        return checkRule(new PasswordMustBeChangedToDifferentOneRule(hashedPassword, newHashedPassword))
                .andThen(() -> raiseEvent(new UserPasswordChangedDomainEvent(userId, hashedPassword)))
                .map(unused -> this);
    }

    public void on(UserRegisteredDomainEvent event) {
        userId = event.userId();
        username = event.username();
        hashedPassword = event.password();
    }

    public void on(UserPasswordChangedDomainEvent event) {
        hashedPassword = event.password();
    }

    public UserId id() {
        return userId;
    }

    public Username username() {
        return username;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username=" + username +
                '}';
    }
}
