package database;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseTest {

    @Test
    public void DatabaseInMemory() {
        Connection connection;

        try {
           connection = DriverManager.getConnection("jdbc:sqlite::memory:");

           Assertions.assertNotNull(connection);

           Statement statement = connection.createStatement();
           statement.setQueryTimeout(10);

           statement.executeUpdate("DROP TABLE IF EXISTS test_table");
           statement.executeUpdate("CREATE TABLE test_table (id integer, name string)");
           statement.executeUpdate("INSERT INTO test_table VALUES(1, 'Alice')");
           statement.executeUpdate("INSERT INTO test_table VALUES(2, 'Bob')");

           ResultSet rs = statement.executeQuery("SELECT * FROM test_table ORDER BY id");

           rs.next();
           Assertions.assertEquals("Alice", rs.getString("name"));

           rs.next();
           Assertions.assertEquals("Bob", rs.getString("name"));

        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}