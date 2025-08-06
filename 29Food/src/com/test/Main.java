package com.test;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AdminService adminService = new AdminService();
        CustomerService customerService = new CustomerService();
        System.out.println("========== WELCOME TO MINI FOOD ORDERING ==========");
        while (true) {
            System.out.println(" 1. Admin Menu\n 2. Customer Order\n 3. Exit");
            int choice = InputUtil.readInt(sc,"Select option: ",1,3);
            switch(choice) {
                case 1: adminService.showMenu(sc); break;
                case 2: customerService.placeOrder(sc); break;
                case 3: System.out.println("Goodbye!"); sc.close(); System.exit(0);
            }
        }
    }
}

