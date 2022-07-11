package com.mateuszziomek.modularmonolithstore.buildingblocks.module;

import reactor.core.publisher.Mono;

public interface MessageProcessorModule {
    Mono<Void> processMessages(int amount);
}
