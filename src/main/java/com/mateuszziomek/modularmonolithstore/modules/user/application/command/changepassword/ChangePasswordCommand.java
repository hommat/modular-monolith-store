package com.mateuszziomek.modularmonolithstore.modules.user.application.command.changepassword;

import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.Command;

import java.util.UUID;

public class ChangePasswordCommand implements Command {
    private final UUID userId;
    private final String password;

    public ChangePasswordCommand(final UUID userId, final String password) {
        this.userId = userId;
        this.password = password;
    }

    public UUID userId() {
        return userId;
    }

    public String password() {
        return password;
    }
}
