package com.mateuszziomek.modularmonolithstore.modules.user.application.exception;

import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserId;

public class UserNotFoundException extends RuntimeException {
    private final transient UserId userId;

    public UserNotFoundException(final UserId userId) {
        super("User not found");
        this.userId = userId;
    }

    public UserId userId() {
        return userId;
    }
}
