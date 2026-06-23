package model;

/**
 * Represents a chess move.
 */
public class Move {
    private final Square from;
    private final Square to;
    private final Piece pieceMoved;
    private final Piece pieceCaptured;
    private final boolean isCastling;
    private final boolean isEnPassant;
    private final boolean isPromotion;
    
    /**
     * Constructs a move.
     * 
     * @param from the starting square
     * @param to the destination square
     * @param pieceMoved the piece being moved
     * @param pieceCaptured the piece being captured (null if none)
     * @param isCastling true if this move is castling
     * @param isEnPassant true if this move is an en passant capture
     * @param isPromotion true if this move involves pawn promotion
     */
    public Move(Square from, Square to, Piece pieceMoved, Piece pieceCaptured, 
                boolean isCastling, boolean isEnPassant, boolean isPromotion) {
        this.from = from;
        this.to = to;
        this.pieceMoved = pieceMoved;
        this.pieceCaptured = pieceCaptured;
        this.isCastling = isCastling;
        this.isEnPassant = isEnPassant;
        this.isPromotion = isPromotion;
    }

    /**
     * @return the starting square
     */
    public Square getFrom() { return from; }

    /**
     * @return the destination square
     */
    public Square getTo() { return to; }

    /**
     * @return the piece moved
     */
    public Piece getPieceMoved() { return pieceMoved; }

    /**
     * @return the piece captured, if any
     */
    public Piece getPieceCaptured() { return pieceCaptured; }

    /**
     * @return true if castling move
     */
    public boolean isCastling() { return isCastling; }

    /**
     * @return true if en passant move
     */
    public boolean isEnPassant() { return isEnPassant; }

    /**
     * @return true if pawn promotion
     */
    public boolean isPromotion() { return isPromotion; }

    /**
     * Returns the algebraic notation of the move.
     */
    @Override
    public String toString() {
        if (isCastling) {
            return to.getCol() == 6 ? "O-O" : "O-O-O";
        }
        String promoStr = isPromotion ? "=Q" : "";
        return from.toString() + (pieceCaptured != null ? "x" : "-") + to.toString() + promoStr;
    }
}
