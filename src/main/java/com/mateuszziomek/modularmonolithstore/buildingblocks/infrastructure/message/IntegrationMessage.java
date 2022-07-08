package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message;

import com.google.common.base.Objects;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class IntegrationMessage {
    protected final UUID messageId;
    protected final LocalDateTime occurAt;

    protected IntegrationMessage() {
        messageId = UUID.randomUUID();
        occurAt = LocalDateTime.now();
    }

    public UUID messageId() {
        return messageId;
    }

    public LocalDateTime occurAt() {
        return occurAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntegrationMessage)) return false;
        var that = (IntegrationMessage) o;
        return Objects.equal(messageId, that.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(messageId);
    }
}
