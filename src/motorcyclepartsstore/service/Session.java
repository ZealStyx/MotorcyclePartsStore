package motorcyclepartsstore.service;

import motorcyclepartsstore.model.Role;
import motorcyclepartsstore.model.User;

/** Holds the signed-in user and enforces role checks inside the service layer. */
public final class Session {

    private static User current;

    private Session() {
    }

    public static User current() {
        return current;
    }

    public static void set(User user) {
        current = user;
    }

    public static void clear() {
        current = null;
    }

    /** Throws SecurityException unless the signed-in user holds one of the given roles. */
    public static void require(Role... roles) {
        if (current != null) {
            for (Role r : roles) {
                if (current.getRole() == r) {
                    return;
                }
            }
        }
        throw new SecurityException("Your user level is not allowed to do this.");
    }
}
