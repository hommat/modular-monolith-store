package com.mateuszziomek.modularmonolithstore.buildingblocks.domain;

import reactor.core.publisher.Mono;

public interface AggregateRepository<U extends AggregateId, T extends AggregateRoot<U>> {
    Mono<T> findById(U aggregateId);
    Mono<Void> save(T aggregate);
}
