package com.mateuszziomek.modularmonolithstore.buildingblocks.module;

import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.Command;
import io.vavr.control.Try;
import reactor.core.publisher.Mono;

public interface ModuleCommandDispatcher {
    Mono<Try<Void>> dispatchCommand(Command command);
}
