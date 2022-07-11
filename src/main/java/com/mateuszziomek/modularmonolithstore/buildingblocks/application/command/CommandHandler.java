package com.mateuszziomek.modularmonolithstore.buildingblocks.application.command;

import reactor.core.publisher.Mono;

public interface CommandHandler<T extends Command> {
    Mono<Void> handle(T command);
}
