package com.mateuszziomek.modularmonolithstore.modules.user.application.exception;

import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.Username;

public class UsernameAlreadyInUseException extends RuntimeException {
    private final transient Username username;

    public UsernameAlreadyInUseException(Username username) {
        this.username = username;
    }

    public Username username() {
        return username;
    }
}