package com.mateuszziomek.modularmonolithstore.buildingblocks.module;

import com.mateuszziomek.modularmonolithstore.buildingblocks.application.query.Query;

public interface QueryDispatcherModule {
    <T> T dispatchQuery(Query<T> query);
}
