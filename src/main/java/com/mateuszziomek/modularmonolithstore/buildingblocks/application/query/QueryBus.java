package com.mateuszziomek.modularmonolithstore.buildingblocks.application.query;

import com.google.common.base.Preconditions;
import io.vavr.control.Try;

import java.util.HashMap;

public class QueryBus {
    private final HashMap<Class<? extends Query>, QueryHandler> handlers = new HashMap<>();

    public <T, U extends Query<T>> T dispatch(final U query) {
        Preconditions.checkNotNull(query, "Query can't be null");

        final var handler = handlers.get(query.getClass());
        if (handler == null) {
            throw new QueryHandlerNotFoundException();
        }

        return (T) handler.handle(query);
    }

    public <T, U extends Query<T>> Try<Void> registerQuery(final Class<U> query, final QueryHandler<T, U> handler) {
        Preconditions.checkNotNull(query, "Query can't be null");
        Preconditions.checkNotNull(handler, "Handler can't be null");

        if (handlers.containsKey(query)) {
            return Try.failure(new QueryHandlerAlreadyRegisteredException());
        }

        handlers.put(query, handler);

        return Try.success(null);
    }
}
