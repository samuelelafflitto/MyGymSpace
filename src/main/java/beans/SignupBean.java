package beans;

public class SignupBean extends LoginBean {
    private String firstName;
    private String lastName;

    public SignupBean() {// The constructor does not need parameters
    }

    // GETTER
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    // SETTER
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
