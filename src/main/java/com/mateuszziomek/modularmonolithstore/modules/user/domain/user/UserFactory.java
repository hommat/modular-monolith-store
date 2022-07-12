package com.mateuszziomek.modularmonolithstore.modules.user.domain.user;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PasswordHashingAlgorithm;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PlainPassword;
import io.vavr.control.Try;

import java.util.HashSet;
import java.util.Set;

public class UserFactory {
    private final Set<Username> usernamesInUse = new HashSet<>();

    public synchronized Try<User> register(
            final UserId userId,
            final Username username,
            final PlainPassword plainPassword,
            final PasswordHashingAlgorithm passwordHashingAlgorithm
    ) {
        Preconditions.checkNotNull(userId, "User id can't be null");
        Preconditions.checkNotNull(username, "Username can't be null");
        Preconditions.checkNotNull(plainPassword, "Password can't be null");
        Preconditions.checkNotNull(passwordHashingAlgorithm, "Password hashing algorithm can't be null");

        final var result = User.register(
                userId,
                username,
                plainPassword,
                passwordHashingAlgorithm,
                usernamesInUse
        );

        if (result.isSuccess()) {
            usernamesInUse.add(username);
        }

        return result;
    }
}
