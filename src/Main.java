import ui.HomeFrame;
import javax.swing.SwingUtilities;


/**
 * 負責叫出首頁 HomeFrame
 */


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomeFrame());
    }
}
