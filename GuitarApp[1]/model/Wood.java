package com.aurionpro.model;

public enum Wood {
    MAHOGANY, MAPLE, ALDER, COCOBOLO, CEDAR, BRAZILIAN_ROSEWOOD;

    @Override
    public String toString() {
        return name().replace("_", " ").charAt(0) + name().replace("_", " ").substring(1).toLowerCase();
    }
}