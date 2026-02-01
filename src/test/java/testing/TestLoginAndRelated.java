package testing;

import beans.LoginBean;
import beans.SignupBean;
import controllers.AuthController;
import exceptions.DataLoadException;
import exceptions.UserSearchFailedException;
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

class TestLoginAndRelated {
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
    @DisplayName("Test Login")
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

        assertNull(SessionManager.getInstance().getLoggedUser(), "La sessione deve essere vuota prima di tentare il login");

        LoginBean loginBean = new LoginBean();
        loginBean.setUsername(TESTUSERNAME);
        loginBean.setPassword("testPassword");

        boolean result = authController.authUser(loginBean);

        assertTrue(result, "Il login dovrebbe avere successo");

        User loggedUser = SessionManager.getInstance().getLoggedUser();
        assertNotNull(loggedUser, "L'utente dovrebbe essere loggato dopo il login");
        assertEquals(TESTUSERNAME, loggedUser.getUsername());
    }

    @Test
    @DisplayName("Test Login con password sbagliata")
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
        assertNull(SessionManager.getInstance().getLoggedUser(), "Nessun utente dovrebbe essere loggato");
    }

    @Test
    @DisplayName("Test Login con utente non registrato")
    void testAuthUser_userNotFound() {
        LoginBean loginBean = new LoginBean();
        loginBean.setUsername(TESTUSERNAME);
        loginBean.setPassword("testPassword");

        assertThrows(UserSearchFailedException.class, () -> authController.authUser(loginBean));
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
