package com.mateuszziomek.modularmonolithstore.modules.user.readmodel.user.details;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserId;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserRepository;
import io.vavr.control.Option;

import java.util.UUID;

public class DetailsUserFinder {
    private final UserRepository userRepository;

    public DetailsUserFinder(UserRepository userRepository) {
        Preconditions.checkNotNull(userRepository, "User repository can't be null");

        this.userRepository = userRepository;
    }

    public Option<DetailsUser> findById(final UUID id) {
        Preconditions.checkNotNull(id, "Id can't be null");

        return userRepository.findById(new UserId(id)).map(DetailsUserMapper::fromModel);
    }
}
