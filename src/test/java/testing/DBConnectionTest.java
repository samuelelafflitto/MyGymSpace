package testing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectionTest {

    @Test
    @DisplayName("Test Singleton: getInstance deve restituire sempre lo stesso oggetto")
    void testGetInstance() {
        DBConnection firstInstance = DBConnection.getInstance();
        DBConnection secondInstance = DBConnection.getInstance();

        assertNotNull(firstInstance, "L'istanza non dovrebbe essere null");
        assertSame(firstInstance, secondInstance, "Le due istanze dovrebbero essere identiche");
    }

    @Test
    @DisplayName("Test Connessione: getConnection deve restituire una connessione valida")
    void testGetConnection() throws SQLException {
        DBConnection dbConnection = DBConnection.getInstance();
        Connection connection = dbConnection.getConnection();

        assertNotNull(connection, "La connessione non dovrebbe essere null");
        assertFalse(connection.isClosed(), "La connessione dovrebbe essere aperta");
    }

    @Test
    @DisplayName("Test Riconnessione: se chiusa, ne crea una nuova")
    void testReconnection() throws SQLException {
        DBConnection dbConnection = DBConnection.getInstance();

        Connection firstConnection = dbConnection.getConnection();
        assertFalse(firstConnection.isClosed());

        firstConnection.close();
        assertTrue(firstConnection.isClosed(), "La prima connessione dovrebbe essere stata chiusa");

        Connection secondConnection = dbConnection.getConnection();

        assertNotNull(secondConnection);
        assertFalse(secondConnection.isClosed(), "La seconda connessione dovrebbe essere ancora aperta");
        assertNotSame(firstConnection, secondConnection, "La nuova connessione dovrebbe essere diversa dalla precedente");
    }
}
