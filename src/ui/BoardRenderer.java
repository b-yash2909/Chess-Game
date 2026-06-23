package ui;

import model.Board;
import model.GameState;
import model.GameStatus;
import model.Move;
import model.Piece;
import model.Square;

import java.util.List;

/**
 * Handles rendering the chess board and game state to the console
 * using ANSI escape sequences.
 */
public class BoardRenderer {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_LIGHT_SQUARE = "\u001B[47m";
    public static final String ANSI_DARK_SQUARE = "\u001B[100m";
    public static final String ANSI_HIGHLIGHT = "\u001B[43m";
    public static final String ANSI_BLACK_TEXT = "\u001B[30m";

    /**
     * Renders the game state to standard output.
     * Clears the console first.
     *
     * @param state the current GameState
     */
    public void render(GameState state) {
        clearScreen();
        printBanner();
        printBoard(state);
        printLegend();
        printStatus(state);
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void printBanner() {
        System.out.println("╔══════════════════════════╗");
        System.out.println("║      CONSOLE CHESS       ║");
        System.out.println("║   Human (♙ ) vs AI (♟ )   ║");
        System.out.println("║  Type \"help\" for commands║");
        System.out.println("╚══════════════════════════╝\n");
    }

    private void printBoard(GameState state) {
        Board board = state.getBoard();
        List<Move> history = state.getMoveHistory();
        Square lastFrom = null;
        Square lastTo = null;

        if (!history.isEmpty()) {
            Move lastMove = history.get(history.size() - 1);
            lastFrom = lastMove.getFrom();
            lastTo = lastMove.getTo();
        }

        System.out.println("     a  b  c  d  e  f  g  h ");
        for (int r = 0; r < 8; r++) {
            System.out.print(" " + (8 - r) + "  ");
            for (int c = 0; c < 8; c++) {
                Square iterSquare = new Square(r, c);
                boolean isLight = (r + c) % 2 == 0;
                boolean isHighlight = iterSquare.equals(lastFrom) || iterSquare.equals(lastTo);

                String background = isHighlight ? ANSI_HIGHLIGHT : (isLight ? ANSI_LIGHT_SQUARE : ANSI_DARK_SQUARE);

                Piece p = board.getPiece(iterSquare);
                String symbol = (p == null) ? " " : p.getSymbol();

                // Print block: 3 characters wide
                System.out.print(background + ANSI_BLACK_TEXT + " " + symbol + " " + ANSI_RESET);
            }
            System.out.println("  " + (8 - r));
        }
        System.out.println("     a  b  c  d  e  f  g  h \n");
    }

    /**
     * Prints the Unicode piece legend.
     */
    public void printLegend() {
        System.out.println("White: King=♔  Queen=♕  Rook=♖  Bishop=♗  Knight=♘  Pawn=♙");
        System.out.println("Black: King=♚  Queen=♛  Rook=♜  Bishop=♝  Knight=♞  Pawn=♟");
        System.out.println();
    }

    /**
     * Prints context-aware status messages.
     */
    public void printStatus(GameState state) {
        if (state.getStatus() == GameStatus.CHECK) {
            System.out.println("!!! CHECK !!!");
        } else if (state.getStatus() == GameStatus.CHECKMATE) {
            System.out.println("!!! CHECKMATE !!! "
                    + (state.getCurrentTurn() == model.PieceColor.WHITE ? "Black" : "White") + " wins!");
        } else if (state.getStatus() == GameStatus.STALEMATE) {
            System.out.println("!!! STALEMATE !!! Game is a draw.");
        } else if (state.getStatus() == GameStatus.DRAW) {
            System.out.println("!!! DRAW !!! Fifty-move rule or insufficient material.");
        }
    }
}
