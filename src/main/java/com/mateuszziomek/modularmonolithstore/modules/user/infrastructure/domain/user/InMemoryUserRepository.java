package com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.domain.user;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox.OutboxMessageRepository;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.User;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserId;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserRepository;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.Username;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class InMemoryUserRepository implements UserRepository {
    private final OutboxMessageRepository outboxMessageRepository;
    private final HashMap<UserId, User> usersById = new HashMap<>(10000);
    private final Set<Username> usernamesInUse = new HashSet<>();

    public InMemoryUserRepository(final OutboxMessageRepository outboxMessageRepository) {
        Preconditions.checkNotNull(outboxMessageRepository, "Outbox message repository can't be null");

        this.outboxMessageRepository = outboxMessageRepository;
    }

    @Override
    public Mono<User> findById(final UserId userId) {
        Preconditions.checkNotNull(userId, "User id can't ve null");

        final var user = usersById.get(userId);

        return user == null ? Mono.empty() : Mono.just(user);
    }

    @Override
    public Mono<Boolean> isUsernameInUse(final Username username) {
        Preconditions.checkNotNull(username, "Username id can't ve null");

        return Mono.just(usernamesInUse.contains(username));
    }

    @Override
    public Mono<Void> save(final User user) {
        Preconditions.checkNotNull(user, "User id can't ve null");

        final var userId = user.id();

        return outboxMessageRepository
                .saveDomainEvents(user.pendingDomainEvents())
                .doOnSuccess(result -> {
                    user.markDomainEventsAsCommitted();
                    usersById.put(userId, user);
                    usernamesInUse.add(user.username());
                });
    }
}
