package motorcyclepartsstore.ui;

/** A page hosted by the shell. Forms implement this to refresh their data when shown. */
public interface Screen {

    /** Called every time the page becomes visible. */
    default void onShow() {
    }
}
