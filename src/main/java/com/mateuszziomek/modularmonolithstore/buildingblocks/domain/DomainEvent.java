package com.mateuszziomek.modularmonolithstore.buildingblocks.domain;

import com.google.common.base.Objects;

import java.time.LocalDateTime;
import java.util.UUID;

public class DomainEvent {
    protected final UUID id;
    protected final LocalDateTime occurAt;

    public DomainEvent() {
        id = UUID.randomUUID();
        occurAt = LocalDateTime.now();
    }

    public UUID id() {
        return id;
    }

    public LocalDateTime occurAt() {
        return occurAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainEvent)) return false;
        var that = (DomainEvent) o;
        return Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
