package com.aurionpro.model;

public enum Type {
    ACOUSTIC, ELECTRIC;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}