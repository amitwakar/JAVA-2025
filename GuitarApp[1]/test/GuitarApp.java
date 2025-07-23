package com.aurionpro.test;


import java.util.Scanner;

import com.aurionpro.model.Builder;
import com.aurionpro.model.Guitar;
import com.aurionpro.model.GuitarPrinter;
import com.aurionpro.model.GuitarSpec;
import com.aurionpro.model.InputValidator;
import com.aurionpro.model.Type;
import com.aurionpro.model.Wood;
import com.aurionpro.service.GuitarSearchService;
import com.aurionpro.service.Inventory;

public class  GuitarApp {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        Scanner sc = new Scanner(System.in);
        GuitarSearchService searchService = new GuitarSearchService(inventory, sc);

        while (true) {
            System.out.println("\n GUITAR INVENTORY MENU ");
            System.out.println("1. Add Guitar");
            System.out.println("2. Search Guitar");
            System.out.println("3. View All Guitars");
            System.out.println("4. Exit");

            int choice = InputValidator.getValidatedInt(sc, "Enter choice: ", 1, 4);

            switch (choice) {
                case 1 -> addGuitar(sc, inventory);
                case 2 -> searchService.startSearch();
                case 3 -> {
                    System.out.println("\n GUITAR INVENTORY ");
                    GuitarPrinter.printHeader();
                    inventory.getAllGuitars().forEach(System.out::println);
                }
                case 4 -> {
                    System.out.println(" Exiting. Thank you!");
                    return;
                }
            }
        }
    }

    private static void addGuitar(Scanner sc, Inventory inventory) {
        System.out.println("\n ADD GUITAR ");
        System.out.print("Enter Serial Number: ");
        String serial = sc.nextLine();

        double price = InputValidator.getValidatedDouble(sc, "Enter Price: ");
        Builder builder = InputValidator.getValidatedEnum(sc, Builder.class, "Select Builder");
        String model = InputValidator.getValidatedString(sc, "Enter Model: ");
        Type type = InputValidator.getValidatedEnum(sc, Type.class, "Select Type");
        Wood backWood = InputValidator.getValidatedEnum(sc, Wood.class, "Select Back Wood");
        Wood topWood = InputValidator.getValidatedEnum(sc, Wood.class, "Select Top Wood");

        GuitarSpec spec = new GuitarSpec(builder, model, type, backWood, topWood);
        inventory.addGuitar(new Guitar(serial, price, spec));
        System.out.println("Guitar added successfully.");
    }
}
