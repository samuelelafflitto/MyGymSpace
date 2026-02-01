package testing;

import beans.SignupBean;
import controllers.AuthController;
import exceptions.DataLoadException;
import exceptions.ExistingUserException;
import models.dao.factory.DBDAO;
import models.dao.factory.FactoryDAO;
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

    @BeforeEach
    void setUp() throws Exception {
        resetSessionManager();
        forceFactoryToUseDBDAO();

        authController = new AuthController();
        deleteTestUserFromDB();
    }

    @AfterEach
    void tearDown() {
        deleteTestUserFromDB();

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
        User dbUser = null;
        try {
            dbUser = userDAO.getUserByUsername(TESTUSERNAME);
        } catch (DataLoadException _) {
            fail("Errore DB durante la verifica");
        }

        assertNotNull(dbUser, "L'utente dovrebbe essere stato salvato nel Database");
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
    private void deleteTestUserFromDB() {
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

    private void forceFactoryToUseDBDAO() throws Exception {
        Field instanceField = FactoryDAO.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        FactoryDAO dbInstance = new DBDAO();

        instanceField.set(null, dbInstance);
    }
}
