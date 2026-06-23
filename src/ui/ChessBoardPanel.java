package ui;

import model.*;
import engine.ChessEngine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Visual renderer for the chess board, pieces, overlays, and user interaction.
 * Styled with a modern emerald-and-sage theme, outer margins for coordinates,
 * sound integrations, and history undo tracking.
 */
public class ChessBoardPanel extends JPanel implements MouseListener {
    // CONSTANTS
    public static final int TILE_SIZE = 80;
    public static final int MARGIN = 26; // Margin around the board for file/rank letters
    public static final int BOARD_SIZE = TILE_SIZE * 8;
    
    // Modern Chess Theme Colors (Emerald Green & Light Sage)
    public static final Color LIGHT_SQUARE = new Color(238, 238, 210);
    public static final Color DARK_SQUARE = new Color(118, 150, 86);
    public static final Color SELECTED_COLOR = new Color(247, 247, 105, 170);
    public static final Color LEGAL_MOVE_COLOR = new Color(0, 0, 0, 35);
    public static final Color LAST_MOVE_COLOR = new Color(186, 202, 43, 130);
    public static final Color CHECK_COLOR = new Color(240, 98, 146, 180);
    public static final Color HINT_COLOR = new Color(30, 144, 255, 140);
    public static final Color MARGIN_BG = new Color(22, 26, 30);
    public static final Color TEXT_COLOR = new Color(175, 188, 202);

    // FIELDS
    private GameState gameState;
    private final ChessFrame frame;
    private final GameMode gameMode;
    private Square selectedSquare;
    private List<Move> legalMovesForSelected;
    private Move lastMove;
    private boolean gameOver;
    private boolean aiThinking;
    
    // Undo & Hint history fields
    private final List<GameState> history;
    private Move hintMove;
    private boolean hintThinking;

    /**
     * Constructs a ChessBoardPanel.
     */
    public ChessBoardPanel(GameState gameState, ChessFrame frame, GameMode mode) {
        this.gameState = gameState;
        this.frame = frame;
        this.gameMode = mode;
        this.selectedSquare = null;
        this.legalMovesForSelected = new ArrayList<>();
        this.lastMove = null;
        this.gameOver = false;
        this.aiThinking = false;
        
        this.history = new ArrayList<>();
        this.hintMove = null;
        this.hintThinking = false;

        // Panel sizing includes the outer margin on all sides
        int totalSize = BOARD_SIZE + 2 * MARGIN;
        setPreferredSize(new Dimension(totalSize, totalSize));
        addMouseListener(this);
    }

    /**
     * Resets the board panel with a new game state.
     */
    public void reset(GameState newState) {
        this.gameState = newState;
        this.selectedSquare = null;
        this.legalMovesForSelected = new ArrayList<>();
        this.lastMove = null;
        this.gameOver = false;
        this.aiThinking = false;
        this.history.clear();
        this.hintMove = null;
        this.hintThinking = false;
        repaint();
        frame.setStatus(gameMode == GameMode.HUMAN_VS_HUMAN ? "White's turn" : "Your turn — White");
        frame.updateMoveLog();
    }

    /**
     * Rolls back the game state to the previous turn.
     */
    public void undoLastMove() {
        if (aiThinking || hintThinking || history.isEmpty()) {
            return;
        }

        // In vs AI mode, we pop twice to revert both the AI's move and player's move
        if (gameMode == GameMode.HUMAN_VS_COMPUTER) {
            if (history.size() >= 2) {
                gameState = history.remove(history.size() - 1); // Discard AI's state
                gameState = history.remove(history.size() - 1); // Restore player's previous state
            } else {
                // If only 1 state exists, restore it (resets to start)
                gameState = history.remove(0);
            }
        } else {
            gameState = history.remove(history.size() - 1); // PvP mode pops once
        }

        selectedSquare = null;
        legalMovesForSelected.clear();
        hintMove = null;
        gameOver = false;

        // Restore last move indicator
        if (!gameState.getMoveHistory().isEmpty()) {
            lastMove = gameState.getMoveHistory().get(gameState.getMoveHistory().size() - 1);
        } else {
            lastMove = null;
        }

        repaint();
        checkGameOver();
        frame.updateMoveLog();
    }

    /**
     * Triggers the AI Hint calculation in a background thread.
     */
    public void triggerHint() {
        if (gameOver || aiThinking || hintThinking) return;
        hintThinking = true;
        frame.setStatus("Calculating best hint...");

        SwingWorker<Move, Void> worker = new SwingWorker<Move, Void>() {
            @Override
            protected Move doInBackground() throws Exception {
                ChessEngine engine = new ChessEngine();
                return engine.getBestMove(gameState);
            }

            @Override
            protected void done() {
                try {
                    hintMove = get();
                    hintThinking = false;
                    if (hintMove != null) {
                        frame.setStatus("Hint: Move piece from " + hintMove.getFrom() + " to " + hintMove.getTo());
                        repaint();
                    } else {
                        frame.setStatus("No suitable hint found.");
                    }
                } catch (Exception ex) {
                    hintThinking = false;
                    frame.setStatus("Error calculating hint.");
                }
            }
        };
        worker.execute();
    }

    public GameState getGameState() {
        return gameState;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 1. Fill entire panel background
        g2d.setColor(MARGIN_BG);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // 2. Draw Chess board squares
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                boolean isLight = (r + c) % 2 == 0;
                g2d.setColor(isLight ? LIGHT_SQUARE : DARK_SQUARE);
                g2d.fillRect(c * TILE_SIZE + MARGIN, r * TILE_SIZE + MARGIN, TILE_SIZE, TILE_SIZE);
            }
        }

        // 3. Last move highlight
        if (lastMove != null) {
            g2d.setColor(LAST_MOVE_COLOR);
            Square from = lastMove.getFrom();
            Square to = lastMove.getTo();
            g2d.fillRect(from.getCol() * TILE_SIZE + MARGIN, from.getRow() * TILE_SIZE + MARGIN, TILE_SIZE, TILE_SIZE);
            g2d.fillRect(to.getCol() * TILE_SIZE + MARGIN, to.getRow() * TILE_SIZE + MARGIN, TILE_SIZE, TILE_SIZE);
        }

        // 4. Selected square highlight
        if (selectedSquare != null) {
            g2d.setColor(SELECTED_COLOR);
            g2d.fillRect(selectedSquare.getCol() * TILE_SIZE + MARGIN, selectedSquare.getRow() * TILE_SIZE + MARGIN, TILE_SIZE, TILE_SIZE);
        }

        // 5. King in check highlight
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

        // 6. Hint highlight
        if (hintMove != null) {
            g2d.setColor(HINT_COLOR);
            Square from = hintMove.getFrom();
            Square to = hintMove.getTo();
            g2d.fillRect(from.getCol() * TILE_SIZE + MARGIN, from.getRow() * TILE_SIZE + MARGIN, TILE_SIZE, TILE_SIZE);
            g2d.fillRect(to.getCol() * TILE_SIZE + MARGIN, to.getRow() * TILE_SIZE + MARGIN, TILE_SIZE, TILE_SIZE);
        }

        // 7. Legal move dots/indicators
        if (legalMovesForSelected != null && !legalMovesForSelected.isEmpty()) {
            Stroke oldStroke = g2d.getStroke();
            g2d.setColor(LEGAL_MOVE_COLOR);
            for (Move m : legalMovesForSelected) {
                Square to = m.getTo();
                int tx = to.getCol() * TILE_SIZE + MARGIN;
                int ty = to.getRow() * TILE_SIZE + MARGIN;

                Piece destPiece = gameState.getBoard().getPiece(to);
                if (destPiece != null || m.isEnPassant()) {
                    // Hollow circle for capture options
                    g2d.setStroke(new BasicStroke(5));
                    g2d.drawOval(tx + 8, ty + 8, TILE_SIZE - 16, TILE_SIZE - 16);
                } else {
                    // Solid dot for empty square moves
                    int diameter = TILE_SIZE / 4;
                    int cx = tx + (TILE_SIZE - diameter) / 2;
                    int cy = ty + (TILE_SIZE - diameter) / 2;
                    g2d.fillOval(cx, cy, diameter, diameter);
                }
            }
            g2d.setStroke(oldStroke);
        }

        // 8. Draw pieces (Unicode Glyph Styling)
        g2d.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 54));
        FontMetrics fm = g2d.getFontMetrics();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = gameState.getBoard().getPiece(new Square(r, c));
                if (p != null) {
                    String symbol = p.getSymbol();
                    int textWidth = fm.stringWidth(symbol);
                    int textHeight = fm.getHeight();
                    int px = c * TILE_SIZE + MARGIN + (TILE_SIZE - textWidth) / 2;
                    int py = r * TILE_SIZE + MARGIN + (TILE_SIZE - textHeight) / 2 + fm.getAscent();

                    if (p.getColor() == PieceColor.WHITE) {
                        // Shadow
                        g2d.setColor(new Color(20, 20, 20, 160));
                        g2d.drawString(symbol, px + 1, py + 2);
                        // Piece Fill
                        g2d.setColor(Color.WHITE);
                        g2d.drawString(symbol, px, py);
                    } else {
                        // Highlight/Shadow
                        g2d.setColor(new Color(240, 240, 240, 120));
                        g2d.drawString(symbol, px + 1, py + 1);
                        // Piece Fill
                        g2d.setColor(new Color(25, 25, 25));
                        g2d.drawString(symbol, px, py);
                    }
                }
            }
        }

        // 9. Render files & ranks markers in the outer margin
        g2d.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        g2d.setColor(TEXT_COLOR);
        FontMetrics marginFm = g2d.getFontMetrics();

        // Draw ranks (1-8) along left and right margins
        for (int r = 0; r < 8; r++) {
            String rankStr = String.valueOf(8 - r);
            int textY = r * TILE_SIZE + MARGIN + (TILE_SIZE - marginFm.getHeight()) / 2 + marginFm.getAscent();
            
            // Left Margin
            int textXLeft = (MARGIN - marginFm.stringWidth(rankStr)) / 2;
            g2d.drawString(rankStr, textXLeft, textY);
            
            // Right Margin
            int textXRight = BOARD_SIZE + MARGIN + (MARGIN - marginFm.stringWidth(rankStr)) / 2;
            g2d.drawString(rankStr, textXRight, textY);
        }

        // Draw files (a-h) along top and bottom margins
        for (int c = 0; c < 8; c++) {
            String fileStr = String.valueOf((char) ('a' + c));
            int textX = c * TILE_SIZE + MARGIN + (TILE_SIZE - marginFm.stringWidth(fileStr)) / 2;
            
            // Top Margin
            int textYTop = MARGIN - 7;
            g2d.drawString(fileStr, textX, textYTop);
            
            // Bottom Margin
            int textYBottom = BOARD_SIZE + MARGIN + marginFm.getAscent() + 4;
            g2d.drawString(fileStr, textX, textYBottom);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameOver || aiThinking || hintThinking
                || (gameMode == GameMode.HUMAN_VS_COMPUTER && gameState.getCurrentTurn() != PieceColor.WHITE)) {
            return;
        }

        // Adjust mouse coordinate to take margin into account
        int col = (e.getX() - MARGIN) / TILE_SIZE;
        int row = (e.getY() - MARGIN) / TILE_SIZE;

        if (col < 0 || col >= 8 || row < 0 || row >= 8) {
            return;
        }

        Square clicked = new Square(row, col);
        PieceColor currentTurn = gameState.getCurrentTurn();

        if (selectedSquare == null) {
            // First click: select a piece of own color
            Piece p = gameState.getBoard().getPiece(clicked);
            if (p != null && p.getColor() == currentTurn) {
                selectedSquare = clicked;
                List<Move> allMoves = gameState.generateAllLegalMoves(currentTurn);
                legalMovesForSelected.clear();
                for (Move m : allMoves) {
                    if (m.getFrom().equals(clicked)) {
                        legalMovesForSelected.add(m);
                    }
                }
                repaint();
            }
        } else {
            // Second click: choose destination
            Move matchingMove = null;
            for (Move m : legalMovesForSelected) {
                if (m.getTo().equals(clicked)) {
                    matchingMove = m;
                    break;
                }
            }

            if (matchingMove != null) {
                // Record current state to history before committing the move
                history.add(gameState.copy());

                Class<? extends Piece> chosenClass = Queen.class;
                if (matchingMove.isPromotion()) {
                    PromotionDialog dialog = new PromotionDialog(frame, currentTurn);
                    dialog.setVisible(true);
                    chosenClass = dialog.getChosenPieceClass();
                }

                gameState.applyMove(matchingMove);

                if (matchingMove.isPromotion()) {
                    Piece promotedPiece;
                    if (chosenClass == Queen.class) {
                        promotedPiece = new Queen(currentTurn);
                    } else if (chosenClass == Rook.class) {
                        promotedPiece = new Rook(currentTurn);
                    } else if (chosenClass == Knight.class) {
                        promotedPiece = new Knight(currentTurn);
                    } else if (chosenClass == Bishop.class) {
                        promotedPiece = new Bishop(currentTurn);
                    } else {
                        promotedPiece = new Queen(currentTurn);
                    }
                    gameState.getBoard().setPiece(matchingMove.getTo(), promotedPiece);
                    gameState.updateStatus();
                }

                lastMove = matchingMove;
                selectedSquare = null;
                legalMovesForSelected.clear();
                hintMove = null; // Clear active hint

                // Play Move Audio
                triggerMoveSound(matchingMove);

                repaint();
                checkGameOver();
                frame.updateMoveLog();

                if (!gameOver && gameMode == GameMode.HUMAN_VS_COMPUTER) {
                    triggerAIMove();
                }
            } else {
                // Clicking own different piece swaps selection
                Piece p = gameState.getBoard().getPiece(clicked);
                if (p != null && p.getColor() == currentTurn) {
                    selectedSquare = clicked;
                    List<Move> allMoves = gameState.generateAllLegalMoves(currentTurn);
                    legalMovesForSelected.clear();
                    for (Move m : allMoves) {
                        if (m.getFrom().equals(clicked)) {
                            legalMovesForSelected.add(m);
                        }
                    }
                    repaint();
                } else {
                    // Click empty/invalid square clears selection
                    selectedSquare = null;
                    legalMovesForSelected.clear();
                    repaint();
                }
            }
        }
    }

    private void triggerMoveSound(Move move) {
        if (gameState.getStatus() == GameStatus.CHECKMATE) {
            SoundManager.playGameOver();
        } else if (gameState.getStatus() == GameStatus.CHECK) {
            SoundManager.playCheck();
        } else if (move.getPieceCaptured() != null || move.isEnPassant()) {
            SoundManager.playCapture();
        } else {
            SoundManager.playMove();
        }
    }

    private String turnLabel() {
        return gameState.getCurrentTurn() == PieceColor.WHITE ? "White" : "Black";
    }

    private void checkGameOver() {
        GameStatus status = gameState.getStatus();
        if (status == GameStatus.CHECKMATE) {
            gameOver = true;
            String winner;
            if (gameMode == GameMode.HUMAN_VS_COMPUTER) {
                winner = (gameState.getCurrentTurn() == PieceColor.WHITE) ? "Computer (Black)" : "Player (White)";
            } else {
                winner = (gameState.getCurrentTurn() == PieceColor.WHITE) ? "Black" : "White";
            }
            repaint();
            JOptionPane.showMessageDialog(frame, "Checkmate! " + winner + " wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        } else if (status == GameStatus.STALEMATE) {
            gameOver = true;
            repaint();
            JOptionPane.showMessageDialog(frame, "Stalemate! Game is a draw.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        } else if (status == GameStatus.DRAW) {
            gameOver = true;
            repaint();
            JOptionPane.showMessageDialog(frame, "Draw! Fifty-move rule or insufficient material.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        } else if (status == GameStatus.CHECK) {
            if (gameMode == GameMode.HUMAN_VS_HUMAN) {
                frame.setStatus("Check! — " + turnLabel() + "'s turn");
            } else {
                frame.setStatus("Check! — " + (gameState.getCurrentTurn() == PieceColor.WHITE ? "Your turn — White" : "Computer is thinking..."));
            }
        } else if (status == GameStatus.ONGOING) {
            if (gameMode == GameMode.HUMAN_VS_HUMAN) {
                frame.setStatus(turnLabel() + "'s turn");
            } else {
                frame.setStatus(gameState.getCurrentTurn() == PieceColor.WHITE ? "Your turn — White" : "Computer is thinking...");
            }
        }
    }

    private void triggerAIMove() {
        aiThinking = true;
        frame.setStatus("Computer is thinking...");

        SwingWorker<Move, Void> worker = new SwingWorker<Move, Void>() {
            @Override
            protected Move doInBackground() throws Exception {
                // Short sleep for realism
                Thread.sleep(350);
                ChessEngine engine = new ChessEngine();
                return engine.getBestMove(gameState);
            }

            @Override
            protected void done() {
                try {
                    Move aiMove = get();
                    if (aiMove != null) {
                        // Record pre-AI state to history
                        history.add(gameState.copy());
                        
                        gameState.applyMove(aiMove);
                        lastMove = aiMove;

                        // Play sound matching action
                        triggerMoveSound(aiMove);
                    }
                    gameState.updateStatus();
                    aiThinking = false;
                    repaint();
                    checkGameOver();
                    frame.updateMoveLog();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    aiThinking = false;
                    frame.setStatus("Error occurred during AI turn.");
                }
            }
        };
        worker.execute();
    }

    // UNUSED MOUSE LISTENER CALLS
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}

