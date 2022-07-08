package com.mateuszziomek.modularmonolithstore.buildingblocks.domain;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import java.util.UUID;

public class AggregateId {
    private final UUID value;

    public AggregateId(final UUID value) {
        Preconditions.checkNotNull(value, "Aggregate id can't be null");

        this.value = value;
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AggregateId)) return false;
        var aggregateId = (AggregateId) o;
        return Objects.equal(value, aggregateId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "AggregateId{" +
                "value=" + value +
                '}';
    }
}
