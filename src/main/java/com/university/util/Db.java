package com.university.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Small JDBC helper: always uses PreparedStatement (prevents SQL injection). */
public class Db {
    private static void bind(PreparedStatement ps, Object[] p) throws SQLException {
        for (int i = 0; i < p.length; i++) ps.setObject(i + 1, p[i]);
    }

    public static List<Object[]> query(String sql, Object... p) throws SQLException {
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, p);
            try (ResultSet rs = ps.executeQuery()) {
                int n = rs.getMetaData().getColumnCount();
                List<Object[]> out = new ArrayList<>();
                while (rs.next()) {
                    Object[] row = new Object[n];
                    for (int i = 0; i < n; i++) row[i] = rs.getObject(i + 1);
                    out.add(row);
                }
                return out;
            }
        }
    }

    public static int update(String sql, Object... p) throws SQLException {
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, p);
            return ps.executeUpdate();
        }
    }
}
