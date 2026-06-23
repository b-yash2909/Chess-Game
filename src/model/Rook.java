package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Rook piece.
 */
public class Rook extends Piece {

    /**
     * Constructs a Rook.
     */
    public Rook(PieceColor color) {
        super(color);
    }

    @Override
    public String getSymbol() {
        return getColor() == PieceColor.WHITE ? "♖" : "♜";
    }

    @Override
    public boolean isValidMove(Board board, Square from, Square to) {
        if (from.getRow() != to.getRow() && from.getCol() != to.getCol()) return false;
        if (from.equals(to)) return false;

        int rowDir = Integer.compare(to.getRow(), from.getRow());
        int colDir = Integer.compare(to.getCol(), from.getCol());

        int r = from.getRow() + rowDir;
        int c = from.getCol() + colDir;

        while (r != to.getRow() || c != to.getCol()) {
            if (board.getPiece(new Square(r, c)) != null) return false;
            r += rowDir;
            c += colDir;
        }

        Piece target = board.getPiece(to);
        return target == null || target.getColor() != getColor();
    }

    @Override
    public List<Square> getLegalMoves(Board board, Square position) {
        List<Square> moves = new ArrayList<>();
        int[][] dirs = {{-1,0}, {1,0}, {0,-1}, {0,1}};
        for (int[] d : dirs) {
            int r = position.getRow() + d[0];
            int c = position.getCol() + d[1];
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
                r += d[0];
                c += d[1];
            }
        }
        return moves;
    }

    @Override
    public Piece copy() {
        Rook r = new Rook(getColor());
        r.setHasMoved(hasMoved());
        return r;
    }
}
