package ui;

import model.AIDifficulty;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Modal dialog prompting the user to select an AI difficulty level.
 * Features a rich dark gradient background and custom polished buttons matching
 * the rest of the application's premium aesthetic.
 */
public class DifficultySelectionDialog extends JDialog {
    private AIDifficulty selectedDifficulty = null;

    /**
     * Constructs a DifficultySelectionDialog.
     *
     * @param parent the parent frame
     */
    public DifficultySelectionDialog(JFrame parent) {
        super(parent, "Select AI Difficulty", true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);

        // Gradient background panel matching LaunchScreen
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // Top Section: Title & Subtitle
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(25, 20, 10, 20));

        JLabel titleLabel = new JLabel("Choose AI Difficulty");
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Select a level that matches your skill");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(170, 180, 195));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(titleLabel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        topPanel.add(subtitleLabel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center Section: 2x2 Grid of Styled Buttons
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(new EmptyBorder(15, 30, 30, 30));

        // Use custom premium colors matching the LaunchScreen & Emerald Chess Theme
        gridPanel.add(createDifficultyButton(AIDifficulty.EASY, new Color(46, 125, 50)));    // Forest/Emerald Green
        gridPanel.add(createDifficultyButton(AIDifficulty.MEDIUM, new Color(25, 118, 210))); // Deep Cobalt Blue
        gridPanel.add(createDifficultyButton(AIDifficulty.HARD, new Color(216, 107, 36)));   // Rich Amber Orange
        gridPanel.add(createDifficultyButton(AIDifficulty.EXPERT, new Color(180, 60, 60)));   // Crimson Red

        mainPanel.add(gridPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(parent);
    }

    /**
     * Helper to create a custom styled difficulty button.
     */
    private JButton createDifficultyButton(AIDifficulty difficulty, Color baseColor) {
        String labelText = "<html><center><font size='5'><b>" + difficulty.name() + "</b></font><br>"
                + "<font size='3' color='#D1D5DB'>" + difficulty.getDescription() + "</font></center></html>";

        StyledButton button = new StyledButton(labelText, baseColor);
        button.setPreferredSize(new Dimension(170, 110));
        button.addActionListener(e -> {
            selectedDifficulty = difficulty;
            dispose();
        });
        return button;
    }

    /**
     * Retrieves the selected difficulty level.
     *
     * @return the selected AIDifficulty
     */
    public AIDifficulty getSelectedDifficulty() {
        return selectedDifficulty;
    }

    /**
     * Custom panel that paints a rich dark gradient background matching LaunchScreen.
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
     * Custom JButton with rounded borders, shadows, and smooth hover effects.
     */
    private static class StyledButton extends JButton {
        private final Color baseColor;
        private final Color hoverColor;
        private final Color pressedColor;

        public StyledButton(String text, Color baseColor) {
            super(text);
            this.baseColor = baseColor;
            this.hoverColor = adjustColor(baseColor, 18);
            this.pressedColor = adjustColor(baseColor, -15);

            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

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

            Color bg = getModel().isPressed() ? pressedColor : (getModel().isRollover() ? hoverColor : baseColor);

            // Draw shadow
            g2d.setColor(new Color(0, 0, 0, 80));
            g2d.fillRoundRect(2, 4, getWidth() - 4, getHeight() - 6, 18, 18);

            // Draw base button body
            g2d.setColor(bg);
            g2d.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 4, 18, 18);

            // Draw border glow on hover
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
