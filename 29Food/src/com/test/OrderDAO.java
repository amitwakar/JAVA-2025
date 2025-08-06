package com.test;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderDAO {
    public boolean saveOrder(Order order) {
        String orderSQL = "INSERT INTO orders (customer_name, total_amount, discount_applied, net_total, payment_mode, delivery_agent) VALUES (?,?,?,?,?,?)";
        String itemSQL = "INSERT INTO order_items (order_id, item, quantity, item_price) VALUES (?,?,?,?)";
        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);
            PreparedStatement psOrder = con.prepareStatement(orderSQL, Statement.RETURN_GENERATED_KEYS);
            psOrder.setString(1, order.getCustomerName());
            psOrder.setDouble(2, order.getTotal());
            psOrder.setDouble(3, order.getDiscount());
            psOrder.setDouble(4, order.getNetTotal());
            psOrder.setString(5, order.getPaymentMode().name());
            psOrder.setString(6, order.getDeliveryAgent().getName());
            psOrder.executeUpdate();
            ResultSet rs = psOrder.getGeneratedKeys();
            int oid = -1;
            if (rs.next())
                oid = rs.getInt(1);
            rs.close(); psOrder.close();
            if (oid == -1) throw new SQLException("Order ID not generated");
            PreparedStatement psItem = con.prepareStatement(itemSQL);
            for (OrderItem oi : order.getItems()) {
                psItem.setInt(1, oid);
                psItem.setString(2, oi.getMenuItem().getName());
                psItem.setInt(3, oi.getQuantity());
                psItem.setDouble(4, oi.getMenuItem().getPrice());
                psItem.executeUpdate();
            }
            psItem.close();
            con.commit();
            return true;
        } catch(Exception e) {
            try { if (con != null) con.rollback(); } catch(Exception ee){}
            System.out.println("DB Error: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch(Exception e){}
        }
        return false;
    }
}
