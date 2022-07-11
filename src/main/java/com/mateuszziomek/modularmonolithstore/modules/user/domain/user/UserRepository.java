package com.mateuszziomek.modularmonolithstore.modules.user.domain.user;

import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> findById(UserId userId);
    Mono<Boolean> isUsernameInUse(Username username);
    Mono<Void> save(User user);
}
