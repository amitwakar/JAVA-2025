package com.test;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

public class CustomerService {
    private MenuDAO menuDAO = new MenuDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private DeliveryAgentDAO agentDAO = new DeliveryAgentDAO();

    public void placeOrder(Scanner sc) {
        List<MenuItem> menu = menuDAO.getAll();
        List<OrderItem> cart = new ArrayList<>();
        while (true) {
            displayMenu(menu);
            int itemId = InputUtil.readInt(sc,"Select item ID (0 to checkout): ",0,Integer.MAX_VALUE);
            if (itemId == 0) break;
            Optional<MenuItem> opt = menu.stream().filter(m->m.getId()==itemId).findFirst();
            if (!opt.isPresent()) {
                System.out.println("Item ID not found.");
                continue;
            }
            int qty = InputUtil.readInt(sc,"Enter quantity: ",1,20);
            cart.add(new OrderItem(opt.get(), qty));
            System.out.println(opt.get().getName() + " x" + qty + " added.");
        }
        if (cart.isEmpty()) {
            System.out.println("No items selected. Returning to menu.");
            return;
        }
        String customerName = InputUtil.readNonEmpty(sc,"Enter your name: ");

        double total = cart.stream().mapToDouble(oi->oi.getMenuItem().getPrice()*oi.getQuantity()).sum();
        double discount = (total > Constants.DISCOUNT_THRESHOLD) ? Constants.DISCOUNT_AMOUNT : 0.0;
        double netTotal = total - discount;

        PaymentMode paymentMode = null;
        while (paymentMode == null) {
            String pm = InputUtil.readNonEmpty(sc,"Payment mode (CASH/UPI): ").toUpperCase();
            try { paymentMode = PaymentMode.valueOf(pm); }
            catch(Exception e) { System.out.println("Please enter CASH or UPI."); }
        }

        List<DeliveryAgent> agents = agentDAO.getAll();
        DeliveryAgent assigned = agents.get(new Random().nextInt(agents.size()));

        Order order = new Order(customerName, cart, total, discount, netTotal, paymentMode, assigned);

        if (orderDAO.saveOrder(order)) printInvoice(order);
        else System.out.println("Order placement failed. Please try again.");
    }

    private void displayMenu(List<MenuItem> menu) {
        System.out.println("\n---- MENU ----");
        System.out.printf("%-5s %-20s %-10s\n", "ID", "Item", "Price");
        for (MenuItem m: menu)
            System.out.printf("%-5d %-20s ₹%-10.2f\n", m.getId(), m.getName(), m.getPrice());
    }

    private void printInvoice(Order order) {
        System.out.println("\n======= INVOICE =======");
        System.out.println("Customer: " + order.getCustomerName());
        System.out.println("----------------------");
        for (OrderItem oi : order.getItems()) {
            System.out.printf("%-20s x%2d ₹%-8.2f\n", oi.getMenuItem().getName(), oi.getQuantity(), oi.getMenuItem().getPrice());
        }
        System.out.println("----------------------");
        System.out.printf("Total:     ₹%.2f\n", order.getTotal());
        System.out.printf("Discount: -₹%.2f\n", order.getDiscount());
        System.out.printf("Net Due:   ₹%.2f\n", order.getNetTotal());
        System.out.println("Payment: " + order.getPaymentMode());
        System.out.println("Delivery Agent: " + order.getDeliveryAgent().getName());
        System.out.println("=======================");
        System.out.println("Thank you for ordering!");
    }
}
