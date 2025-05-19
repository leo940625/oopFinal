//package MovieBooking.help;
package ui.ticketdownload;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class TicketPanel extends JPanel {

    private final int PANEL_WIDTH = 400;
    private final int PANEL_HEIGHT = 200;
    private final int CORNER_RADIUS = 30;
    private final int SIDE_RADIUS = 12;
    private final int SIDE_COUNT = 3;

    public TicketPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Area ticketArea = new Area(new Rectangle2D.Double(0, 0, PANEL_WIDTH, PANEL_HEIGHT));

        int r = CORNER_RADIUS;
        int d = r * 2;

        // Subtract corner circles
        ticketArea.subtract(new Area(new Ellipse2D.Double(-r, -r, d, d))); // Top-left
        ticketArea.subtract(new Area(new Ellipse2D.Double(PANEL_WIDTH - r, -r, d, d))); // Top-right
        ticketArea.subtract(new Area(new Ellipse2D.Double(-r, PANEL_HEIGHT - r, d, d))); // Bottom-left
        ticketArea.subtract(new Area(new Ellipse2D.Double(PANEL_WIDTH - r, PANEL_HEIGHT - r, d, d))); // Bottom-right

        int sr = SIDE_RADIUS;
        int sd = sr * 2;
        int baseY = 60;
        int spacing = 40;

        for (int i = 0; i < SIDE_COUNT; i++) {
            int y = baseY + i * spacing;
            ticketArea.subtract(new Area(new Ellipse2D.Double(-sr, y - sr, sd, sd)));
            ticketArea.subtract(new Area(new Ellipse2D.Double(PANEL_WIDTH - sr, y - sr, sd, sd)));
        }

        g2.setColor(Color.WHITE);
        g2.fill(ticketArea);

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.draw(ticketArea);

        g2.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Ticket Preview");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLayout(new FlowLayout(FlowLayout.CENTER, 80, 80));
            frame.add(new TicketPanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
