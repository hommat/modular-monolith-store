package com.mateuszziomek.modularmonolithstore.modules.user.application.command.register;

import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandValidator;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

import java.util.UUID;

public class RegisterValidator implements CommandValidator<RegisterCommand> {
    private static final String USER_ID_ERROR = "User id is required";
    private static final String USERNAME_ERROR = "Username is required";
    private static final String PASSWORD_ERROR = "Password is required";

    @Override
    public Validation<Seq<String>, RegisterCommand> validate(final RegisterCommand command) {
        return Validation
                .combine(
                    validateUserId(command.userId()),
                    validateUsername(command.username()),
                    validatePassword(command.password())
                )
                .ap(RegisterCommand::new);
    }

    private Validation<String, UUID> validateUserId(final UUID userId) {
        if (userId == null) {
            return Validation.invalid(USER_ID_ERROR);
        }

        return Validation.valid(userId);
    }

    private Validation<String, String> validateUsername(final String username) {
        if (username == null || username.isBlank()) {
            return Validation.invalid(USERNAME_ERROR);
        }

        return Validation.valid(username);
    }

    private Validation<String, String> validatePassword(final String password) {
        if (password == null || password.isBlank()) {
            return Validation.invalid(PASSWORD_ERROR);
        }

        return Validation.valid(password);
    }
}
