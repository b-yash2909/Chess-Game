package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Pawn piece.
 */
public class Pawn extends Piece {

    /**
     * Constructs a Pawn with the specified color.
     *
     * @param color the color of the pawn
     */
    public Pawn(PieceColor color) {
        super(color);
    }

    @Override
    public String getSymbol() {
        return getColor() == PieceColor.WHITE ? "♙" : "♟";
    }

    @Override
    public boolean isValidMove(Board board, Square from, Square to) {
        int dir = (getColor() == PieceColor.WHITE) ? -1 : 1;
        int dRow = to.getRow() - from.getRow();
        int dCol = Math.abs(to.getCol() - from.getCol());

        Piece targetPiece = board.getPiece(to);

        // Forward move
        if (dCol == 0) {
            // One square forward
            if (dRow == dir) {
                return targetPiece == null;
            }
            // Two squares forward
            if (dRow == 2 * dir && !hasMoved()) {
                Square intermediate = new Square(from.getRow() + dir, from.getCol());
                return targetPiece == null && board.getPiece(intermediate) == null;
            }
        }
        // Capture move (diagonal)
        else if (dCol == 1 && dRow == dir) {
            if (targetPiece != null && targetPiece.getColor() != getColor()) {
                return true;
            }
            // En passant
            Square epTarget = board.getEnPassantTarget();
            if (epTarget != null && epTarget.equals(to)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Square> getLegalMoves(Board board, Square position) {
        List<Square> moves = new ArrayList<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Square to = new Square(r, c);
                if (isValidMove(board, position, to)) {
                    moves.add(to);
                }
            }
        }
        return moves;
    }

    @Override
    public Piece copy() {
        Pawn p = new Pawn(getColor());
        p.setHasMoved(hasMoved());
        return p;
    }
}
