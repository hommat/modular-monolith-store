package com.mateuszziomek.modularmonolithstore.modules.cart.readmodel.cart;

import java.util.UUID;

public class DetailsCart {
    private final UUID id;

    private DetailsCart(final UUID id) {
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
        return "DetailsCart{" +
                "id=" + id +
                '}';
    }

    public static class Builder {
        private UUID id;

        public Builder id(UUID id) {
            this.id = id;

            return this;
        }

        public DetailsCart build() {
            return new DetailsCart(id);
        }
    }
}
