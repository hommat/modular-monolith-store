package com.mateuszziomek.modularmonolithstore.modules.user.domain.user;

import io.vavr.control.Option;

public interface UserRepository {
    Option<User> findById(UserId userId);
    boolean isUsernameInUse(Username username);
    void save(User user);
}
