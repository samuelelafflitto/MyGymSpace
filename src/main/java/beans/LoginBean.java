package beans;

public class LoginBean {
    private String username;
    private String password;

    public LoginBean() {// Il costruttore non ha bisogno di parametri
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
