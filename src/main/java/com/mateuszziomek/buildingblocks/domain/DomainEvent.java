package com.mateuszziomek.buildingblocks.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class DomainEvent {
    private final UUID id;
    private final LocalDateTime occurAt;

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
}
