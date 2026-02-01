package beans;

public class ProfileDataBean {
    private String newPassword;
    private String newFirstName;
    private String newLastName;

    private String inputPassword;
    private String confirmPassword;

    private String currentPassword;

    public ProfileDataBean() {// Il costruttore non ha bisogno di parametri
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getNewFirstName() {
        return newFirstName;
    }

    public String getNewLastName() {
        return newLastName;
    }

    public String getInputPassword() {
        return inputPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setNewFirstName(String newFirstName) {
        this.newFirstName = newFirstName;
    }

    public void setNewLastName(String newLastName) {
        this.newLastName = newLastName;
    }

    public void setInputPassword(String inputPassword) {
        this.inputPassword = inputPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
}
