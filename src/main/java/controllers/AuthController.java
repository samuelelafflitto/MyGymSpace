package controllers;

import beans.LoginBean;
import beans.SignupBean;
import exceptions.DataLoadException;
import exceptions.ExistingUserException;
import exceptions.UserSearchFailedException;
import models.dao.factory.FactoryDAO;
import models.user.Athlete;
import models.user.User;
import models.user.UserDAO;
import utils.session.SessionManager;

public class AuthController {
    private static final String ATHLETE_TYPE = "ATH";

    public AuthController() {// Il costruttore non ha bisogno di parametri
    }

    public boolean authUser(LoginBean loginBean) {
        UserDAO userDAO = FactoryDAO.getInstance().createUserDAO();
        String username = loginBean.getUsername();
        String password = loginBean.getPassword();
        User user = null;

        try {
            user = userDAO.getUser(username, password);
        } catch (DataLoadException e) {
            System.out.println(e.getMessage());
        }

        if (user == null) {
            throw new UserSearchFailedException();
        } else {
            SessionManager.getInstance().setLoggedUser(user);
            return true;
        }
    }

    public boolean registerUser(SignupBean signupBean) {
        UserDAO userDAO = FactoryDAO.getInstance().createUserDAO();
        String firstName = signupBean.getFirstName();
        String lastName = signupBean.getLastName();
        String username = signupBean.getUsername();
        String password = signupBean.getPassword();
        User existingUser = null;

        try {
            existingUser = userDAO.getUserByUsername(username);
        } catch (DataLoadException e) {
            System.out.println(e.getMessage());
        }

        if(existingUser != null) {
            throw new ExistingUserException();
        }
        Athlete user = new Athlete(username, password, firstName, lastName, ATHLETE_TYPE);

        try {
            userDAO.addUser(username, user);
        } catch (DataLoadException e) {
            System.out.println(e.getMessage());
            return false;
        }
        SessionManager.getInstance().setLoggedUser(user);
        return true;
    }
}
