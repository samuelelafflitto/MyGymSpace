package beans;

public class LoginBean {
    private String username;
    private String password;

    public LoginBean() {// The constructor does not need parameters
    }

    // GETTER
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // SETTER
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
