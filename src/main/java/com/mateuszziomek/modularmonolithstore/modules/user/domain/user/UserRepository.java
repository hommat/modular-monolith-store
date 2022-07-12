package com.mateuszziomek.modularmonolithstore.modules.user.domain.user;

import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.AggregateRepository;

public interface UserRepository extends AggregateRepository<UserId, User> { }
