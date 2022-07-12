package com.mateuszziomek.modularmonolithstore.buildingblocks.application.command;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class CommandValidationException extends RuntimeException {
    private final Validation<Seq<String>, ? extends Object> validation;

    public CommandValidationException(final Validation<Seq<String>, ? extends Object> validation) {
        this.validation = validation;
    }

    public Validation<Seq<String>, Object> validation() {
        return (Validation<Seq<String>, Object>) validation;
    }
}
