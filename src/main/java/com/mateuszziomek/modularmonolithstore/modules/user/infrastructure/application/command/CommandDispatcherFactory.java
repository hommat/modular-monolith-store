package com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.application.command;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandDispatcher;
import com.mateuszziomek.modularmonolithstore.modules.user.application.command.changepassword.ChangePasswordCommand;
import com.mateuszziomek.modularmonolithstore.modules.user.application.command.changepassword.ChangePasswordHandler;
import com.mateuszziomek.modularmonolithstore.modules.user.application.command.register.RegisterCommand;
import com.mateuszziomek.modularmonolithstore.modules.user.application.command.register.RegisterHandler;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserRepository;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PasswordHashingAlgorithm;

public class CommandDispatcherFactory {
    private CommandDispatcherFactory() {}

    public static CommandDispatcher create(
            final UserRepository userRepository,
            final PasswordHashingAlgorithm passwordHashingAlgorithm
    ) {
        Preconditions.checkNotNull(userRepository, "User repository can't be null");
        Preconditions.checkNotNull(passwordHashingAlgorithm, "Password hashing algorithm can't be null");

        final var commandDispatcher = new CommandDispatcher();

        commandDispatcher.registerCommand(
                RegisterCommand.class,
                new RegisterHandler(userRepository, passwordHashingAlgorithm)
        );

        commandDispatcher.registerCommand(
                ChangePasswordCommand.class,
                new ChangePasswordHandler(userRepository, passwordHashingAlgorithm)
        );

        return commandDispatcher;
    }
}
