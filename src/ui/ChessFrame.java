package ui;

import model.GameState;
import model.GameMode;
import model.PieceColor;
import model.GameStatus;
import model.Board;
import model.Piece;
import model.Pawn;
import model.Knight;
import model.Bishop;
import model.Rook;
import model.Queen;
import model.Square;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Main application window for Zenith Chess.
 * Houses the ChessBoardPanel and a feature-rich, modern side dashboard panel
 * containing timers, move logs, capture lists, and interactive controls.
 */
public class ChessFrame extends JFrame {
    private JLabel statusLabel;
    private final ChessBoardPanel chessBoardPanel;
    private final GameMode gameMode;

    // Sidebar Components
    private JTextArea moveLogArea;
    private JLabel whiteTimeLabel;
    private JLabel blackTimeLabel;
    private JLabel whiteCapturedLabel;
    private JLabel blackCapturedLabel;
    private JPanel whiteTimerPanel;
    private JPanel blackTimerPanel;

    // Timer state (10 minutes each by default)
    private int whiteTimeLeft = 600;
    private int blackTimeLeft = 600;
    private Timer gameTimer;
    private boolean timeOutOccurred = false;

    /**
     * Constructs the ChessFrame.
     */
    public ChessFrame(GameMode mode) {
        super("Chess Game");
        this.gameMode = mode;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        // Create initial game state
        GameState gameState = new GameState();

        // Create Left Side: Chess Board Panel
        chessBoardPanel = new ChessBoardPanel(gameState, this, mode);
        add(chessBoardPanel, BorderLayout.CENTER);

        // Create Right Side: Sidebar Panel
        JPanel sidebar = createSidebar(mode);
        add(sidebar, BorderLayout.EAST);

        // Initialize Timers
        setupTimers();

        // Fit, center, and show window
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Initial captured pieces display
        updateCapturedPieces();
    }

    /**
     * Creates and styles the sidebar container.
     */
    private JPanel createSidebar(GameMode mode) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 688));
        panel.setBackground(new Color(26, 30, 36));
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // HEADER: Game Mode Info
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("CHESS");
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String modeText = (mode.getType() == GameMode.Type.HUMAN_VS_HUMAN) ? "Local 2-Player Match" : "vs Computer Engine";
        JLabel modeLabel = new JLabel(modeText);
        modeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        modeLabel.setForeground(new Color(130, 145, 160));
        modeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        headerPanel.add(modeLabel);
        panel.add(headerPanel, BorderLayout.NORTH);

        // CENTER BODY PANEL (Status, Clocks, Captured Pieces, Move Log)
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setOpaque(false);
        bodyPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

        // Status Banner
        JPanel statusContainer = new JPanel(new BorderLayout());
        statusContainer.setBackground(new Color(36, 42, 52));
        statusContainer.setBorder(new EmptyBorder(8, 12, 8, 12));

        String initialStatus = (mode.getType() == GameMode.Type.HUMAN_VS_HUMAN) ? "White's turn" : "Your turn — White";
        statusLabel = new JLabel(initialStatus, SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setForeground(new Color(255, 255, 255));
        statusContainer.add(statusLabel, BorderLayout.CENTER);
        bodyPanel.add(statusContainer);
        bodyPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Digital Clocks Panel
        JPanel clocksContainer = new JPanel(new GridLayout(1, 2, 12, 0));
        clocksContainer.setOpaque(false);
        clocksContainer.setMaximumSize(new Dimension(300, 55));

        // White Clock Panel
        whiteTimerPanel = new JPanel(new BorderLayout());
        whiteTimerPanel.setBackground(new Color(40, 46, 56));
        whiteTimerPanel.setBorder(new LineBorder(new Color(60, 68, 82), 1, true));
        whiteTimeLabel = new JLabel("10:00", SwingConstants.CENTER);
        whiteTimeLabel.setFont(new Font("Segoe UI Symbol", Font.BOLD, 18));
        whiteTimeLabel.setForeground(Color.WHITE);
        JLabel whiteTitle = new JLabel("White", SwingConstants.CENTER);
        whiteTitle.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        whiteTitle.setForeground(new Color(170, 180, 195));
        whiteTimerPanel.add(whiteTitle, BorderLayout.NORTH);
        whiteTimerPanel.add(whiteTimeLabel, BorderLayout.CENTER);

        // Black Clock Panel
        blackTimerPanel = new JPanel(new BorderLayout());
        blackTimerPanel.setBackground(new Color(40, 46, 56));
        blackTimerPanel.setBorder(new LineBorder(new Color(60, 68, 82), 1, true));
        blackTimeLabel = new JLabel("10:00", SwingConstants.CENTER);
        blackTimeLabel.setFont(new Font("Segoe UI Symbol", Font.BOLD, 18));
        blackTimeLabel.setForeground(Color.WHITE);
        JLabel blackTitle = new JLabel("Black", SwingConstants.CENTER);
        blackTitle.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        blackTitle.setForeground(new Color(170, 180, 195));
        blackTimerPanel.add(blackTitle, BorderLayout.NORTH);
        blackTimerPanel.add(blackTimeLabel, BorderLayout.CENTER);

        clocksContainer.add(whiteTimerPanel);
        clocksContainer.add(blackTimerPanel);
        bodyPanel.add(clocksContainer);
        bodyPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Captured Pieces Section
        JPanel capPanel = new JPanel();
        capPanel.setLayout(new BoxLayout(capPanel, BoxLayout.Y_AXIS));
        capPanel.setBackground(new Color(36, 42, 52));
        capPanel.setBorder(new EmptyBorder(8, 10, 8, 10));

        whiteCapturedLabel = new JLabel("White Captures: None");
        whiteCapturedLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 12));
        whiteCapturedLabel.setForeground(new Color(220, 225, 235));

        blackCapturedLabel = new JLabel("Black Captures: None");
        blackCapturedLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 12));
        blackCapturedLabel.setForeground(new Color(220, 225, 235));

        capPanel.add(whiteCapturedLabel);
        capPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        capPanel.add(blackCapturedLabel);
        bodyPanel.add(capPanel);
        bodyPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Move History Log Section
        JPanel moveLogPanel = new JPanel(new BorderLayout());
        moveLogPanel.setBackground(new Color(22, 25, 30));
        moveLogPanel.setBorder(new LineBorder(new Color(45, 52, 64), 1, true));

        JLabel historyTitle = new JLabel("Move History Log", SwingConstants.CENTER);
        historyTitle.setFont(new Font("Segoe UI Semibold", Font.BOLD, 11));
        historyTitle.setForeground(new Color(150, 165, 180));
        historyTitle.setBorder(new EmptyBorder(4, 4, 4, 4));
        moveLogPanel.add(historyTitle, BorderLayout.NORTH);

        moveLogArea = new JTextArea();
        moveLogArea.setEditable(false);
        moveLogArea.setBackground(new Color(18, 20, 24));
        moveLogArea.setForeground(new Color(100, 210, 120)); // Matrix Green
        moveLogArea.setFont(new Font("Courier New", Font.PLAIN, 13));
        moveLogArea.setMargin(new Insets(5, 8, 5, 8));

        JScrollPane logScroll = new JScrollPane(moveLogArea);
        logScroll.setBorder(null);
        moveLogPanel.add(logScroll, BorderLayout.CENTER);
        bodyPanel.add(moveLogPanel);

        panel.add(bodyPanel, BorderLayout.CENTER);

        // SOUTH PANEL: Control Buttons
        JPanel southPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        southPanel.setOpaque(false);

        StyledButton btnUndo = new StyledButton("Undo", new Color(130, 80, 50));
        btnUndo.addActionListener(e -> chessBoardPanel.undoLastMove());

        StyledButton btnHint = new StyledButton("AI Hint", new Color(40, 120, 180));
        btnHint.addActionListener(e -> chessBoardPanel.triggerHint());
        
        StyledButton btnNew = new StyledButton("Reset", new Color(75, 85, 95));
        btnNew.addActionListener(e -> {
            stopGameTimer();
            new LaunchScreen();
            this.dispose();
        });

        StyledButton btnResign = new StyledButton("Resign", new Color(170, 55, 55));
        btnResign.addActionListener(e -> handleResign());

        southPanel.add(btnUndo);
        if (mode.isHintsEnabled()) {
            southPanel.add(btnHint);
        } else {
            southPanel.add(new JLabel(""));
        }
        southPanel.add(btnNew);
        southPanel.add(btnResign);

        panel.add(southPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Configures the digital timers countdown thread.
     */
    private void setupTimers() {
        gameTimer = new Timer(1000, e -> {
            if (timeOutOccurred)
                return;
            GameState state = chessBoardPanel.getGameState();

            // Only countdown if game is ongoing
            if (state.getStatus() == GameStatus.ONGOING || state.getStatus() == GameStatus.CHECK) {
                if (state.getCurrentTurn() == PieceColor.WHITE) {
                    whiteTimeLeft--;
                    if (whiteTimeLeft <= 0) {
                        handleTimeOut(PieceColor.WHITE);
                    }
                } else {
                    blackTimeLeft--;
                    if (blackTimeLeft <= 0) {
                        handleTimeOut(PieceColor.BLACK);
                    }
                }
                updateClockLabels();
                highlightActiveTimer(state.getCurrentTurn());
            }
        });
        gameTimer.start();
    }

    private void stopGameTimer() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }

    private void highlightActiveTimer(PieceColor activeColor) {
        // Highlight active turn panel with green border/glow
        if (activeColor == PieceColor.WHITE) {
            whiteTimerPanel.setBackground(new Color(46, 85, 62));
            blackTimerPanel.setBackground(new Color(40, 46, 56));
        } else {
            blackTimerPanel.setBackground(new Color(46, 85, 62));
            whiteTimerPanel.setBackground(new Color(40, 46, 56));
        }
    }

    private void updateClockLabels() {
        whiteTimeLabel.setText(formatTime(whiteTimeLeft));
        blackTimeLabel.setText(formatTime(blackTimeLeft));
    }

    private String formatTime(int totalSecs) {
        int minutes = totalSecs / 60;
        int seconds = totalSecs % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void handleTimeOut(PieceColor colorLost) {
        timeOutOccurred = true;
        stopGameTimer();
        SoundManager.playGameOver();
        String winner = (colorLost == PieceColor.WHITE) ? "Black" : "White";
        if (gameMode.getType() == GameMode.Type.HUMAN_VS_COMPUTER) {
            winner = (colorLost == PieceColor.WHITE) ? "Computer" : "Player";
        }
        JOptionPane.showMessageDialog(this, "Time is up! " + winner + " wins on time!", "Game Over",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleResign() {
        stopGameTimer();
        SoundManager.playGameOver();
        String text = (chessBoardPanel.getGameState().getCurrentTurn() == PieceColor.WHITE)
                ? "White resigns. Black wins!"
                : "Black resigns. White wins!";
        JOptionPane.showMessageDialog(this, text, "Resigned", JOptionPane.INFORMATION_MESSAGE);
        new LaunchScreen();
        this.dispose();
    }

    /**
     * Updates the status bar/label message.
     */
    public void setStatus(String message) {
        statusLabel.setText(message);
    }

    /**
     * Synchronizes and updates the move history log and captured pieces lists.
     */
    public void updateMoveLog() {
        if (chessBoardPanel == null || moveLogArea == null)
            return;

        List<model.Move> history = chessBoardPanel.getGameState().getMoveHistory();
        StringBuilder sb = new StringBuilder();
        int moveNum = 1;
        for (int i = 0; i < history.size(); i += 2) {
            sb.append(String.format("%2d. %-8s", moveNum, history.get(i).toString()));
            if (i + 1 < history.size()) {
                sb.append(history.get(i + 1).toString());
            }
            sb.append("\n");
            moveNum++;
        }
        moveLogArea.setText(sb.toString());
        moveLogArea.setCaretPosition(moveLogArea.getDocument().getLength());

        updateCapturedPieces();
    }

    /**
     * Calculates captured pieces from initial vs remaining layout on board.
     */
    private void updateCapturedPieces() {
        int wP = 0, wN = 0, wB = 0, wR = 0, wQ = 0;
        int bP = 0, bN = 0, bB = 0, bR = 0, bQ = 0;

        Board b = chessBoardPanel.getGameState().getBoard();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = b.getPiece(new Square(r, c));
                if (p != null) {
                    if (p.getColor() == PieceColor.WHITE) {
                        if (p instanceof Pawn)
                            wP++;
                        else if (p instanceof Knight)
                            wN++;
                        else if (p instanceof Bishop)
                            wB++;
                        else if (p instanceof Rook)
                            wR++;
                        else if (p instanceof Queen)
                            wQ++;
                    } else {
                        if (p instanceof Pawn)
                            bP++;
                        else if (p instanceof Knight)
                            bN++;
                        else if (p instanceof Bishop)
                            bB++;
                        else if (p instanceof Rook)
                            bR++;
                        else if (p instanceof Queen)
                            bQ++;
                    }
                }
            }
        }

        // Calculate differences based on starting positions
        StringBuilder whiteCaps = new StringBuilder(); // Captured Black pieces
        appendCaps(whiteCaps, "♟", 8 - bP);
        appendCaps(whiteCaps, "♞", 2 - bN);
        appendCaps(whiteCaps, "♝", 2 - bB);
        appendCaps(whiteCaps, "♜", 2 - bR);
        appendCaps(whiteCaps, "♛", 1 - bQ);

        StringBuilder blackCaps = new StringBuilder(); // Captured White pieces
        appendCaps(blackCaps, "♙", 8 - wP);
        appendCaps(blackCaps, "♘", 2 - wN);
        appendCaps(blackCaps, "♗", 2 - wB);
        appendCaps(blackCaps, "♖", 2 - wR);
        appendCaps(blackCaps, "♕", 1 - wQ);

        whiteCapturedLabel.setText("Captures: " + (whiteCaps.length() == 0 ? "None" : whiteCaps.toString()));
        blackCapturedLabel.setText("Captures: " + (blackCaps.length() == 0 ? "None" : blackCaps.toString()));
    }

    private void appendCaps(StringBuilder sb, String symbol, int count) {
        for (int i = 0; i < count; i++) {
            sb.append(symbol).append(" ");
        }
    }

    /**
     * Inner custom JButton for themed design in sidebar.
     */
    private static class StyledButton extends JButton {
        private final Color baseColor;
        private final Color hoverColor;
        private final Color pressedColor;

        public StyledButton(String text, Color baseColor) {
            super(text);
            this.baseColor = baseColor;
            this.hoverColor = adjustColor(baseColor, 20);
            this.pressedColor = adjustColor(baseColor, -15);

            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
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

            g2d.setColor(bg);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

            if (getModel().isRollover()) {
                g2d.setColor(new Color(255, 255, 255, 60));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            }

            g2d.dispose();
            super.paintComponent(g);
        }
    }
}
