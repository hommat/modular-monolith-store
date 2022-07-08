package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message;

public interface IntegrationEventHandler <T extends IntegrationEvent> extends IntegrationMessageHandler<T> { }
