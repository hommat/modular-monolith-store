package com.mateuszziomek.modularmonolithstore.modules.user.application.command.register;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandHandler;
import com.mateuszziomek.modularmonolithstore.modules.user.application.exception.UsernameAlreadyInUseException;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.User;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserId;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserRepository;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.Username;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PasswordHashingAlgorithm;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PlainPassword;
import io.vavr.control.Try;

public class RegisterHandler implements CommandHandler<RegisterCommand> {
    private final UserRepository userRepository;
    private final PasswordHashingAlgorithm passwordHashingAlgorithm;

    public RegisterHandler(
            final UserRepository userRepository,
            final PasswordHashingAlgorithm passwordHashingAlgorithm
    ) {
        Preconditions.checkNotNull(userRepository, "User repository can't be null");
        Preconditions.checkNotNull(passwordHashingAlgorithm, "Password hashing algorithm can't be null");

        this.userRepository = userRepository;
        this.passwordHashingAlgorithm = passwordHashingAlgorithm;
    }

    @Override
    public Try<Void> handle(final RegisterCommand command) {
        Preconditions.checkNotNull(command, "Command can't be null");

        final var userId = new UserId(command.userId());
        final var username = new Username(command.username());
        final var password = new PlainPassword(command.password());

        if (userRepository.isUsernameInUse(username)) {
            return Try.failure(new UsernameAlreadyInUseException(username));
        }

        final var user = User.register(userId, username, password, passwordHashingAlgorithm);
        userRepository.save(user);

        return Try.success(null);
    }
}
