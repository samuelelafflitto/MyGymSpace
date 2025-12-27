package models.user;

public abstract class UserDAO {
    public abstract User getUser(String usr, String psw);
    public abstract User getUserByUsername(String usr);
    public abstract void addUser(String usr, String psw);
}
