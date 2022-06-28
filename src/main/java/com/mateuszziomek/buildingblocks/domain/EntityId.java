package com.mateuszziomek.buildingblocks.domain;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import java.util.UUID;

public class EntityId {
    private final UUID value;

    public EntityId(final UUID value) {
        Preconditions.checkNotNull(value, "Entity id can't be null");

        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AggregateId)) return false;
        EntityId entityId = (EntityId) o;
        return Objects.equal(value, entityId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
