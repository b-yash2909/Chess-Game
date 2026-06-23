package ui;

import model.GameMode;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A highly polished, modern launch screen for Zenith Chess.
 * Features custom painted controls, smooth hover transitions, and a sleek dark
 * theme.
 */
public class LaunchScreen extends JFrame {

    /**
     * Constructs the LaunchScreen.
     */
    public LaunchScreen() {
        super(" Chess Launcher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Use custom gradient panel as the main content pane
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // TOP SECTION: Title & Branding
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(45, 20, 20, 20));

        JLabel logoLabel = new JLabel("♚  CHESS  ♔");
        logoLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 38));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("A Modern Interactive Chess Environment");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(170, 180, 195));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(logoLabel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        topPanel.add(subtitleLabel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // CENTER SECTION: Game Mode Cards
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

        JLabel modeSelectLabel = new JLabel("Choose Game Mode");
        modeSelectLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        modeSelectLabel.setForeground(new Color(220, 225, 235));
        modeSelectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(modeSelectLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        buttonPanel.setOpaque(false);

        // Styled Button 1: Player vs AI
        StyledButton btnHvC = new StyledButton(
                "<html><center><font size='5'>🤖 Player vs Computer</font><br>" +
                        "<font size='3' color='#D1D5DB'>Challenge the chess engine</font></center></html>",
                new Color(46, 117, 89));
        btnHvC.setPreferredSize(new Dimension(240, 130));
        btnHvC.addActionListener(e -> openGame(GameMode.HUMAN_VS_COMPUTER));

        // Styled Button 2: Player vs Player
        StyledButton btnHvH = new StyledButton(
                "<html><center><font size='5'>👥 Local Match</font><br>" +
                        "<font size='3' color='#D1D5DB'>Play 1v1 with a friend</font></center></html>",
                new Color(41, 98, 255));
        btnHvH.setPreferredSize(new Dimension(240, 130));
        btnHvH.addActionListener(e -> openGame(GameMode.HUMAN_VS_HUMAN));

        buttonPanel.add(btnHvC);
        buttonPanel.add(btnHvH);
        centerPanel.add(buttonPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // BOTTOM SECTION: Status Info
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(10, 20, 25, 20));

        JLabel infoLabel = new JLabel("White moves first • Compiles instantly", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setForeground(new Color(120, 135, 150));
        bottomPanel.add(infoLabel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Set dimensions & centering
        setMinimumSize(new Dimension(620, 480));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void openGame(GameMode mode) {
        new ChessFrame(mode);
        this.dispose();
    }

    /**
     * Custom JPanel that paints a rich dark gradient background.
     */
    private static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int w = getWidth();
            int h = getHeight();
            // Deep gradient from near-black slate to charcoal navy
            GradientPaint gp = new GradientPaint(0, 0, new Color(17, 20, 24), w, h, new Color(33, 37, 44));
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }

    /**
     * Custom JButton with custom rounded drawing, hover transitions, and a hand
     * cursor.
     */
    private static class StyledButton extends JButton {
        private final Color baseColor;
        private final Color hoverColor;
        private final Color pressedColor;

        public StyledButton(String text, Color baseColor) {
            super(text);
            this.baseColor = baseColor;

            // Calculate hover and pressed variants
            this.hoverColor = adjustColor(baseColor, 18);
            this.pressedColor = adjustColor(baseColor, -15);

            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Force repaint on hover transitions
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    repaint();
                }
            });
        }

        private Color adjustColor(Color c, int amount) {
            int r = Math.min(255, Math.max(0, c.getRed() + amount));
            int g = Math.min(255, Math.max(0, c.getGreen() + amount));
            int b = Math.min(255, Math.max(0, c.getBlue() + amount));
            return new Color(r, g, b);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color bg;
            if (getModel().isPressed()) {
                bg = pressedColor;
            } else if (getModel().isRollover()) {
                bg = hoverColor;
            } else {
                bg = baseColor;
            }

            // Draw shadow
            g2d.setColor(new Color(0, 0, 0, 80));
            g2d.fillRoundRect(2, 4, getWidth() - 4, getHeight() - 6, 18, 18);

            // Draw base button body
            g2d.setColor(bg);
            g2d.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 4, 18, 18);

            // Draw subtle border glow on hover
            if (getModel().isRollover()) {
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.setColor(new Color(255, 255, 255, 80));
                g2d.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 4, 18, 18);
            }

            g2d.dispose();
            super.paintComponent(g);
        }
    }
}
