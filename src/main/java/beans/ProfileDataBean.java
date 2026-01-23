package beans;

public class ProfileDataBean {
    private String newPassword;
    private String newUsername;
    private String newFirstName;
    private String newLastName;

    private String currentPassword;

    public ProfileDataBean() {// Il costruttore non ha bisogno di parametri
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public String getNewFirstName() {
        return newFirstName;
    }

    public String getNewLastName() {
        return newLastName;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    public void setNewFirstName(String newFirstName) {
        this.newFirstName = newFirstName;
    }

    public void setNewLastName(String newLastName) {
        this.newLastName = newLastName;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
}
