package models.user;

public abstract class UserDAO {
    public abstract User getUser(String usr, String psw);
    public abstract User getUserByUsername(String usr);
    public abstract void addUser(String username, User user);
    /*public abstract void addUser(String fName, String lName, String usr, String psw);*/
}
