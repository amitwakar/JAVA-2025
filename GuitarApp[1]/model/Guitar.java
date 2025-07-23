package com.aurionpro.model;

public class Guitar {
    private final String serialNumber;
    private final double price;
    private final GuitarSpec spec;

    public Guitar(String serialNumber, double price, GuitarSpec spec) {
        this.serialNumber = serialNumber;
        this.price = price;
        this.spec = spec;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public double getPrice() {
        return price;
    }

    public GuitarSpec getSpec() {
        return spec;
    }

    @Override
    public String toString() {
        return String.format(
            "%-8s | %-15s | %-10s | %-8s | %-18s | %-15s | ₹%.2f",
            serialNumber,
            spec.getModel(),
            spec.getBuilder(),
            spec.getType(),
            spec.getBackWood(),
            spec.getTopWood(),
            price
        );
    }
}
