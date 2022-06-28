package com.mateuszziomek.modularmonolithstore.buildingblocks.domain;

public interface BusinessRule {
    boolean isBroken();
    String message();
}
