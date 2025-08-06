package com.test;


import java.util.List;

public class Order {
    private int id;
    private String customerName;
    private List<OrderItem> items;
    private double total;
    private double discount;
    private double netTotal;
    private PaymentMode paymentMode;
    private DeliveryAgent deliveryAgent;

    public Order(String customerName, List<OrderItem> items, double total, double discount, double netTotal, PaymentMode paymentMode, DeliveryAgent deliveryAgent) {
        this.customerName = customerName;
        this.items = items;
        this.total = total;
        this.discount = discount;
        this.netTotal = netTotal;
        this.paymentMode = paymentMode;
        this.deliveryAgent = deliveryAgent;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCustomerName() { return customerName; }
    public List<OrderItem> getItems() { return items; }
    public double getTotal() { return total; }
    public double getDiscount() { return discount; }
    public double getNetTotal() { return netTotal; }
    public PaymentMode getPaymentMode() { return paymentMode; }
    public DeliveryAgent getDeliveryAgent() { return deliveryAgent; }
}
