package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.inbox;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessage;

public class InboxMessage {
    private final IntegrationMessage message;

    public InboxMessage(final IntegrationMessage message) {
        Preconditions.checkNotNull(message, "Message can't be null");

        this.message = message;
    }

    public IntegrationMessage message() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InboxMessage)) return false;
        var that = (InboxMessage) o;
        return Objects.equal(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(message);
    }

    @Override
    public String toString() {
        return "InboxMessage{" +
                "message=" + message +
                '}';
    }
}
