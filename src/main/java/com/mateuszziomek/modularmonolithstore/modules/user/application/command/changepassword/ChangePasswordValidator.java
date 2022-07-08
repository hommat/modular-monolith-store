package com.mateuszziomek.modularmonolithstore.modules.user.application.command.changepassword;

import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandValidator;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

import java.util.UUID;

public class ChangePasswordValidator implements CommandValidator<ChangePasswordCommand> {
    private static final String USER_ID_ERROR = "User id is required";
    private static final String PASSWORD_ERROR = "Password is required";

    @Override
    public Validation<Seq<String>, ChangePasswordCommand> validate(final ChangePasswordCommand command) {
        return Validation
                .combine(
                    validateUserId(command.userId()),
                    validatePassword(command.password())
                )
                .ap(ChangePasswordCommand::new);
    }

    private Validation<String, UUID> validateUserId(final UUID userId) {
        if (userId == null) {
            return Validation.invalid(USER_ID_ERROR);
        }

        return Validation.valid(userId);
    }

    private Validation<String, String> validatePassword(final String password) {
        if (password == null || password.isBlank()) {
            return Validation.invalid(PASSWORD_ERROR);
        }

        return Validation.valid(password);
    }
}
