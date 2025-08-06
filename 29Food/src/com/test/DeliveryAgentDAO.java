package com.test;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DeliveryAgentDAO {
    public List<DeliveryAgent> getAll() {
        List<DeliveryAgent> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM delivery_agents")) {
            while (rs.next())
                list.add(new DeliveryAgent(rs.getInt("id"), rs.getString("name")));
        } catch(Exception e) { System.out.println("DB Error: " + e.getMessage()); }
        return list;
    }
}
