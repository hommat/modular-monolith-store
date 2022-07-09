package com.mateuszziomek.modularmonolithstore.buildingblocks.module;

import com.mateuszziomek.modularmonolithstore.buildingblocks.application.query.Query;
import io.vavr.control.Try;

public interface QueryDispatcherModule {
    <T> Try<T> dispatchQuery(Query<T> query);
}
