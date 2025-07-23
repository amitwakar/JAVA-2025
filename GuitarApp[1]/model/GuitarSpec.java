package com.aurionpro.model;

public class GuitarSpec {
    private final Builder builder;
    private final String model;
    private final Type type;
    private final Wood backWood;
    private final Wood topWood;

    public GuitarSpec(Builder builder, String model, Type type, Wood backWood, Wood topWood) {
        this.builder = builder;
        this.model = model.toLowerCase();
        this.type = type;
        this.backWood = backWood;
        this.topWood = topWood;
    }

    public Builder getBuilder() {
        return builder;
    }

    public String getModel() {
        return model;
    }

    public Type getType() {
        return type;
    }

    public Wood getBackWood() {
        return backWood;
    }

    public Wood getTopWood() {
        return topWood;
    }

    public boolean matches(GuitarSpec other) {
        return builder == other.builder &&
                model.equalsIgnoreCase(other.model) &&
                type == other.type &&
                backWood == other.backWood &&
                topWood == other.topWood;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s Guitar [%s Back | %s Top]",
                builder, model, type, backWood, topWood);
    }
}
