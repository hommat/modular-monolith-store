package com.mateuszziomek.modules.user.domain.user.event;

import com.google.common.base.Preconditions;
import com.mateuszziomek.buildingblocks.domain.DomainEvent;
import com.mateuszziomek.modules.user.domain.user.UserId;
import com.mateuszziomek.modules.user.domain.user.password.HashedPassword;
import com.mateuszziomek.modules.user.domain.user.Username;

public class UserRegisteredDomainEvent extends DomainEvent {
    private final UserId userId;
    private final Username username;
    private final HashedPassword hashedPassword;

    public UserRegisteredDomainEvent(
            final UserId userId,
            final Username username,
            final HashedPassword hashedPassword
    ) {
        Preconditions.checkNotNull(userId, "User id can't be null");
        Preconditions.checkNotNull(username, "Username can't be null");
        Preconditions.checkNotNull(hashedPassword, "Password can't be null");

        this.userId = userId;
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    public UserId userId() {
        return userId;
    }

    public Username username() {
        return username;
    }

    public HashedPassword password() {
        return hashedPassword;
    }
}
