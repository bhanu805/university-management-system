package com.university.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Creates MySQL connections. Config: db.properties, overridable by env DB_URL / DB_USER / DB_PASSWORD. */
public class DBConnection {
    private static final Properties P = new Properties();

    static {
        try (InputStream in = DBConnection.class.getResourceAsStream("/db.properties")) {
            if (in != null) P.load(in);
        } catch (Exception ignored) { }
    }

    private static String prop(String env, String key) {
        String v = System.getenv(env);
        return v != null ? v : P.getProperty(key);
    }

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(prop("DB_URL", "db.url"),
                prop("DB_USER", "db.user"), prop("DB_PASSWORD", "db.password"));
    }
}
