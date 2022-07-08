package com.mateuszziomek.modularmonolithstore.buildingblocks.application.command;

import io.vavr.control.Try;

public interface CommandHandler<T extends Command> {
    Try<Void> handle(T command);
}
