package com.mateuszziomek.modularmonolithstore.modules.user.application.command.register;

import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.Command;

import java.util.UUID;

public class RegisterCommand implements Command {
    private final UUID userId;
    private final String username;
    private final String password;

    public RegisterCommand(final UUID userId, final String username, final String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public UUID userId() {
        return userId;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }
}
