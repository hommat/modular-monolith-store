package com.mateuszziomek.modularmonolithstore.buildingblocks.application.command;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public interface CommandValidator<T extends Command> {
    Validation<Seq<String>, T> validate(T command);
}
