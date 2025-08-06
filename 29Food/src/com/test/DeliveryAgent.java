package com.test;

public class DeliveryAgent {
    private int id;
    private String name;

    public DeliveryAgent(int id, String name) {
        this.id = id; this.name = name;
    }
    public int getId() { return id; }
    public String getName() { return name; }
}