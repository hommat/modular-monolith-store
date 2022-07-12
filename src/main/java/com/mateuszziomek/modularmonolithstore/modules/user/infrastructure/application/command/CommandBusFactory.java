package com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.application.command;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandLoggingDecorator;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandValidationDecorator;
import com.mateuszziomek.modularmonolithstore.modules.user.application.command.changepassword.ChangePasswordCommand;
import com.mateuszziomek.modularmonolithstore.modules.user.application.command.changepassword.ChangePasswordHandler;
import com.mateuszziomek.modularmonolithstore.modules.user.application.command.changepassword.ChangePasswordValidator;
import com.mateuszziomek.modularmonolithstore.modules.user.application.command.register.RegisterCommand;
import com.mateuszziomek.modularmonolithstore.modules.user.application.command.register.RegisterHandler;
import com.mateuszziomek.modularmonolithstore.modules.user.application.command.register.RegisterValidator;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserFactory;
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
        final var userFactory = new UserFactory();

        commandBus.registerCommand(
                RegisterCommand.class,
                new CommandLoggingDecorator<>(
                        new CommandValidationDecorator<>(
                                new RegisterHandler(userRepository, passwordHashingAlgorithm, userFactory),
                                new RegisterValidator()
                        )

                )
        );

        commandBus.registerCommand(
                ChangePasswordCommand.class,
                new CommandLoggingDecorator<>(
                        new CommandValidationDecorator<>(
                                new ChangePasswordHandler(userRepository, passwordHashingAlgorithm),
                                new ChangePasswordValidator()
                        )
                )
        );

        return commandBus;
    }
}
