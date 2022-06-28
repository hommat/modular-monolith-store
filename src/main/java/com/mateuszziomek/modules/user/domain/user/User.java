package com.mateuszziomek.modules.user.domain.user;

import com.google.common.base.Preconditions;
import com.mateuszziomek.buildingblocks.domain.AggregateRoot;
import com.mateuszziomek.modules.user.domain.user.event.UserPasswordChangedDomainEvent;
import com.mateuszziomek.modules.user.domain.user.password.HashedPassword;
import com.mateuszziomek.modules.user.domain.user.password.PasswordHashingAlgorithm;
import com.mateuszziomek.modules.user.domain.user.password.PlainPassword;
import com.mateuszziomek.modules.user.domain.user.event.UserRegisteredDomainEvent;
import com.mateuszziomek.modules.user.domain.user.rule.PasswordMustBeChangedToDifferentOneRule;
import io.vavr.control.Try;

public class User extends AggregateRoot {
    private UserId userId;
    private Username username;
    private HashedPassword hashedPassword;

    private User() {}

    public User(
            final UserId userId,
            final Username username,
            final HashedPassword hashedPassword
    ) {
        Preconditions.checkNotNull(userId, "User id can't be null");
        Preconditions.checkNotNull(username, "Username can't be null");
        Preconditions.checkNotNull(hashedPassword, "Password can't be null");

        this.userId = userId;
        this.username = username;
        this.hashedPassword = hashedPassword;
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

        var user = new User();
        var hashedPassword = passwordHashingAlgorithm.hash(plainPassword);
        user.userId = userId;
        user.username = username;
        user.hashedPassword = hashedPassword;
        user.addDomainEvent(new UserRegisteredDomainEvent(userId, username, hashedPassword));
        return user;
    }

    public Try<Void> changePassword(
            final PlainPassword newPlainPassword,
            final PasswordHashingAlgorithm passwordHashingAlgorithm
    ) {
        Preconditions.checkNotNull(newPlainPassword, "Password can't be null");
        Preconditions.checkNotNull(passwordHashingAlgorithm, "Password hashing algorithm can't be null");

        var newHashedPassword = passwordHashingAlgorithm.hash(newPlainPassword);

        return checkRule(new PasswordMustBeChangedToDifferentOneRule(hashedPassword, newHashedPassword))
                .andThen(() -> {
                    hashedPassword = newHashedPassword;
                    addDomainEvent(new UserPasswordChangedDomainEvent(userId, hashedPassword));
                });
    }

    public UserId userId() {
        return userId;
    }

    public Username username() {
        return username;
    }

    public HashedPassword hashedPassword() {
        return hashedPassword;
    }
}
