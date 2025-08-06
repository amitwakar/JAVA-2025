package com.test;


import java.util.List;
import java.util.Scanner;

public class AdminService {
    private MenuDAO menuDAO = new MenuDAO();

    public void showMenu(Scanner sc) {
        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. View Menu");
            System.out.println("2. Add Menu Item");
            System.out.println("3. Remove Menu Item");
            System.out.println("4. Back");
            int ch = InputUtil.readInt(sc, "Choice: ", 1, 4);
            switch (ch) {
                case 1: displayMenu(); break;
                case 2: addMenuItem(sc); break;
                case 3: removeMenuItem(sc); break;
                case 4: return;
            }
        }
    }
    public void displayMenu() {
        List<MenuItem> menu = menuDAO.getAll();
        System.out.println("\n----- MENU -----");
        System.out.printf("%-5s %-20s %-10s\n", "ID", "Item", "Price");
        for (MenuItem mi : menu)
            System.out.printf("%-5d %-20s ₹%-10.2f\n", mi.getId(), mi.getName(), mi.getPrice());
    }
    public void addMenuItem(Scanner sc) {
        String name = InputUtil.readNonEmpty(sc, "Enter new item name: ");
        double price = InputUtil.readPositiveDouble(sc, "Enter price: ");
        if (menuDAO.add(new MenuItem(0, name, price)))
            System.out.println("Item added.");
        else System.out.println("Unable to add item.");
    }
    public void removeMenuItem(Scanner sc) {
        displayMenu();
        int id = InputUtil.readInt(sc, "Enter item ID to remove: ", 1, Integer.MAX_VALUE);
        if (menuDAO.remove(id))
            System.out.println("Item removed.");
        else System.out.println("Unable to remove item.");
    }
}

