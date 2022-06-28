package com.mateuszziomek.buildingblocks.domain;

public interface BusinessRule {
    boolean isBroken();
    String message();
}
