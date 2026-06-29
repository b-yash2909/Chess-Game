package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the top-level state of the game.
 */
public class GameState {
    private Board board;
    private PieceColor currentTurn;
    private List<Move> moveHistory;
    private GameStatus status;

    /**
     * Initializes a new default GameState.
     */
    public GameState() {
        this.board = new Board();
        this.currentTurn = PieceColor.WHITE;
        this.moveHistory = new ArrayList<>();
        this.status = GameStatus.ONGOING;
        updateStatus();
    }

    /**
     * Copy constructor for simulation and AI.
     */
    private GameState(Board board, PieceColor currentTurn, List<Move> moveHistory, GameStatus status) {
        this.board = board.copy();
        this.currentTurn = currentTurn;
        this.moveHistory = new ArrayList<>(moveHistory);
        this.status = status;
    }

    /**
     * Gets the game board.
     */
    public Board getBoard() { return board; }

    /**
     * Gets the turn color.
     */
    public PieceColor getCurrentTurn() { return currentTurn; }
    
    /**
     * Gets the move history.
     */
    public List<Move> getMoveHistory() { return moveHistory; }
    
    /**
     * Gets the game status.
     */
    public GameStatus getStatus() { return status; }

    /**
     * Determines whether castling is legally specified.
     */
    private boolean isCastling(Square from, Square to, Piece piece) {
        return piece instanceof King && Math.abs(to.getCol() - from.getCol()) == 2;
    }

    /**
     * Determines whether en passant is legally specified.
     */
    private boolean isEnPassant(Square from, Square to, Piece piece) {
        return piece instanceof Pawn && from.getCol() != to.getCol() && board.getPiece(to) == null;
    }

    /**
     * Determines whether pawn promotion is legally specified.
     */
    private boolean isPromotion(Square to, Piece piece) {
        return piece instanceof Pawn && (to.getRow() == 0 || to.getRow() == 7);
    }

    /**
     * Applies a move to the game state. 
     * Assumes the move has already been validated as fully legal.
     */
    public void applyMove(Move move) {
        Square from = move.getFrom();
        Square to = move.getTo();
        Piece piece = move.getPieceMoved();

        // En Passant capture execution
        if (move.isEnPassant()) {
            board.setPiece(new Square(from.getRow(), to.getCol()), null);
        }

        // Apply Piece Move
        board.setPiece(to, piece);
        board.setPiece(from, null);

        // Castling execution (move rook)
        if (move.isCastling()) {
            int r = from.getRow();
            if (to.getCol() == 6) { // Kingside
                board.setPiece(new Square(r, 5), board.getPiece(new Square(r, 7)));
                board.setPiece(new Square(r, 7), null);
            } else if (to.getCol() == 2) { // Queenside
                board.setPiece(new Square(r, 3), board.getPiece(new Square(r, 0)));
                board.setPiece(new Square(r, 0), null);
            }
        }

        // Promotion execution
        if (move.isPromotion()) {
            board.setPiece(to, new Queen(piece.getColor()));
        }

        // Update En Passant target
        if (piece instanceof Pawn && Math.abs(to.getRow() - from.getRow()) == 2) {
            board.setEnPassantTarget(new Square((from.getRow() + to.getRow()) / 2, from.getCol()));
        } else {
            board.setEnPassantTarget(null);
        }

        piece.setHasMoved(true);

        // Update Clocks
        if (piece instanceof Pawn || move.getPieceCaptured() != null) {
            board.setHalfMoveClock(0);
        } else {
            board.setHalfMoveClock(board.getHalfMoveClock() + 1);
        }
        if (currentTurn == PieceColor.BLACK) {
            board.setFullMoveNumber(board.getFullMoveNumber() + 1);
        }

        // History and Turn Toggle
        moveHistory.add(move);
        currentTurn = (currentTurn == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
        
        updateStatus();
    }

    /**
     * Generates all fully legal moves (not resulting in own check) for a given color.
     */
    public List<Move> generateAllLegalMoves(PieceColor color) {
        List<Move> legalMoves = new ArrayList<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Square from = new Square(r, c);
                Piece piece = board.getPiece(from);
                if (piece != null && piece.getColor() == color) {
                    List<Square> pseudoLegals = piece.getLegalMoves(board, from);
                    for (Square to : pseudoLegals) {
                        Piece captured = board.getPiece(to);
                        boolean isCas = isCastling(from, to, piece);
                        boolean isEP = isEnPassant(from, to, piece);
                        boolean isPro = isPromotion(to, piece);
                        
                        Move m = new Move(from, to, piece, captured, isCas, isEP, isPro);
                        
                        // Simulate to check legal (no check left over)
                        GameState testState = this.copy();
                        testState.applyMoveSimulate(m);
                        if (!testState.getBoard().isInCheck(color)) {
                            legalMoves.add(m);
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    private void applyMoveSimulate(Move m) {
        Square from = m.getFrom();
        Square to = m.getTo();
        Piece p = board.getPiece(from);
        
        if (m.isEnPassant()) board.setPiece(new Square(from.getRow(), to.getCol()), null);
        board.setPiece(to, p);
        board.setPiece(from, null);
    }

    /**
     * Check if game over by status.
     * Updates status internally.
     */
    public void updateStatus() {
        if (board.getHalfMoveClock() >= 100) {
            status = GameStatus.DRAW; // 50 move rule (100 half moves)
            return;
        }
        
        List<Move> moves = generateAllLegalMoves(currentTurn);
        if (moves.isEmpty()) {
            if (board.isInCheck(currentTurn)) {
                status = GameStatus.CHECKMATE;
            } else {
                status = GameStatus.STALEMATE;
            }
        } else {
            if (board.isInCheck(currentTurn)) {
                status = GameStatus.CHECK;
            } else {
                status = GameStatus.ONGOING;
            }
        }
        
        // Insufficient material check omitted for brevity but can be added
    }

    /**
     * Creates a deep copy of the game state.
     */
    public GameState copy() {
        return new GameState(board, currentTurn, moveHistory, status);
    }

    /**
     * Sets the current player turn color.
     *
     * @param currentTurn the new player turn color
     */
    public void setCurrentTurn(PieceColor currentTurn) {
        this.currentTurn = currentTurn;
    }
}
