package motorcyclepartsstore.model;

/** An account. A null role means the account is a pending sign-up awaiting an admin. */
public class User {

    public static final String ACTIVE = "Active";
    public static final String INACTIVE = "Inactive";
    public static final String PENDING = "Pending";

    private String fullName;
    private String username;
    private String email;
    private String phone = "";
    private String salt;
    private String passwordHash;
    private Role role;
    private String status = ACTIVE;

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isActiveAdmin() {
        return role == Role.ADMIN && ACTIVE.equals(status);
    }

    public String roleLabel() {
        return role == null ? "Unassigned" : role.getLabel();
    }
}
