package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessage;

public class OutboxMessage {
    private final IntegrationMessage message;

    public OutboxMessage(final IntegrationMessage message) {
        Preconditions.checkNotNull(message, "Message can't be null");

        this.message = message;
    }

    public IntegrationMessage message() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OutboxMessage)) return false;
        var that = (OutboxMessage) o;
        return Objects.equal(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(message);
    }

    @Override
    public String toString() {
        return "OutboxMessage{" +
                "message=" + message +
                '}';
    }
}
