package models.user;

import java.util.Map;

public abstract class UserDAO {
    public abstract User getUser(String usr, String psw);
    public abstract User getUserByUsername(String usr);
    public abstract void addUser(String username, User user);

    public abstract User fetchUserFromPersistence(String username, String type, Map<String, User> userCache);
}
