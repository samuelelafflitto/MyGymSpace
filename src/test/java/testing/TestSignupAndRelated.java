package testing;

import beans.SignupBean;
import controllers.AuthController;
import exceptions.DataLoadException;
import exceptions.ExistingUserException;
import models.dao.factory.DBDAO;
import models.dao.factory.FactoryDAO;
import models.dao.factory.FsysDAO;
import models.dao.factory.MemDAO;
import models.user.User;
import models.user.UserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.session.SessionManager;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestSignupAndRelated {
    private AuthController authController;
    private static final String TESTUSERNAME = "test_user";
    // Impostare come: demo, db, fsys per testare
    private static final String PERSISTENCE = "db";

    @BeforeEach
    void setUp() throws Exception {
        resetSessionManager();
        forceFactoryMode();

        authController = new AuthController();
        deleteTestUser();
    }

    @AfterEach
    void tearDown() {
        deleteTestUser();

        try {
            resetSessionManager();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @Test
    @DisplayName("T07 - Test Registrazione")
    void testRegisterUser() {
        SignupBean bean = new SignupBean();
        bean.setFirstName("testFirstName");
        bean.setLastName("testLastName");
        bean.setUsername(TESTUSERNAME);
        bean.setPassword("testPassword");

        boolean result = authController.registerUser(bean);

        assertTrue(result, "La registrazione dovrebbe essere stata effettuata");

        User loggedUser = SessionManager.getInstance().getLoggedUser();
        assertNotNull(loggedUser, "L'utente dovrebbe essere loggato dopo la registrazione");
        assertEquals(TESTUSERNAME, loggedUser.getUsername());

        UserDAO userDAO = FactoryDAO.getInstance().createUserDAO();
        User savedUser = null;
        try {
            savedUser = userDAO.getUserByUsername(TESTUSERNAME);
        } catch (DataLoadException _) {
            fail("Errore durante la verifica nel sistema di persistenza");
        }

        assertNotNull(savedUser, "L'utente dovrebbe essere stato salvato in persistenza");
    }

    @Test
    @DisplayName("T08 - Test Registrazione con username già usato")
    void testRegisterUser_secondTime() {
        SignupBean bean = new SignupBean();
        bean.setFirstName("testFirstName");
        bean.setLastName("testLastName");
        bean.setUsername(TESTUSERNAME);
        bean.setPassword("testPassword");

        // Prima registrazione
        authController.registerUser(bean);

        // Logout
        try {
            resetSessionManager();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertThrows(ExistingUserException.class, () -> authController.registerUser(bean));
    }


    // HELPER
    private void deleteTestUser() {
        UserDAO userDAO = FactoryDAO.getInstance().createUserDAO();
        try {
            userDAO.deleteUser(TESTUSERNAME);
        } catch (DataLoadException e) {
            System.err.println("ATTENZIONE: pulizia utente di test fallita. " + e.getMessage());
        }
    }

    private void resetSessionManager() throws NoSuchFieldException, IllegalAccessException {
        Field instance = SessionManager.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    private void forceFactoryMode() throws Exception {
        Field instanceField = FactoryDAO.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        FactoryDAO selectedFactory = switch (PERSISTENCE.toUpperCase()) {
            case "DEMO" -> new MemDAO();
            case "DB" -> new DBDAO();
            case "FSYS" -> new FsysDAO();
            default -> {
                System.out.println("Errore nel recupero del tipo di persistenza, avvio in modalità DEMO");
                yield new MemDAO();
            }
        };

        instanceField.set(null, selectedFactory);
    }
}
