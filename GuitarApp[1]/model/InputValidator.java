package com.aurionpro.model;

import java.util.Scanner;

public class InputValidator {
    public static int getValidatedInt(Scanner sc, String prompt, int min, int max) {
        int value;
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine();
            try {
                value = Integer.parseInt(input);
                if (value >= min && value <= max) break;
                else System.out.println("Enter a number between " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number!");
            }
        }
        return value;
    }

    public static double getValidatedDouble(Scanner sc, String prompt) {
        double value;
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine();
            try {
                value = Double.parseDouble(input);
                if (value > 0) break;
                else System.out.println("Enter a positive amount.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number!");
            }
        }
        return value;
    }

    public static String getValidatedString(Scanner sc, String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = sc.nextLine().trim();
            if (!input.isEmpty()) break;
            else System.out.println("This field can't be empty.");
        }
        return input;
    }

    public static <T extends Enum<T>> T getValidatedEnum(Scanner sc, Class<T> enumClass, String label) {
        T[] constants = enumClass.getEnumConstants();
        System.out.println(label + ":");
        for (int i = 0; i < constants.length; i++) {
            System.out.println((i + 1) + ". " + constants[i]);
        }

        int choice = getValidatedInt(sc, "Select option: ", 1, constants.length);
        return constants[choice - 1];
    }
}