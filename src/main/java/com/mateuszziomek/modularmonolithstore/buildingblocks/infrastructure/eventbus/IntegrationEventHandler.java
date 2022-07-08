package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.eventbus;

public interface IntegrationEventHandler <T extends IntegrationEvent> extends IntegrationMessageHandler<T> { }
