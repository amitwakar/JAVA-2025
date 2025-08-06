package com.test;

import java.util.Scanner;

public class InputUtil {
    public static int readInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int num = Integer.parseInt(sc.nextLine());
                if (num >= min && num <= max) return num;
            } catch(Exception e){}
            System.out.println("Please enter a valid number between " + min + " and " + max);
        }
    }
    public static double readPositiveDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double d = Double.parseDouble(sc.nextLine());
                if (d > 0) return d;
            } catch(Exception e){}
            System.out.println("Please enter a positive number.");
        }
    }
    public static String readNonEmpty(Scanner sc, String prompt) {
        String str;
        do {
            System.out.print(prompt);
            str = sc.nextLine().trim();
        } while (str.isEmpty());
        return str;
    }
}
