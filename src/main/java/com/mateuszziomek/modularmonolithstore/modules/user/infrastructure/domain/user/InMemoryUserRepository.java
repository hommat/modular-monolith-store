package com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.domain.user;

import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.InMemoryAggregateRepository;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox.OutboxMessageRepository;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.User;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserId;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserRepository;

public class InMemoryUserRepository extends InMemoryAggregateRepository<UserId, User> implements UserRepository {
    public InMemoryUserRepository(final OutboxMessageRepository outboxMessageRepository) {
        super(outboxMessageRepository);
    }
}
