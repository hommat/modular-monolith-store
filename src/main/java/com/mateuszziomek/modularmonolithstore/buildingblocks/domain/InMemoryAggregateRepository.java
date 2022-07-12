package com.mateuszziomek.modularmonolithstore.buildingblocks.domain;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox.OutboxMessageRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class InMemoryAggregateRepository<U extends AggregateId, T extends AggregateRoot<U>>
        implements AggregateRepository<U, T> {
    private static final Duration AGGREGATE_LOCK_INTERVAL = Duration.ofMillis(5);
    private final OutboxMessageRepository outboxMessageRepository;
    private final ConcurrentHashMap<U, T> aggregatesById = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<U, Lock> aggregateLocksById = new ConcurrentHashMap<>();

    public InMemoryAggregateRepository(final OutboxMessageRepository outboxMessageRepository) {
        Preconditions.checkNotNull(outboxMessageRepository, "Outbox message repository can't be null");

        this.outboxMessageRepository = outboxMessageRepository;
    }

    @Override
    public Mono<T> findById(final U aggregateId) {
        Preconditions.checkNotNull(aggregateId, "Aggregate id can't ve null");

        final var aggregate = aggregatesById.get(aggregateId);

        return aggregate == null ? Mono.empty() : Mono.just(aggregate);
    }

    @Override
    public Mono<Void> save(final T aggregate) {
        Preconditions.checkNotNull(aggregate, "Aggregate can't ve null");

        final var aggregateId = aggregate.id();
        final var aggregateLock = createAggreagateLock(aggregateId);

        return lockReactive(aggregateLock)
                .then(Mono.defer(() -> Mono.just(aggregate.pendingDomainEvents())))
                .flatMap(pendingEvents -> outboxMessageRepository.saveDomainEvents(pendingEvents).then(Mono.just(pendingEvents)))
                .doOnSuccess(pendingEvents -> {
                    aggregate.markDomainEventsAsCommitted(pendingEvents);
                    aggregatesById.put(aggregateId, aggregate);
                })
                .doOnTerminate(() -> unlockAggregate(aggregateId))
                .then();
    }

    private Lock createAggreagateLock(final U aggregateId) {
        return aggregateLocksById.computeIfAbsent(aggregateId, id -> new ReentrantLock());
    }

    private void unlockAggregate(final U aggregateId) {
        aggregateLocksById.get(aggregateId).unlock();
    }

    private Mono<Void> lockReactive(final Lock lock) {
        return Flux.interval(AGGREGATE_LOCK_INTERVAL)
                .filter(unused -> lock.tryLock())
                .next()
                .then();
    }
}
