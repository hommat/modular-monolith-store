package com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.application.command;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandBus;
import com.mateuszziomek.modularmonolithstore.modules.user.application.command.changepassword.ChangePasswordCommand;
import com.mateuszziomek.modularmonolithstore.modules.user.application.command.changepassword.ChangePasswordHandler;
import com.mateuszziomek.modularmonolithstore.modules.user.application.command.register.RegisterCommand;
import com.mateuszziomek.modularmonolithstore.modules.user.application.command.register.RegisterHandler;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserRepository;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PasswordHashingAlgorithm;

public class CommandBusFactory {
    private CommandBusFactory() {}

    public static CommandBus create(
            final UserRepository userRepository,
            final PasswordHashingAlgorithm passwordHashingAlgorithm
    ) {
        Preconditions.checkNotNull(userRepository, "User repository can't be null");
        Preconditions.checkNotNull(passwordHashingAlgorithm, "Password hashing algorithm can't be null");

        final var commandBus = new CommandBus();

        commandBus.registerCommand(
                RegisterCommand.class,
                new RegisterHandler(userRepository, passwordHashingAlgorithm)
        );

        commandBus.registerCommand(
                ChangePasswordCommand.class,
                new ChangePasswordHandler(userRepository, passwordHashingAlgorithm)
        );

        return commandBus;
    }
}
