package com.mateuszziomek.modularmonolithstore.modules.user.integration.event;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationEvent;

import java.util.UUID;

public class UserRegisteredIntegrationEvent extends IntegrationEvent {
    private final UUID userId;
    private final String username;

    public UserRegisteredIntegrationEvent(final UUID userId, final String username) {
        Preconditions.checkNotNull(userId, "User id can't be null");
        Preconditions.checkNotNull(username, "Username can't be null");

        this.userId = userId;
        this.username = username;
    }

    public UUID userId() {
        return userId;
    }

    public String username() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRegisteredIntegrationEvent)) return false;
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "UserRegisteredIntegrationEvent{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", messageId=" + messageId +
                ", occurAt=" + occurAt +
                '}';
    }
}
