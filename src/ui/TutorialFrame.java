package ui;

import model.AIDifficulty;
import model.GameMode;
import model.Lesson;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 * Main window for the interactive Chess Tutorial ("Learn to Play").
 * Features a list of lessons on the left, instruction display at the top,
 * practice board in the middle-right, and navigation controls at the bottom.
 */
public class TutorialFrame extends JFrame {
    private final List<Lesson> lessons;
    private int currentLessonIndex = 0;

    // UI Components
    private JList<String> lessonJList;
    private JTextPane instructionArea;
    private JPanel boardContainer;
    private TutorialPracticeBoardPanel activeBoardPanel;

    private JButton btnPrev;
    private JButton btnNext;
    private JLabel lblProgress;

    /**
     * Constructs a new TutorialFrame.
     */
    public TutorialFrame() {
        super("Learn to Play Chess");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Load the lessons curriculum
        lessons = TutorialContent.getAllLessons();

        // Root panel setup
        JPanel rootPanel = new JPanel(new BorderLayout());
        setContentPane(rootPanel);

        // 1. Sidebar Lesson List (WEST)
        JPanel sidebarPanel = createSidebarPanel();
        rootPanel.add(sidebarPanel, BorderLayout.WEST);

        // 2. Main Content Area (CENTER)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(31, 41, 55));

        // Instruction area (TOP of Center)
        instructionArea = new JTextPane();
        instructionArea.setContentType("text/html");
        instructionArea.setEditable(false);
        instructionArea.setBackground(new Color(24, 28, 36));
        instructionArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane instructionScroll = new JScrollPane(instructionArea);
        instructionScroll.setPreferredSize(new Dimension(680, 120)); // Reduced height from 160
        instructionScroll.setBorder(new LineBorder(new Color(45, 55, 72), 1));
        centerPanel.add(instructionScroll, BorderLayout.NORTH);

        // Practice Board Container (CENTER of Center)
        boardContainer = new JPanel(new GridBagLayout());
        boardContainer.setBackground(new Color(31, 41, 55));
        centerPanel.add(boardContainer, BorderLayout.CENTER);

        rootPanel.add(centerPanel, BorderLayout.CENTER);

        // 3. Navigation Controls (SOUTH)
        JPanel navPanel = createNavigationPanel();
        rootPanel.add(navPanel, BorderLayout.SOUTH);

        // Load the initial lesson
        loadLesson(0);

        // Frame configuration
        setMinimumSize(new Dimension(930, 680)); // Reduced height from 720
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Creates the sidebar containing the lesson titles.
     */
    private JPanel createSidebarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(240, 0));
        panel.setBackground(new Color(20, 24, 30));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Curriculum", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(5, 5, 15, 5));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Create String array for JList
        String[] titles = new String[lessons.size()];
        for (int i = 0; i < lessons.size(); i++) {
            titles[i] = lessons.get(i).getTitle();
        }

        lessonJList = new JList<>(titles);
        lessonJList.setBackground(new Color(20, 24, 30));
        lessonJList.setForeground(new Color(209, 213, 219));
        lessonJList.setSelectionBackground(new Color(46, 125, 50)); // Emerald selection
        lessonJList.setSelectionForeground(Color.WHITE);
        lessonJList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lessonJList.setFixedCellHeight(35);
        lessonJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        lessonJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = lessonJList.getSelectedIndex();
                if (selectedIndex >= 0 && selectedIndex < lessons.size() && selectedIndex != currentLessonIndex) {
                    loadLesson(selectedIndex);
                }
            }
        });

        JScrollPane listScroll = new JScrollPane(lessonJList);
        listScroll.setBorder(null);
        listScroll.setOpaque(false);
        listScroll.getViewport().setOpaque(false);
        panel.add(listScroll, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the bottom navigation bar panel.
     */
    private JPanel createNavigationPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(new Color(20, 24, 30));
        panel.setBorder(new LineBorder(new Color(45, 55, 72), 1));

        // Previous button
        btnPrev = new StyledButton("<< Previous", new Color(75, 85, 99));
        btnPrev.addActionListener(e -> {
            if (currentLessonIndex > 0) {
                loadLesson(currentLessonIndex - 1);
            }
        });

        // Try Again button
        JButton btnReset = new StyledButton("Try Again", new Color(130, 80, 50));
        btnReset.addActionListener(e -> {
            if (activeBoardPanel != null) {
                activeBoardPanel.resetToLessonSetup();
            }
        });

        // Next button
        btnNext = new StyledButton("Next >>", new Color(46, 125, 50));
        btnNext.addActionListener(e -> {
            if (currentLessonIndex < lessons.size() - 1) {
                loadLesson(currentLessonIndex + 1);
            } else {
                // Final lesson: Start Guided Practice Game
                GameMode practiceMode = new GameMode(GameMode.Type.HUMAN_VS_COMPUTER, AIDifficulty.EASY);
                practiceMode.setHintsEnabled(true);
                new ChessFrame(practiceMode);
                this.dispose();
            }
        });

        // Back to Menu button
        JButton btnMenu = new StyledButton("Back to Menu", new Color(180, 60, 60));
        btnMenu.addActionListener(e -> {
            new LaunchScreen();
            this.dispose();
        });

        lblProgress = new JLabel("Lesson 1 of 14");
        lblProgress.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblProgress.setForeground(new Color(156, 163, 175));

        panel.add(btnPrev);
        panel.add(btnReset);
        panel.add(lblProgress);
        panel.add(btnNext);
        panel.add(btnMenu);

        return panel;
    }

    /**
     * Loads the lesson details and rebuilds the active practice board.
     *
     * @param index the index of the lesson to load
     */
    private void loadLesson(int index) {
        currentLessonIndex = index;
        Lesson lesson = lessons.get(index);

        // 1. Update Sidebar selection
        lessonJList.setSelectedIndex(index);

        // 2. Format and render instruction HTML
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family: Segoe UI, Arial, sans-serif; color: #E5E7EB; background-color: #181C24; margin: 8px;'>");
        html.append("<h1 style='color: #FFFFFF; font-size: 17px; margin-bottom: 6px; font-weight: bold;'>")
                .append(lesson.getTitle()).append("</h1>");
        for (String paragraph : lesson.getInstructionParagraphs()) {
            html.append("<p style='font-size: 13px; line-height: 1.45; margin-bottom: 6px;'>")
                    .append(paragraph).append("</p>");
        }
        if (lesson.getHintText() != null && !lesson.getHintText().isEmpty()) {
            html.append("<p style='font-size: 12px; color: #9CA3AF; font-style: italic; margin-top: 10px; border-top: 1px solid #374151; padding-top: 6px;'>")
                    .append("<b>Practice Tip:</b> ").append(lesson.getHintText()).append("</p>");
        }
        html.append("</body></html>");
        instructionArea.setText(html.toString());

        // 3. Rebuild the interactive practice board
        boardContainer.removeAll();
        activeBoardPanel = new TutorialPracticeBoardPanel(lesson);
        boardContainer.add(activeBoardPanel);

        // 4. Update Navigation buttons
        btnPrev.setEnabled(index > 0);
        lblProgress.setText(String.format("Lesson %d of %d", index + 1, lessons.size()));

        if (index == lessons.size() - 1) {
            btnNext.setText("Start Guided Practice");
            btnNext.setBackground(new Color(124, 58, 237)); // Purple highlight for final CTA
        } else {
            btnNext.setText("Next >>");
            btnNext.setBackground(new Color(46, 125, 50));
        }

        boardContainer.revalidate();
        boardContainer.repaint();
    }

    /**
     * Custom themed JButton helper used for navigation components.
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
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    repaint();
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
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
        public void setBackground(Color bg) {
            // Overridden to adapt hover styles if background changes dynamically
            super.setBackground(bg);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color bg = getModel().isPressed() ? pressedColor : (getModel().isRollover() ? hoverColor : getBackground() == null ? baseColor : getBackground());
            if (!isEnabled()) {
                bg = new Color(55, 65, 81);
            }

            g2d.setColor(bg);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

            if (getModel().isRollover() && isEnabled()) {
                g2d.setColor(new Color(255, 255, 255, 60));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            }

            g2d.dispose();
            super.paintComponent(g);
        }
    }
}
