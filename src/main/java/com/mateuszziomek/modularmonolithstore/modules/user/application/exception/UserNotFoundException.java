package com.mateuszziomek.modularmonolithstore.modules.user.application.exception;

import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserId;

public class UserNotFoundException extends RuntimeException {
    private final transient UserId userId;

    public UserNotFoundException(UserId userId) {
        this.userId = userId;
    }

    public UserId userId() {
        return userId;
    }
}
