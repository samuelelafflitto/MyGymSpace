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
    // To be set as: demo, db, fsys
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
    @DisplayName("T07 - Signup Test")
    void testRegisterUser() {
        SignupBean bean = new SignupBean();
        bean.setFirstName("testFirstName");
        bean.setLastName("testLastName");
        bean.setUsername(TESTUSERNAME);
        bean.setPassword("testPassword");

        boolean result = authController.registerUser(bean);

        assertTrue(result, "Registration should have been made");

        User loggedUser = SessionManager.getInstance().getLoggedUser();
        assertNotNull(loggedUser, "The user should be logged in after registration");
        assertEquals(TESTUSERNAME, loggedUser.getUsername());

        UserDAO userDAO = FactoryDAO.getInstance().createUserDAO();
        User savedUser = null;
        try {
            savedUser = userDAO.getUserByUsername(TESTUSERNAME);
        } catch (DataLoadException _) {
            fail("Error while checking in persistence");
        }

        assertNotNull(savedUser, "The user should have been saved persistently");
    }

    @Test
    @DisplayName("T08 - Signup Test with already used username")
    void testRegisterUser_secondTime() {
        SignupBean bean = new SignupBean();
        bean.setFirstName("testFirstName");
        bean.setLastName("testLastName");
        bean.setUsername(TESTUSERNAME);
        bean.setPassword("testPassword");

        // First signup
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
            System.err.println("WARNING: Test user cleanup failed " + e.getMessage());
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
                System.out.println("Persistence mode retrieving error, starting test in Demo mode...");
                yield new MemDAO();
            }
        };

        instanceField.set(null, selectedFactory);
    }
}
