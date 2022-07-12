package com.mateuszziomek.modularmonolithstore.modules.user.domain.user.rule;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.BusinessRule;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.Username;

import java.util.Set;

public class UsernameMustNotBeInUseRule implements BusinessRule {
    private final Set<Username> usernamesInUse;
    private final Username username;

    public UsernameMustNotBeInUseRule(final Set<Username> usernamesInUse, final Username username) {
        Preconditions.checkNotNull(usernamesInUse, "Usernames can't be null");
        Preconditions.checkNotNull(username, "Username can't be null");

        this.usernamesInUse = usernamesInUse;
        this.username = username;
    }

    @Override
    public boolean isBroken() {
        return usernamesInUse.contains(username);
    }

    @Override
    public String message() {
        return "Username must not be in use";
    }
}
