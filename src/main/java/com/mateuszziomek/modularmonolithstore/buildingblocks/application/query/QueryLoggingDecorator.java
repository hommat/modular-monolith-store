package com.mateuszziomek.modularmonolithstore.buildingblocks.application.query;

import com.google.common.base.Preconditions;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryLoggingDecorator<T, U extends Query<T>> implements QueryHandler<T, U> {
    private static final Logger LOG = LoggerFactory.getLogger(QueryLoggingDecorator.class.getSimpleName());

    private final QueryHandler<T, U> handler;

    public QueryLoggingDecorator(final QueryHandler<T, U> handler) {
        Preconditions.checkNotNull(handler, "Handler can't be null");

        this.handler = handler;
    }

    @Override
    public Try<T> handle(final U query) {
        Preconditions.checkNotNull(query, "Query can't be null");
        LOG.info("Executing query {}", query.getClass().getSimpleName());

        var result = handler.handle(query);

        if (result.isSuccess()) {
            LOG.info("Query {} executed successfully", query.getClass().getSimpleName());
        } else {
            LOG.info(
                "Query {} execution failed {}",
                query.getClass().getSimpleName(),
                result.getCause().getMessage()
            );
        }

        return result;
    }
}
