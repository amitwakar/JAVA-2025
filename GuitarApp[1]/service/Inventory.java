package com.aurionpro.service;



import java.util.ArrayList;
import java.util.List;

import com.aurionpro.model.Builder;
import com.aurionpro.model.Guitar;
import com.aurionpro.model.GuitarSpec;
import com.aurionpro.model.Type;
import com.aurionpro.model.Wood;

public class Inventory {
    private final List<Guitar> guitars = new ArrayList<>();

    public Inventory() {
        loadSampleData(); // Load 10 guitars when Inventory is created
    }

    public void addGuitar(Guitar guitar) {
        guitars.add(guitar);
    }

    public List<Guitar> search(GuitarSpec spec) {
        List<Guitar> matches = new ArrayList<>();
        for (Guitar g : guitars) {
            if (g.getSpec().matches(spec)) {
                matches.add(g);
            }
        }
        return matches;
    }

    public List<Guitar> getAllGuitars() {
        return guitars;
    }

    private void loadSampleData() {
        guitars.add(new Guitar("F001", 1499.95, new GuitarSpec(Builder.FENDER, "Stratocastor", Type.ELECTRIC, Wood.ALDER, Wood.ALDER)));
        guitars.add(new Guitar("G001", 1599.95, new GuitarSpec(Builder.GIBSON, "Les Paul", Type.ELECTRIC, Wood.MAHOGANY, Wood.MAPLE)));
        guitars.add(new Guitar("M001", 1399.95, new GuitarSpec(Builder.MARTIN, "SJ", Type.ACOUSTIC, Wood.MAHOGANY, Wood.CEDAR)));
        guitars.add(new Guitar("C001", 1299.95, new GuitarSpec(Builder.COLLINGS, "SJ", Type.ACOUSTIC, Wood.CEDAR, Wood.MAHOGANY)));
        guitars.add(new Guitar("O001", 1499.95, new GuitarSpec(Builder.OLSON, "SJ", Type.ACOUSTIC, Wood.BRAZILIAN_ROSEWOOD, Wood.CEDAR)));
        guitars.add(new Guitar("R001", 1599.95, new GuitarSpec(Builder.RYAN, "SJ", Type.ELECTRIC, Wood.MAPLE, Wood.MAPLE)));
        guitars.add(new Guitar("P001", 1899.95, new GuitarSpec(Builder.PRS, "Custom 24", Type.ELECTRIC, Wood.MAPLE, Wood.MAPLE)));
        guitars.add(new Guitar("F002", 1199.95, new GuitarSpec(Builder.FENDER, "Telecaster", Type.ELECTRIC, Wood.ALDER, Wood.ALDER)));
        guitars.add(new Guitar("G002", 999.95, new GuitarSpec(Builder.GIBSON, "SG", Type.ELECTRIC, Wood.MAHOGANY, Wood.MAHOGANY)));
        guitars.add(new Guitar("M002", 1099.95, new GuitarSpec(Builder.MARTIN, "OOO-28", Type.ACOUSTIC, Wood.MAHOGANY, Wood.CEDAR)));
    }
}
