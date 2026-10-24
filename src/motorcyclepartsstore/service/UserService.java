package motorcyclepartsstore.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import motorcyclepartsstore.model.Role;
import motorcyclepartsstore.model.User;

/** Accounts: sign in, sign up and admin user management (in-memory demo data). */
public final class UserService {

    private static final UserService INSTANCE = new UserService();
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern PHONE = Pattern.compile("^[0-9+\\-() ]{7,20}$");
    public static final int MIN_PASSWORD = 6;

    private final List<User> users = new ArrayList<>();

    private UserService() {
        seed("Maria Santos", "admin", "admin@oneprompt.example", "0917 100 0001", "admin123", Role.ADMIN);
        seed("Juan Dela Cruz", "clerk", "clerk@oneprompt.example", "0917 100 0002", "clerk123", Role.INVENTORY_CLERK);
        seed("Ana Reyes", "sales", "sales@oneprompt.example", "0917 100 0003", "sales123", Role.SALES);
        seed("Luis Garcia", "instructor", "instructor@oneprompt.example", "0917 100 0004", "instructor123", Role.INSTRUCTOR);
    }

    public static UserService get() {
        return INSTANCE;
    }

    private void seed(String name, String username, String email, String phone, String password, Role role) {
        User u = new User();
        u.setFullName(name);
        u.setUsername(username);
        u.setEmail(email);
        u.setPhone(phone);
        u.setRole(role);
        setPassword(u, password);
        users.add(u);
    }

    private void setPassword(User u, String password) {
        u.setSalt(PasswordUtil.newSalt());
        u.setPasswordHash(PasswordUtil.hash(u.getSalt(), password));
    }

    public List<User> all() {
        return new ArrayList<>(users);
    }

    public User find(String username) {
        if (username == null) {
            return null;
        }
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username.trim())) {
                return u;
            }
        }
        return null;
    }

    /** Result of a sign-in attempt: the user on success, otherwise an error message. */
    public static final class AuthResult {
        public final User user;
        public final String error;

        AuthResult(User user, String error) {
            this.user = user;
            this.error = error;
        }
    }

    public AuthResult authenticate(String username, String password, Role level) {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            return new AuthResult(null, "Enter your username and password.");
        }
        User u = find(username);
        if (u == null || !u.getPasswordHash().equals(PasswordUtil.hash(u.getSalt(), password))) {
            return new AuthResult(null, "Invalid username or password.");
        }
        if (User.PENDING.equals(u.getStatus()) || u.getRole() == null) {
            return new AuthResult(null, "Your account is waiting for an admin to assign a user level.");
        }
        if (!User.ACTIVE.equals(u.getStatus())) {
            return new AuthResult(null, "This account is inactive. Contact an admin.");
        }
        if (u.getRole() != level) {
            return new AuthResult(null, "This account is not registered as " + level.getLabel() + ".");
        }
        return new AuthResult(u, null);
    }

    /** Sign-up request: creates a pending account without a user level. Returns an error or null. */
    public String register(String fullName, String username, String email, String password, String confirm) {
        String err = validateCommon(null, fullName, username, email, "");
        if (err != null) {
            return err;
        }
        err = validatePassword(password, confirm, true);
        if (err != null) {
            return err;
        }
        User u = new User();
        u.setFullName(fullName.trim());
        u.setUsername(username.trim());
        u.setEmail(email.trim());
        u.setStatus(User.PENDING);
        setPassword(u, password);
        users.add(u);
        return null;
    }

    /**
     * Adds or updates a user (admin only). existing == null creates a new user.
     * Returns an error message or null on success.
     */
    public String save(User existing, String fullName, String username, String email, String phone,
            Role role, String status, String newPassword, String confirm) {
        Session.require(Role.ADMIN);
        String err = validateCommon(existing, fullName, username, email, phone);
        if (err != null) {
            return err;
        }
        if (role == null) {
            return "Choose a user level.";
        }
        boolean passwordGiven = newPassword != null && !newPassword.isEmpty();
        err = validatePassword(newPassword, confirm, existing == null || passwordGiven);
        if (err != null) {
            return err;
        }
        if (existing != null && existing == Session.current()
                && (role != existing.getRole() || !User.ACTIVE.equals(status))) {
            return "You cannot change your own user level or deactivate your own account.";
        }
        if (existing != null && existing.isActiveAdmin()
                && (role != Role.ADMIN || !User.ACTIVE.equals(status)) && countActiveAdmins() <= 1) {
            return "At least one active Admin must remain.";
        }
        User u = existing;
        if (u == null) {
            u = new User();
            users.add(u);
        }
        u.setFullName(fullName.trim());
        u.setUsername(username.trim());
        u.setEmail(email.trim());
        u.setPhone(phone == null ? "" : phone.trim());
        u.setRole(role);
        u.setStatus(status);
        if (passwordGiven) {
            setPassword(u, newPassword);
        }
        return null;
    }

    /** Deletes a user (admin only). Returns an error message or null on success. */
    public String delete(User target) {
        Session.require(Role.ADMIN);
        if (target == Session.current()) {
            return "You cannot delete the account you are signed in with.";
        }
        if (target.isActiveAdmin() && countActiveAdmins() <= 1) {
            return "The last active Admin cannot be deleted.";
        }
        users.remove(target);
        return null;
    }

    public int countActiveAdmins() {
        int n = 0;
        for (User u : users) {
            if (u.isActiveAdmin()) {
                n++;
            }
        }
        return n;
    }

    private String validateCommon(User existing, String fullName, String username, String email, String phone) {
        if (isBlank(fullName)) {
            return "Full name is required.";
        }
        if (isBlank(username)) {
            return "Username is required.";
        }
        if (!username.trim().matches("[A-Za-z0-9._-]{3,30}")) {
            return "Username must be 3-30 letters, digits, dots, dashes or underscores.";
        }
        User other = find(username);
        if (other != null && other != existing) {
            return "That username is already taken.";
        }
        if (isBlank(email) || !EMAIL.matcher(email.trim()).matches()) {
            return "Enter a valid email address.";
        }
        if (!isBlank(phone) && !PHONE.matcher(phone.trim()).matches()) {
            return "Enter a valid phone number.";
        }
        return null;
    }

    private String validatePassword(String password, String confirm, boolean required) {
        boolean given = password != null && !password.isEmpty();
        if (!given && !required) {
            return null;
        }
        if (!given) {
            return "Password is required.";
        }
        if (password.length() < MIN_PASSWORD) {
            return "Password must be at least " + MIN_PASSWORD + " characters.";
        }
        if (!password.equals(confirm)) {
            return "Passwords do not match.";
        }
        return null;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
