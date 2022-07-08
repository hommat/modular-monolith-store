package com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.domain.user;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox.OutboxMessageRepository;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.User;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserId;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserRepository;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.Username;
import io.vavr.control.Option;

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
    public Option<User> findById(UserId userId) {
        Preconditions.checkNotNull(userId, "User id can't ve null");

        var user = usersById.get(userId);

        return user == null ? Option.none() : Option.of(user);
    }

    @Override
    public boolean isUsernameInUse(Username username) {
        Preconditions.checkNotNull(username, "Username id can't ve null");

        return usernamesInUse.contains(username);
    }

    @Override
    public void save(User user) {
        Preconditions.checkNotNull(user, "User id can't ve null");

        var userId = user.id();
        outboxMessageRepository.saveDomainEvents(user.pendingDomainEvents());
        user.markDomainEventsAsCommitted();
        usersById.put(userId, user);
        usernamesInUse.add(user.username());
    }
}
