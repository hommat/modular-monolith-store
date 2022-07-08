package com.mateuszziomek.modularmonolithstore.buildingblocks.application.command;

import io.vavr.control.Try;
import reactor.core.publisher.Mono;

public interface ReactiveCommandHandler<T extends Command> {
    Mono<Try<Void>> handle(T command);
}
