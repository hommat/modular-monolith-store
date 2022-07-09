package com.mateuszziomek.modularmonolithstore.modules.user.readmodel.user.details;

import java.util.UUID;

public class DetailsUser {
    private final UUID id;

    private DetailsUser(final UUID id) {
        this.id = id;
    }

    public static Builder builder() {
        return new Builder();
    }

    public UUID id() {
        return id;
    }

    @Override
    public String toString() {
        return "DetailsUser{" +
                "id=" + id +
                '}';
    }

    public static class Builder {
        private UUID id;

        public Builder id(UUID id) {
            this.id = id;

            return this;
        }

        public DetailsUser build() {
            return new DetailsUser(id);
        }
    }
}
