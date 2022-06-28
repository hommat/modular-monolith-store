package com.mateuszziomek.modules.user.domain.user;

import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> findById(UserId userId);
    Mono<Void> save(User user);
}
