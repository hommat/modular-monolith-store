package com.mateuszziomek.modularmonolithstore.modules.user.application.command.changepassword;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandHandler;
import com.mateuszziomek.modularmonolithstore.modules.user.application.exception.UserNotFoundException;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserId;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserRepository;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PasswordHashingAlgorithm;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PlainPassword;
import io.vavr.control.Try;

public class ChangePasswordHandler implements CommandHandler<ChangePasswordCommand> {
    private final UserRepository userRepository;
    private final PasswordHashingAlgorithm passwordHashingAlgorithm;

    public ChangePasswordHandler(
            final UserRepository userRepository,
            final PasswordHashingAlgorithm passwordHashingAlgorithm
    ) {
        Preconditions.checkNotNull(userRepository, "User repository can't be null");
        Preconditions.checkNotNull(passwordHashingAlgorithm, "Password hashing algorithm can't be null");

        this.userRepository = userRepository;
        this.passwordHashingAlgorithm = passwordHashingAlgorithm;
    }

    @Override
    public Try<Void> handle(final ChangePasswordCommand command) {
        Preconditions.checkNotNull(command, "Command can't be null");

        final var userId = new UserId(command.userId());
        final var password = new PlainPassword(command.password());
        final var userOption =  userRepository.findById(userId);

        if (userOption.isEmpty()) {
            return Try.failure(new UserNotFoundException(userId));
        }

        final var user = userOption.get();

        synchronized (user) {
            return user.changePassword(password, passwordHashingAlgorithm)
                    .andThen(userRepository::save)
                    .flatMap(unused -> Try.success(null));
        }
    }
}
