package org.cnsgithub;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.junit.jupiter.api.Test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MssqlJdbc1059 {

    private static final String URL = "jdbc:sqlserver://<INSERT_URL_HERE>;databaseName=test_sp";

    @Test
    public void testNamedParams() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, "executor", "password_executor")) {
            CallableStatement stmt = conn.prepareCall("{ call test(?,?) }");
            stmt.setInt("id", 42);
            stmt.setString("str", "");
            stmt.execute();
        }
    }

    //fails with 'The value is not set for the parameter number 2.'
    @Test
    public void testNamedParamsPermissionDenied() {
        SQLServerException e = assertThrows(SQLServerException.class, () -> {
            try (Connection conn = DriverManager.getConnection(URL, "reader", "password_reader")) {
                CallableStatement stmt = conn.prepareCall("{ call test(?,?) }");
                stmt.setInt("id", 42);
                stmt.setString("str", "");
                stmt.execute();
            }
        });
        assertEquals("The EXECUTE permission was denied on the object 'test'", e.getMessage());
    }

}
