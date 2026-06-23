package engine;

import model.Board;
import model.GameState;
import model.GameStatus;
import model.Move;
import model.Piece;
import model.PieceColor;
import model.Square;
import model.Pawn;
import model.Knight;
import model.Bishop;
import model.Rook;
import model.Queen;
import model.King;

import java.util.List;
import java.util.Random;

/**
 * Minimax chess engine for AI-controlled moves.
 */
public class ChessEngine {
    private static final int DEPTH = 3;
    private final Random random = new Random();

    // Material Values
    private static final int PAWN_VAL = 100;
    private static final int KNIGHT_VAL = 320;
    private static final int BISHOP_VAL = 330;
    private static final int ROOK_VAL = 500;
    private static final int QUEEN_VAL = 900;
    private static final int KING_VAL = 20000;

    // Piece-Square Tables (White perspective)
    private static final int[][] PAWN_PST = {
        {0,  0,  0,  0,  0,  0,  0,  0},
        {50, 50, 50, 50, 50, 50, 50, 50},
        {10, 10, 20, 30, 30, 20, 10, 10},
        {5,  5, 10, 25, 25, 10,  5,  5},
        {0,  0,  0, 20, 20,  0,  0,  0},
        {5, -5,-10,  0,  0,-10, -5,  5},
        {5, 10, 10,-20,-20, 10, 10,  5},
        {0,  0,  0,  0,  0,  0,  0,  0}
    };

    private static final int[][] KNIGHT_PST = {
        {-50,-40,-30,-30,-30,-30,-40,-50},
        {-40,-20,  0,  0,  0,  0,-20,-40},
        {-30,  0, 10, 15, 15, 10,  0,-30},
        {-30,  5, 15, 20, 20, 15,  5,-30},
        {-30,  0, 15, 20, 20, 15,  0,-30},
        {-30,  5, 10, 15, 15, 10,  5,-30},
        {-40,-20,  0,  5,  5,  0,-20,-40},
        {-50,-40,-30,-30,-30,-30,-40,-50}
    };

    private static final int[][] BISHOP_PST = {
        {-20,-10,-10,-10,-10,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5, 10, 10,  5,  0,-10},
        {-10,  5,  5, 10, 10,  5,  5,-10},
        {-10,  0, 10, 10, 10, 10,  0,-10},
        {-10, 10, 10, 10, 10, 10, 10,-10},
        {-10,  5,  0,  0,  0,  0,  5,-10},
        {-20,-10,-10,-10,-10,-10,-10,-20}
    };

    private static final int[][] ROOK_PST = {
        {0,  0,  0,  0,  0,  0,  0,  0},
        {5, 10, 10, 10, 10, 10, 10,  5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {0,  0,  0,  5,  5,  0,  0,  0}
    };

    private static final int[][] QUEEN_PST = {
        {-20,-10,-10, -5, -5,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5,  5,  5,  5,  0,-10},
        { -5,  0,  5,  5,  5,  5,  0, -5},
        {  0,  0,  5,  5,  5,  5,  0, -5},
        {-10,  5,  5,  5,  5,  5,  0,-10},
        {-10,  0,  5,  0,  0,  0,  0,-10},
        {-20,-10,-10, -5, -5,-10,-10,-20}
    };

    private static final int[][] KING_PST = {
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-20,-30,-30,-40,-40,-30,-30,-20},
        {-10,-20,-20,-20,-20,-20,-20,-10},
        { 20, 20,  0,  0,  0,  0, 20, 20},
        { 20, 30, 10,  0,  0, 10, 30, 20}
    };

    /**
     * Finds the best move using Minimax with Alpha-Beta pruning.
     *
     * @param state the current GameState
     * @return the optimal move calculated
     */
    public Move getBestMove(GameState state) {
        List<Move> legalMoves = state.generateAllLegalMoves(state.getCurrentTurn());
        if (legalMoves.isEmpty()) return null;

        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;

        for (Move move : legalMoves) {
            GameState nextState = state.copy();
            nextState.applyMove(move);
            int boardValue = minimax(nextState, DEPTH - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

            if (boardValue > bestValue) {
                bestValue = boardValue;
                bestMove = move;
            }
        }
        return bestMove;
    }

    /**
     * Minimax algorithm with alpha-beta pruning.
     */
    private int minimax(GameState state, int depth, int alpha, int beta, boolean isMaximizingOptions) {
        if (depth == 0 || state.getStatus() == GameStatus.CHECKMATE || state.getStatus() == GameStatus.STALEMATE) {
            return evaluate(state);
        }

        List<Move> moves = state.generateAllLegalMoves(state.getCurrentTurn());

        if (isMaximizingOptions) { // Black's perspective
            int maxEval = Integer.MIN_VALUE;
            for (Move move : moves) {
                GameState nextState = state.copy();
                nextState.applyMove(move);
                int eval = minimax(nextState, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        } else { // White's perspective
            int minEval = Integer.MAX_VALUE;
            for (Move move : moves) {
                GameState nextState = state.copy();
                nextState.applyMove(move);
                int eval = minimax(nextState, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }

    /**
     * Evaluates the board materially and positionally.
     * Returns a positive score if Black is leading, negative if White is.
     */
    private int evaluate(GameState state) {
        if (state.getStatus() == GameStatus.CHECKMATE) {
            return state.getCurrentTurn() == PieceColor.WHITE ? 100000 : -100000;
        }
        if (state.getStatus() == GameStatus.STALEMATE || state.getStatus() == GameStatus.DRAW) {
            return 0;
        }

        int scoreWhite = 0;
        int scoreBlack = 0;
        Board board = state.getBoard();

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPiece(new Square(r, c));
                if (p != null) {
                    int val = getPieceValue(p);
                    int pst = getPSTValue(p, r, c);
                    if (p.getColor() == PieceColor.WHITE) {
                        scoreWhite += val + pst;
                    } else {
                        scoreBlack += val + pst;
                    }
                }
            }
        }
        
        // Random tiebreak jitter +- 5
        int jitter = random.nextInt(11) - 5;
        
        // Maximizing player is BLACK
        return scoreBlack - scoreWhite + jitter;
    }

    private int getPieceValue(Piece p) {
        if (p instanceof Pawn) return PAWN_VAL;
        if (p instanceof Knight) return KNIGHT_VAL;
        if (p instanceof Bishop) return BISHOP_VAL;
        if (p instanceof Rook) return ROOK_VAL;
        if (p instanceof Queen) return QUEEN_VAL;
        if (p instanceof King) return KING_VAL;
        return 0;
    }

    private int getPSTValue(Piece p, int row, int col) {
        // PST assumes White at bottom (rows 6,7)
        // For Black, we mirror the row
        int mapRow = (p.getColor() == PieceColor.WHITE) ? row : 7 - row;
        
        if (p instanceof Pawn) return PAWN_PST[mapRow][col];
        if (p instanceof Knight) return KNIGHT_PST[mapRow][col];
        if (p instanceof Bishop) return BISHOP_PST[mapRow][col];
        if (p instanceof Rook) return ROOK_PST[mapRow][col];
        if (p instanceof Queen) return QUEEN_PST[mapRow][col];
        if (p instanceof King) return KING_PST[mapRow][col];
        return 0;
    }
}
