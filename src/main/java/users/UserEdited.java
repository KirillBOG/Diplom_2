package users;

public class UserEdited {
    private String newEmail;
    private String newPassword;
    private String newName;

    public UserEdited(String newEmail, String newPassword, String newName) {
        this.newEmail = newEmail;
        this.newPassword = newPassword;
        this.newName = newName;
    }
}