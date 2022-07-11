package com.mateuszziomek.modularmonolithstore.buildingblocks.module;

import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.Command;
import reactor.core.publisher.Mono;

public interface CommandDispatcherModule {
    Mono<Void> dispatchCommand(Command command);
}
