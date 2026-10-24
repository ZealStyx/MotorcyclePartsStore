package motorcyclepartsstore.ui;

import java.awt.Window;

/** What a screen may ask of the application shell. */
public interface Navigator {

    void navigate(Page page);

    void logout();

    /** Window to use as the owner of dialogs. */
    Window window();
}
