package testing;

import beans.LoginBean;
import beans.SignupBean;
import controllers.AuthController;
import exceptions.DataLoadException;
import exceptions.UserSearchFailedException;
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

class TestLoginAndRelated {
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
    @DisplayName("T04 - Login Test")
    void testAuthUser() {
        SignupBean signupBean = new SignupBean();
        signupBean.setFirstName("testFirstName");
        signupBean.setLastName("testLastName");
        signupBean.setUsername(TESTUSERNAME);
        signupBean.setPassword("testPassword");

        authController.registerUser(signupBean);

        try {
            resetSessionManager();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertNull(SessionManager.getInstance().getLoggedUser(), "The session must be empty before attempting login");

        LoginBean loginBean = new LoginBean();
        loginBean.setUsername(TESTUSERNAME);
        loginBean.setPassword("testPassword");

        boolean result = authController.authUser(loginBean);

        assertTrue(result, "Login should be successful");

        User loggedUser = SessionManager.getInstance().getLoggedUser();
        assertNotNull(loggedUser, "The user should be logged in after logging in");
        assertEquals(TESTUSERNAME, loggedUser.getUsername());
    }

    @Test
    @DisplayName("T05 - Login test with wrong password")
    void testAuthUser_wrongPassword() {
        SignupBean signupBean = new SignupBean();
        signupBean.setFirstName("testFirstName");
        signupBean.setLastName("testLastName");
        signupBean.setUsername(TESTUSERNAME);
        signupBean.setPassword("testPassword");

        authController.registerUser(signupBean);

        try {
            resetSessionManager();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        LoginBean loginBean = new LoginBean();
        loginBean.setUsername(TESTUSERNAME);
        loginBean.setPassword("wrongPassword");

        assertThrows(UserSearchFailedException.class, () -> authController.authUser(loginBean));
        assertNull(SessionManager.getInstance().getLoggedUser(), "No user should be logged in");
    }

    @Test
    @DisplayName("T06 - Login Test with unregistered user")
    void testAuthUser_userNotFound() {
        LoginBean loginBean = new LoginBean();
        loginBean.setUsername(TESTUSERNAME);
        loginBean.setPassword("testPassword");

        assertThrows(UserSearchFailedException.class, () -> authController.authUser(loginBean));
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
