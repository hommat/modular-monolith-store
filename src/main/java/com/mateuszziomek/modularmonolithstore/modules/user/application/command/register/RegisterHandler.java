package com.mateuszziomek.modularmonolithstore.modules.user.application.command.register;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandHandler;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.*;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PasswordHashingAlgorithm;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PlainPassword;
import reactor.core.publisher.Mono;

public class RegisterHandler implements CommandHandler<RegisterCommand> {
    private final UserRepository userRepository;
    private final PasswordHashingAlgorithm passwordHashingAlgorithm;
    private final UserFactory userFactory;

    public RegisterHandler(
            final UserRepository userRepository,
            final PasswordHashingAlgorithm passwordHashingAlgorithm,
            final UserFactory userFactory
    ) {
        Preconditions.checkNotNull(userRepository, "User repository can't be null");
        Preconditions.checkNotNull(passwordHashingAlgorithm, "Password hashing algorithm can't be null");
        Preconditions.checkNotNull(userFactory, "User factory can't be null");

        this.userRepository = userRepository;
        this.passwordHashingAlgorithm = passwordHashingAlgorithm;
        this.userFactory = userFactory;
    }

    @Override
    public Mono<Void> handle(final RegisterCommand command) {
        Preconditions.checkNotNull(command, "Command can't be null");

        final var userId = new UserId(command.userId());
        final var username = new Username(command.username());
        final var password = new PlainPassword(command.password());
        final var user = userFactory.register(userId, username, password, passwordHashingAlgorithm);

        if (user.isFailure()) {
            return Mono.error(user.getCause());
        }

        return userRepository
                .save(user.get())
                .then();
    }
}
