package com.university.dao;

import com.university.util.Db;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.List;

public class AuthDAO {
    public static String sha256(String s) {
        try {
            byte[] d = MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : d) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    /** @return role (ADMIN/STAFF) or null if credentials are wrong */
    public String login(String user, String pass) throws SQLException {
        List<Object[]> r = Db.query("SELECT role FROM users WHERE username=? AND password_hash=?", user, sha256(pass));
        return r.isEmpty() ? null : String.valueOf(r.get(0)[0]);
    }
}
