package com.aurionpro.service;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.aurionpro.model.Builder;
import com.aurionpro.model.Guitar;
import com.aurionpro.model.GuitarPrinter;
import com.aurionpro.model.InputValidator;
import com.aurionpro.model.Type;
import com.aurionpro.model.Wood;

public class GuitarSearchService {
    private final Inventory inventory;
    private final Scanner sc;

    public GuitarSearchService(Inventory inventory, Scanner sc) {
        this.inventory = inventory;
        this.sc = sc;
    }

    public void startSearch() {
        System.out.println("\n SEARCH GUITAR ");
        List<Guitar> results = inventory.getAllGuitars();

        if (results.isEmpty()) {
            System.out.println(" No guitars available.");
            return;
        }

        displayGuitars(" All Guitars:", results);

        while (true) {
            System.out.println("\nDo you want to filter?");
            System.out.println("1. Yes\n2. No");
            int choice = InputValidator.getValidatedInt(sc, "Enter choice: ", 1, 2);
            if (choice == 2) break;

            results = applyFilter(results);
            if (results.isEmpty()) {
                System.out.println(" No guitars match this filter.");
                return;
            }

            displayGuitars(" Filtered Guitars:", results);
        }
    }

    private List<Guitar> applyFilter(List<Guitar> list) {
        System.out.println("Filter by:");
        System.out.println("1. Model\n2. Type\n3. Builder\n4. Back Wood\n5. Top Wood");

        int filterChoice = InputValidator.getValidatedInt(sc, "Select filter: ", 1, 5);

        return switch (filterChoice) {
            case 1 -> {
                String modelKeyword = InputValidator.getValidatedString(sc, "Enter model keyword: ").toLowerCase();
                yield list.stream()
                        .filter(g -> g.getSpec().getModel().toLowerCase().contains(modelKeyword))
                        .collect(Collectors.toList());
            }
            case 2 -> {
                Type type = InputValidator.getValidatedEnum(sc, Type.class, "Select Type");
                yield list.stream()
                        .filter(g -> g.getSpec().getType() == type)
                        .collect(Collectors.toList());
            }
            case 3 -> {
                Builder builder = InputValidator.getValidatedEnum(sc, Builder.class, "Select Builder");
                yield list.stream()
                        .filter(g -> g.getSpec().getBuilder() == builder)
                        .collect(Collectors.toList());
            }
            case 4 -> {
                Wood backWood = InputValidator.getValidatedEnum(sc, Wood.class, "Select Back Wood");
                yield list.stream()
                        .filter(g -> g.getSpec().getBackWood() == backWood)
                        .collect(Collectors.toList());
            }
            case 5 -> {
                Wood topWood = InputValidator.getValidatedEnum(sc, Wood.class, "Select Top Wood");
                yield list.stream()
                        .filter(g -> g.getSpec().getTopWood() == topWood)
                        .collect(Collectors.toList());
            }
            default -> list;
        };
    }

    private void displayGuitars(String message, List<Guitar> guitars) {
        System.out.println("\n" + message);
        GuitarPrinter.printHeader();
        for (int i = 0; i < guitars.size(); i++) {
            System.out.println((i + 1) + ". " + guitars.get(i));
        }
    }
}
