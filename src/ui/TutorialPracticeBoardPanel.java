package ui;

import model.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Interactive practice board used within the tutorial system.
 * Supports both free movement demonstration and rule-bound gameplay validation.
 */
public class TutorialPracticeBoardPanel extends JPanel implements MouseListener {
    // CONSTANTS
    public static final int TILE_SIZE = 55; // Reduced from 70
    public static final int MARGIN = 18; // Reduced from 22
    public static final int BOARD_SIZE = TILE_SIZE * 8;

    // Matching main Chess colors for visual consistency
    public static final Color LIGHT_SQUARE = new Color(238, 238, 210);
    public static final Color DARK_SQUARE = new Color(118, 150, 86);
    public static final Color SELECTED_COLOR = new Color(247, 247, 105, 170);
    public static final Color LEGAL_MOVE_COLOR = new Color(0, 0, 0, 35);
    public static final Color LAST_MOVE_COLOR = new Color(186, 202, 43, 130);
    public static final Color CHECK_COLOR = new Color(240, 98, 146, 180);
    public static final Color MARGIN_BG = new Color(22, 26, 30);
    public static final Color TEXT_COLOR = new Color(175, 188, 202);

    private final Lesson lesson;
    private Board board;
    private GameState gameState;

    private Square selectedSquare = null;
    private List<Square> legalMovesForSelected = new ArrayList<>();
    private List<Move> legalDemoMovesForSelected = new ArrayList<>();
    private Square lastMoveFrom = null;
    private Square lastMoveTo = null;

    /**
     * Constructs a TutorialPracticeBoardPanel for the specified lesson.
     *
     * @param lesson the Lesson to display and practice
     */
    public TutorialPracticeBoardPanel(Lesson lesson) {
        this.lesson = lesson;
        resetToLessonSetup();

        int totalSize = BOARD_SIZE + 2 * MARGIN;
        setPreferredSize(new Dimension(totalSize, totalSize));
        addMouseListener(this);
    }

    /**
     * Rebuilds the board/gameState from the current Lesson's setup again.
     */
    public final void resetToLessonSetup() {
        selectedSquare = null;
        legalMovesForSelected = new ArrayList<>();
        legalDemoMovesForSelected = new ArrayList<>();
        lastMoveFrom = null;
        lastMoveTo = null;

        if (lesson.getPracticeMode() == PracticeMode.FREE_MOVE) {
            this.board = new Board();
            this.board.clearAllPieces();
            this.gameState = null;
            for (Lesson.PiecePlacement placement : lesson.getSetup()) {
                Piece p = TutorialContent.newPieceInstance(placement.pieceClass, placement.color);
                p.setHasMoved(false);
                this.board.setPiece(placement.square, p);
            }
        } else {
            this.board = null;
            this.gameState = new GameState();
            this.gameState.getBoard().clearAllPieces();
            for (Lesson.PiecePlacement placement : lesson.getSetup()) {
                Piece p = TutorialContent.newPieceInstance(placement.pieceClass, placement.color);
                p.setHasMoved(false);
                this.gameState.getBoard().setPiece(placement.square, p);
            }

            // Adjust setup details for specific lessons
            if (lesson.getTitle().contains("En Passant")) {
                this.gameState.getBoard().setEnPassantTarget(new Square(2, 3)); // d6 (Row 2, Col 3)
            } else if (lesson.getTitle().contains("Stalemate")) {
                this.gameState.setCurrentTurn(PieceColor.BLACK);
            }

            this.gameState.updateStatus();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 1. Draw outer background
        g2d.setColor(MARGIN_BG);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // 2. Draw Checkered Squares
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                boolean isLight = (r + c) % 2 == 0;
                g2d.setColor(isLight ? LIGHT_SQUARE : DARK_SQUARE);
                g2d.fillRect(c * TILE_SIZE + MARGIN, r * TILE_SIZE + MARGIN, TILE_SIZE, TILE_SIZE);
            }
        }

        // 3. Highlight Last Move
        if (lastMoveFrom != null && lastMoveTo != null) {
            g2d.setColor(LAST_MOVE_COLOR);
            g2d.fillRect(lastMoveFrom.getCol() * TILE_SIZE + MARGIN, lastMoveFrom.getRow() * TILE_SIZE + MARGIN, TILE_SIZE, TILE_SIZE);
            g2d.fillRect(lastMoveTo.getCol() * TILE_SIZE + MARGIN, lastMoveTo.getRow() * TILE_SIZE + MARGIN, TILE_SIZE, TILE_SIZE);
        }

        // 4. Highlight Selected Piece
        if (selectedSquare != null) {
            g2d.setColor(SELECTED_COLOR);
            g2d.fillRect(selectedSquare.getCol() * TILE_SIZE + MARGIN, selectedSquare.getRow() * TILE_SIZE + MARGIN, TILE_SIZE, TILE_SIZE);
        }

        // 5. Highlight King in check (GAME_RULE_DEMO mode)
        if (lesson.getPracticeMode() == PracticeMode.GAME_RULE_DEMO && gameState != null) {
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    Piece p = gameState.getBoard().getPiece(new Square(r, c));
                    if (p instanceof King && p.getColor() == gameState.getCurrentTurn()) {
                        if (gameState.getBoard().isInCheck(gameState.getCurrentTurn())) {
                            g2d.setColor(CHECK_COLOR);
                            g2d.fillRect(c * TILE_SIZE + MARGIN, r * TILE_SIZE + MARGIN, TILE_SIZE, TILE_SIZE);
                        }
                    }
                }
            }
        }

        // 6. Draw Legal Move Dots
        g2d.setColor(LEGAL_MOVE_COLOR);
        Stroke oldStroke = g2d.getStroke();
        if (lesson.getPracticeMode() == PracticeMode.FREE_MOVE) {
            for (Square to : legalMovesForSelected) {
                int tx = to.getCol() * TILE_SIZE + MARGIN;
                int ty = to.getRow() * TILE_SIZE + MARGIN;
                Piece destPiece = board.getPiece(to);
                if (destPiece != null) {
                    g2d.setStroke(new BasicStroke(4));
                    g2d.drawOval(tx + 8, ty + 8, TILE_SIZE - 16, TILE_SIZE - 16);
                } else {
                    int diameter = TILE_SIZE / 4;
                    g2d.fillOval(tx + (TILE_SIZE - diameter) / 2, ty + (TILE_SIZE - diameter) / 2, diameter, diameter);
                }
            }
        } else {
            for (Move m : legalDemoMovesForSelected) {
                Square to = m.getTo();
                int tx = to.getCol() * TILE_SIZE + MARGIN;
                int ty = to.getRow() * TILE_SIZE + MARGIN;
                Piece destPiece = gameState.getBoard().getPiece(to);
                if (destPiece != null || m.isEnPassant()) {
                    g2d.setStroke(new BasicStroke(4));
                    g2d.drawOval(tx + 8, ty + 8, TILE_SIZE - 16, TILE_SIZE - 16);
                } else {
                    int diameter = TILE_SIZE / 4;
                    g2d.fillOval(tx + (TILE_SIZE - diameter) / 2, ty + (TILE_SIZE - diameter) / 2, diameter, diameter);
                }
            }
        }
        g2d.setStroke(oldStroke);

        // 7. Draw Piece Unicode Glyphs
        g2d.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 36)); // Reduced from 46
        FontMetrics fm = g2d.getFontMetrics();
        Board activeBoard = (lesson.getPracticeMode() == PracticeMode.FREE_MOVE) ? board : gameState.getBoard();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = activeBoard.getPiece(new Square(r, c));
                if (p != null) {
                    String symbol = p.getSymbol();
                    int textWidth = fm.stringWidth(symbol);
                    int textHeight = fm.getHeight();
                    int px = c * TILE_SIZE + MARGIN + (TILE_SIZE - textWidth) / 2;
                    int py = r * TILE_SIZE + MARGIN + (TILE_SIZE - textHeight) / 2 + fm.getAscent();

                    if (p.getColor() == PieceColor.WHITE) {
                        g2d.setColor(new Color(20, 20, 20, 160));
                        g2d.drawString(symbol, px + 1, py + 2);
                        g2d.setColor(Color.WHITE);
                        g2d.drawString(symbol, px, py);
                    } else {
                        g2d.setColor(new Color(240, 240, 240, 120));
                        g2d.drawString(symbol, px + 1, py + 1);
                        g2d.setColor(new Color(25, 25, 25));
                        g2d.drawString(symbol, px, py);
                    }
                }
            }
        }

        // 8. Draw file/rank text labels in margins
        g2d.setFont(new Font("Segoe UI Semibold", Font.BOLD, 11));
        g2d.setColor(TEXT_COLOR);
        FontMetrics marginFm = g2d.getFontMetrics();

        // Ranks
        for (int r = 0; r < 8; r++) {
            String rankStr = String.valueOf(8 - r);
            int textY = r * TILE_SIZE + MARGIN + (TILE_SIZE - marginFm.getHeight()) / 2 + marginFm.getAscent();
            int textXLeft = (MARGIN - marginFm.stringWidth(rankStr)) / 2;
            g2d.drawString(rankStr, textXLeft, textY);

            int textXRight = BOARD_SIZE + MARGIN + (MARGIN - marginFm.stringWidth(rankStr)) / 2;
            g2d.drawString(rankStr, textXRight, textY);
        }

        // Files
        for (int c = 0; c < 8; c++) {
            String fileStr = String.valueOf((char) ('a' + c));
            int textX = c * TILE_SIZE + MARGIN + (TILE_SIZE - marginFm.stringWidth(fileStr)) / 2;
            int textYTop = MARGIN - 6;
            g2d.drawString(fileStr, textX, textYTop);

            int textYBottom = BOARD_SIZE + MARGIN + marginFm.getAscent() + 3;
            g2d.drawString(fileStr, textX, textYBottom);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int col = (e.getX() - MARGIN) / TILE_SIZE;
        int row = (e.getY() - MARGIN) / TILE_SIZE;

        if (col < 0 || col >= 8 || row < 0 || row >= 8) {
            return;
        }

        Square clicked = new Square(row, col);

        if (lesson.getPracticeMode() == PracticeMode.FREE_MOVE) {
            handleFreeMoveClick(clicked);
        } else {
            handleGameRuleDemoClick(clicked);
        }
    }

    private void handleFreeMoveClick(Square clicked) {
        if (selectedSquare == null) {
            Piece p = board.getPiece(clicked);
            if (p != null) {
                selectedSquare = clicked;
                legalMovesForSelected = p.getLegalMoves(board, clicked);
                repaint();
            }
        } else {
            if (legalMovesForSelected.contains(clicked)) {
                Piece piece = board.getPiece(selectedSquare);
                board.setPiece(selectedSquare, null);
                board.setPiece(clicked, piece);
                if (piece != null) {
                    piece.setHasMoved(true);
                }
                lastMoveFrom = selectedSquare;
                lastMoveTo = clicked;

                selectedSquare = null;
                legalMovesForSelected = new ArrayList<>();
                repaint();
            } else {
                Piece p = board.getPiece(clicked);
                if (p != null) {
                    selectedSquare = clicked;
                    legalMovesForSelected = p.getLegalMoves(board, clicked);
                } else {
                    selectedSquare = null;
                    legalMovesForSelected = new ArrayList<>();
                }
                repaint();
            }
        }
    }

    private void handleGameRuleDemoClick(Square clicked) {
        PieceColor currentTurn = gameState.getCurrentTurn();

        if (selectedSquare == null) {
            Piece p = gameState.getBoard().getPiece(clicked);
            if (p != null && p.getColor() == currentTurn) {
                selectedSquare = clicked;
                List<Move> allMoves = gameState.generateAllLegalMoves(currentTurn);
                legalDemoMovesForSelected.clear();
                for (Move m : allMoves) {
                    if (m.getFrom().equals(clicked)) {
                        legalDemoMovesForSelected.add(m);
                    }
                }
                repaint();
            }
        } else {
            Move matchingMove = null;
            for (Move m : legalDemoMovesForSelected) {
                if (m.getTo().equals(clicked)) {
                    matchingMove = m;
                    break;
                }
            }

            if (matchingMove != null) {
                Class<? extends Piece> chosenClass = Queen.class;
                if (matchingMove.isPromotion()) {
                    Window parentWindow = SwingUtilities.getWindowAncestor(this);
                    JFrame parentFrame = (parentWindow instanceof JFrame) ? (JFrame) parentWindow : null;
                    PromotionDialog dialog = new PromotionDialog(parentFrame, currentTurn);
                    dialog.setVisible(true);
                    chosenClass = dialog.getChosenPieceClass();
                }

                gameState.applyMove(matchingMove);

                if (matchingMove.isPromotion()) {
                    Piece promotedPiece;
                    if (chosenClass == Queen.class) promotedPiece = new Queen(currentTurn);
                    else if (chosenClass == Rook.class) promotedPiece = new Rook(currentTurn);
                    else if (chosenClass == Knight.class) promotedPiece = new Knight(currentTurn);
                    else if (chosenClass == Bishop.class) promotedPiece = new Bishop(currentTurn);
                    else promotedPiece = new Queen(currentTurn);
                    gameState.getBoard().setPiece(matchingMove.getTo(), promotedPiece);
                    gameState.updateStatus();
                }

                lastMoveFrom = matchingMove.getFrom();
                lastMoveTo = matchingMove.getTo();
                selectedSquare = null;
                legalDemoMovesForSelected.clear();

                repaint();

                // Check for completion popups
                checkLessonObjectives(matchingMove);
            } else {
                Piece p = gameState.getBoard().getPiece(clicked);
                if (p != null && p.getColor() == currentTurn) {
                    selectedSquare = clicked;
                    List<Move> allMoves = gameState.generateAllLegalMoves(currentTurn);
                    legalDemoMovesForSelected.clear();
                    for (Move m : allMoves) {
                        if (m.getFrom().equals(clicked)) {
                            legalDemoMovesForSelected.add(m);
                        }
                    }
                } else {
                    selectedSquare = null;
                    legalDemoMovesForSelected.clear();
                }
                repaint();
            }
        }
    }

    private void checkLessonObjectives(Move appliedMove) {
        String title = lesson.getTitle().toLowerCase();

        if (title.contains("castling") && appliedMove.isCastling()) {
            JOptionPane.showMessageDialog(this, "Great! You successfully castled!", "Lesson Completed", JOptionPane.INFORMATION_MESSAGE);
        } else if (title.contains("en passant") && appliedMove.isEnPassant()) {
            JOptionPane.showMessageDialog(this, "Great! You captured en passant!", "Lesson Completed", JOptionPane.INFORMATION_MESSAGE);
        } else if (title.contains("promotion") && appliedMove.isPromotion()) {
            JOptionPane.showMessageDialog(this, "Great! You promoted your pawn!", "Lesson Completed", JOptionPane.INFORMATION_MESSAGE);
        } else if (title.contains("check") && !title.contains("mate")) {
            if (gameState.getStatus() == GameStatus.CHECK || gameState.getStatus() == GameStatus.CHECKMATE) {
                JOptionPane.showMessageDialog(this, "Excellent! You put the enemy King in check!", "Lesson Completed", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (title.contains("checkmate")) {
            if (gameState.getStatus() == GameStatus.CHECKMATE) {
                JOptionPane.showMessageDialog(this, "Checkmate! Well done, you have completed the checkmate lesson!", "Lesson Completed", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // Unused mouse listeners
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
