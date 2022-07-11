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
import reactor.core.publisher.Mono;

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
    public Mono<Void> handle(final RegisterCommand command) {
        Preconditions.checkNotNull(command, "Command can't be null");

        final var userId = new UserId(command.userId());
        final var username = new Username(command.username());
        final var password = new PlainPassword(command.password());

        return userRepository
                .isUsernameInUse(username)
                .flatMap(isUsernameInUse -> Boolean.TRUE.equals(isUsernameInUse)
                        ? Mono.error(new UsernameAlreadyInUseException(username))
                        : Mono.just(isUsernameInUse)
                )
                .map(result -> User.register(userId, username, password, passwordHashingAlgorithm))
                .flatMap(userRepository::save)
                .then();
    }
}
