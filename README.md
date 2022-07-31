# Modular Monolith Store

Modular monolith store is a project created for better understand of modular monolith architecture concepts. It has almost 0 domain logic, the main goal was to create and understand all of the building blocks. Project is highly inspired by [this repository](https://github.com/kgrzybek/modular-monolith-with-ddd).

## Persistance

Project uses in memory repository, but it should not be a problem to replace it with a real database

## Messaging

Project uses in memory message broker, but it should not be a problem to replace it with a real messaging system

## Thread safety

Because project works fully in memory, I had to make sure that everything is thread safe. It was my first time working with java concurrent API, so it may not  be ideal in terms of concurrenct
