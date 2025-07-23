package com.aurionpro.model;


public class GuitarPrinter {
    public static void printHeader() {
        System.out.printf("%-8s | %-15s | %-10s | %-8s | %-18s | %-15s | %s\n",
                "Serial", "Model", "Builder", "Type", "Back Wood", "Top Wood", "Price");
        System.out.println("----------------------------------------------------------------------------------------------");
    }
}
