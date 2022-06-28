package com.mateuszziomek.modules.user.domain.user;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class Username {
    private final String value;

    public Username(final String value) {
        Preconditions.checkNotNull(value, "Username can't be null");
        Preconditions.checkArgument(value.isBlank(), "Username can't be blank");

        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Username)) return false;
        Username username = (Username) o;
        return Objects.equal(value, username.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
