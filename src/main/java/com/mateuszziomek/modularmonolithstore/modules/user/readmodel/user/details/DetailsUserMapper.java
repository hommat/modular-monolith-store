package com.mateuszziomek.modularmonolithstore.modules.user.readmodel.user.details;

import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.User;

public class DetailsUserMapper {
    private DetailsUserMapper() {}

    public static DetailsUser fromModel(User user) {
        return DetailsUser
                .builder()
                .id(user.id().value())
                .build();
    }
}
