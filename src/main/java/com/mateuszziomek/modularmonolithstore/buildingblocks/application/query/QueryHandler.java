package com.mateuszziomek.modularmonolithstore.buildingblocks.application.query;

public interface QueryHandler<T, U extends Query<T>> {
    T handle(U query);
}

