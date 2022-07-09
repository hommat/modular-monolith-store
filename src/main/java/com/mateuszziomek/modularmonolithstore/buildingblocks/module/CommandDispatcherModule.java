package com.mateuszziomek.modularmonolithstore.buildingblocks.module;

import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.Command;
import io.vavr.control.Try;

public interface CommandDispatcherModule {
    Try<Void> dispatchCommand(Command command);
}
