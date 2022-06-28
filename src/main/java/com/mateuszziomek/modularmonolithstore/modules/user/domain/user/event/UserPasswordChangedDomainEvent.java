package com.mateuszziomek.modularmonolithstore.modules.user.domain.user.event;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.DomainEvent;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserId;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.HashedPassword;

public class UserPasswordChangedDomainEvent extends DomainEvent {
    private final UserId userId;
    private final HashedPassword hashedPassword;

    public UserPasswordChangedDomainEvent(
            final UserId userId,
            final HashedPassword hashedPassword
    ) {
        Preconditions.checkNotNull(userId, "User id can't be null");
        Preconditions.checkNotNull(hashedPassword, "Password can't be null");

        this.userId = userId;
        this.hashedPassword = hashedPassword;
    }

    public UserId userId() {
        return userId;
    }

    public HashedPassword password() {
        return hashedPassword;
    }
}
