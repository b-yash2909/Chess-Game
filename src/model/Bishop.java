package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Bishop piece.
 */
public class Bishop extends Piece {

    /**
     * Constructs a Bishop.
     */
    public Bishop(PieceColor color) {
        super(color);
    }

    @Override
    public String getSymbol() {
        return getColor() == PieceColor.WHITE ? "♗" : "♝";
    }

    @Override
    public boolean isValidMove(Board board, Square from, Square to) {
        int dRow = Math.abs(to.getRow() - from.getRow());
        int dCol = Math.abs(to.getCol() - from.getCol());

        if (dRow != dCol || dRow == 0) return false;

        int rowDir = (to.getRow() - from.getRow()) > 0 ? 1 : -1;
        int colDir = (to.getCol() - from.getCol()) > 0 ? 1 : -1;

        int r = from.getRow() + rowDir;
        int c = from.getCol() + colDir;
        while (r != to.getRow()) {
            if (board.getPiece(new Square(r, c)) != null) {
                return false; // blocked
            }
            r += rowDir;
            c += colDir;
        }

        Piece target = board.getPiece(to);
        return target == null || target.getColor() != getColor();
    }

    @Override
    public List<Square> getLegalMoves(Board board, Square position) {
        List<Square> moves = new ArrayList<>();
        int[] dirs = {-1, 1};
        for (int rDir : dirs) {
            for (int cDir : dirs) {
                int r = position.getRow() + rDir;
                int c = position.getCol() + cDir;
                while (board.isValidCoordinate(r, c)) {
                    Square to = new Square(r, c);
                    Piece target = board.getPiece(to);
                    if (target == null) {
                        moves.add(to);
                    } else {
                        if (target.getColor() != getColor()) {
                            moves.add(to);
                        }
                        break;
                    }
                    r += rDir;
                    c += cDir;
                }
            }
        }
        return moves;
    }

    @Override
    public Piece copy() {
        Bishop b = new Bishop(getColor());
        b.setHasMoved(hasMoved());
        return b;
    }
}
