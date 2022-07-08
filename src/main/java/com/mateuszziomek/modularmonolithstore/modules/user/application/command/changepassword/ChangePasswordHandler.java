package com.mateuszziomek.modularmonolithstore.modules.user.application.command.changepassword;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.ReactiveCommandHandler;
import com.mateuszziomek.modularmonolithstore.modules.user.application.exception.UserNotFoundException;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserId;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserRepository;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PasswordHashingAlgorithm;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PlainPassword;
import io.vavr.control.Try;
import reactor.core.publisher.Mono;

public class ChangePasswordHandler implements ReactiveCommandHandler<ChangePasswordCommand> {
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
    public Mono<Try<Void>> handle(final ChangePasswordCommand command) {
        Preconditions.checkNotNull(command, "Command can't be null");

        final var userId = new UserId(command.userId());
        final var password = new PlainPassword(command.password());

        return userRepository
                .findById(userId)
                .map(user -> user.changePassword(password, passwordHashingAlgorithm))
                .switchIfEmpty(Mono.just(Try.failure(new UserNotFoundException(userId))))
                .flatMap(changePasswordTry -> changePasswordTry.isSuccess()
                        ? userRepository.save(changePasswordTry.get()).map(unused -> Try.success(null))
                        : Mono.just(Try.failure(changePasswordTry.getCause()))
                );
    }
}
