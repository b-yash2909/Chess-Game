import ui.LaunchScreen;
import javax.swing.SwingUtilities;

/**
 * Entry point for the Chess Application.
 */
public class Main {
    
    /**
     * Main method.
     * Initializes components and starts the game loop.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { new LaunchScreen(); });
    }
}
