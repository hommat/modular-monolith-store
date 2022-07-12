package com.mateuszziomek.modularmonolithstore.modules.user.domain.user;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.AggregateRoot;
import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.BusinessRuleException;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.event.UserPasswordChangedDomainEvent;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.HashedPassword;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PasswordHashingAlgorithm;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PlainPassword;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.event.UserRegisteredDomainEvent;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.rule.PasswordMustBeChangedToDifferentOneRule;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.rule.UsernameMustNotBeInUseRule;
import io.vavr.control.Try;

import java.util.Set;

public class User extends AggregateRoot<UserId> {
    private UserId userId;
    private Username username;
    private volatile HashedPassword hashedPassword;

    public User() {
        // Required for event sourcing
    }

    public static Try<User> register(
            final UserId userId,
            final Username username,
            final PlainPassword plainPassword,
            final PasswordHashingAlgorithm passwordHashingAlgorithm,
            final Set<Username> usernamesInUse
        ) {
        Preconditions.checkNotNull(userId, "User id can't be null");
        Preconditions.checkNotNull(username, "Username can't be null");
        Preconditions.checkNotNull(plainPassword, "Password can't be null");
        Preconditions.checkNotNull(passwordHashingAlgorithm, "Password hashing algorithm can't be null");
        Preconditions.checkNotNull(usernamesInUse, "Usernames in use can't be null");

        final var usernameMustNotBeInUseRule = new UsernameMustNotBeInUseRule(usernamesInUse, username);

        if (usernameMustNotBeInUseRule.isBroken()) {
            return Try.failure(new BusinessRuleException(usernameMustNotBeInUseRule));
        }

        final var user = new User();
        final var hashedPassword = passwordHashingAlgorithm.hash(plainPassword);
        user.raiseEvent(new UserRegisteredDomainEvent(userId, username, hashedPassword));
        return Try.success(user);
    }

    public synchronized Try<User> changePassword(
            final PlainPassword newPlainPassword,
            final PasswordHashingAlgorithm passwordHashingAlgorithm
    ) {
        Preconditions.checkNotNull(newPlainPassword, "Password can't be null");
        Preconditions.checkNotNull(passwordHashingAlgorithm, "Password hashing algorithm can't be null");

        final var newHashedPassword = passwordHashingAlgorithm.hash(newPlainPassword);

        return checkRule(new PasswordMustBeChangedToDifferentOneRule(hashedPassword, newHashedPassword))
                .andThen(() -> raiseEvent(new UserPasswordChangedDomainEvent(userId, newHashedPassword)))
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

    @Override
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
