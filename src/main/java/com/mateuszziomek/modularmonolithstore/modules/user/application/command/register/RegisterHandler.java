package com.mateuszziomek.modularmonolithstore.modules.user.application.command.register;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.ReactiveCommandHandler;
import com.mateuszziomek.modularmonolithstore.modules.user.application.exception.UsernameAlreadyInUseException;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.User;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserId;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserRepository;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.Username;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PasswordHashingAlgorithm;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PlainPassword;
import io.vavr.control.Try;
import reactor.core.publisher.Mono;

public class RegisterHandler implements ReactiveCommandHandler<RegisterCommand> {
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
    public Mono<Try<Void>> handle(final RegisterCommand command) {
        Preconditions.checkNotNull(command, "Command can't be null");

        final var userId = new UserId(command.userId());
        final var username = new Username(command.username());
        final var password = new PlainPassword(command.password());

        return userRepository
                .isUsernameInUse(username)
                .map(isUsernameInUse -> Boolean.TRUE.equals(isUsernameInUse)
                        ? Try.<User>failure(new UsernameAlreadyInUseException(username))
                        : Try.success(User.register(userId, username, password, passwordHashingAlgorithm))
                )
                .flatMap(registerTry -> registerTry.isSuccess()
                        ? userRepository.save(registerTry.get()).map(unused -> Try.success(null))
                        : Mono.just(Try.failure(registerTry.getCause()))
                );
    }
}
