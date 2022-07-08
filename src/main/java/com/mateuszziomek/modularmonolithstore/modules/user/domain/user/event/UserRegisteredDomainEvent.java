package com.mateuszziomek.modularmonolithstore.modules.user.domain.user.event;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.DomainEvent;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserId;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.HashedPassword;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.Username;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRegisteredDomainEvent)) return false;
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "UserRegisteredDomainEvent{" +
                "userId=" + userId +
                ", username=" + username +
                ", id=" + id +
                ", occurAt=" + occurAt +
                '}';
    }
}
