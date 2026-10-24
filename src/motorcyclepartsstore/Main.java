package motorcyclepartsstore;

import javax.swing.SwingUtilities;
import motorcyclepartsstore.service.PredictionService;
import motorcyclepartsstore.ui.Theme;

/**
 * Entry point of the One Prompt Away - Motorcycle Parts Store System.
 *
 * @author zeal
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Theme.install();
            AppFrame frame = new AppFrame();
            frame.setVisible(true);
            // Train the demo Weka models in the background so the UI stays responsive.
            PredictionService.get().start(frame::modelsTrained);
        });
    }

}
