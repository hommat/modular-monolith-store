package com.mateuszziomek.modularmonolithstore.modules.user.domain.user.rule;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.BusinessRule;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.HashedPassword;

public class PasswordMustBeChangedToDifferentOneRule implements BusinessRule {
    private final HashedPassword currentPassword;
    private final HashedPassword newPassword;

    public PasswordMustBeChangedToDifferentOneRule(
            final HashedPassword currentPassword,
            final HashedPassword newPassword
    ) {
        Preconditions.checkNotNull(currentPassword, "Current password can't be null");
        Preconditions.checkNotNull(newPassword, "New password can't be null");

        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    @Override
    public boolean isBroken() {
        return currentPassword.equals(newPassword);
    }

    @Override
    public String message() {
        return "User password can't be changed to current";
    }
}
