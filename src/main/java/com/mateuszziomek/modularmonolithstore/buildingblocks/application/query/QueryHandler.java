package com.mateuszziomek.modularmonolithstore.buildingblocks.application.query;

import io.vavr.control.Try;

public interface QueryHandler<T, U extends Query<T>> {
    Try<T> handle(U query);
}
