package com.test;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {
    public List<MenuItem> getAll() {
        List<MenuItem> menu = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM menu")) {
            while (rs.next())
                menu.add(new MenuItem(rs.getInt("id"), rs.getString("item"), rs.getDouble("price")));
        } catch(Exception e) { System.out.println("DB Error: " + e.getMessage()); }
        return menu;
    }

    public boolean add(MenuItem item) {
        String sql = "INSERT INTO menu (item, price) VALUES (?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, item.getName());
            ps.setDouble(2, item.getPrice());
            ps.executeUpdate();
            return true;
        } catch(Exception e) { System.out.println("DB Error: " + e.getMessage()); }
        return false;
    }

    public boolean remove(int id) {
        String sql = "DELETE FROM menu WHERE id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch(Exception e) { System.out.println("DB Error: " + e.getMessage()); }
        return false;
    }
}
