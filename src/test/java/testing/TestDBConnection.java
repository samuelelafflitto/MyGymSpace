package testing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class TestDBConnection {

    @Test
    @DisplayName("T01 - Singleton Test: getInstance mus always return the same object")
    void testGetInstance() {
        DBConnection firstInstance = DBConnection.getInstance();
        DBConnection secondInstance = DBConnection.getInstance();

        assertNotNull(firstInstance, "The instance should not be null");
        assertSame(firstInstance, secondInstance, "The two instances should be identical");
    }

    @Test
    @DisplayName("T02 - Connection Test: getConnection must return a valid connection")
    void testGetConnection() throws SQLException {
        DBConnection dbConnection = DBConnection.getInstance();
        Connection connection = dbConnection.getConnection();

        assertNotNull(connection, "The connection should not be null");
        assertFalse(connection.isClosed(), "The connection should be open");
    }

    @Test
    @DisplayName("T03 - Reconnection Test: if closed, it should create a new one")
    void testReconnection() throws SQLException {
        DBConnection dbConnection = DBConnection.getInstance();

        Connection firstConnection = dbConnection.getConnection();
        assertFalse(firstConnection.isClosed());

        firstConnection.close();
        assertTrue(firstConnection.isClosed(), "The first connection should have been closed");

        Connection secondConnection = dbConnection.getConnection();

        assertNotNull(secondConnection);
        assertFalse(secondConnection.isClosed(), "The second connection should still be open");
        assertNotSame(firstConnection, secondConnection, "The new connection should be different from the previous one");
    }
}
