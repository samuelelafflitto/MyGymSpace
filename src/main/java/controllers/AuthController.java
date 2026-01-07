package controllers;

import beans.LoginBean;
import exceptions.DataLoadException;
import exceptions.UserSearchFailedException;
import models.dao.factory.FactoryDAO;
import models.user.Athlete;
import models.user.User;
import models.user.UserDAO;
import utils.session.SessionManager;

public class AuthController {
    public AuthController() {
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

    public boolean registerUser(LoginBean loginBean) {
        UserDAO userDAO = FactoryDAO.getInstance().createUserDAO();
        String firstName = loginBean.getFirstName();
        String lastName = loginBean.getLastName();
        String username = loginBean.getUsername();
        String password = loginBean.getPassword();
        String type = "ATH";

        Athlete user = new Athlete(firstName, lastName, username, password, type);
        try {
            userDAO.addUser(username, user);
        } catch (DataLoadException e) {
            System.out.println(e.getMessage());
        }
        SessionManager.getInstance().setLoggedUser(user);
        return true;
    }
}
