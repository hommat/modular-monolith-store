package com.mateuszziomek.modularmonolithstore.modules.user.readmodel.user.details;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.User;

public class DetailsUserMapper {
    private DetailsUserMapper() {}

    public static DetailsUser fromModel(final User user) {
        Preconditions.checkNotNull(user, "User can't be null");

        return DetailsUser
                .builder()
                .id(user.id().value())
                .build();
    }
}
