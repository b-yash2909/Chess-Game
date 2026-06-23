package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a King piece.
 */
public class King extends Piece {

    /**
     * Constructs a King.
     */
    public King(PieceColor color) {
        super(color);
    }

    @Override
    public String getSymbol() {
        return getColor() == PieceColor.WHITE ? "♔" : "♚";
    }

    @Override
    public boolean isValidMove(Board board, Square from, Square to) {
        if (from.equals(to)) return false;
        
        int dRow = Math.abs(to.getRow() - from.getRow());
        int dCol = Math.abs(to.getCol() - from.getCol());

        // Normal move
        if (dRow <= 1 && dCol <= 1) {
            Piece target = board.getPiece(to);
            return target == null || target.getColor() != getColor();
        }

        // Castling logic (pseudo-legal validation)
        if (!hasMoved() && dRow == 0 && dCol == 2) {
            int row = from.getRow();
            int colDir = to.getCol() > from.getCol() ? 1 : -1;
            
            // Check if squares between king and rook are empty
            int c = from.getCol() + colDir;
            while (c != 0 && c != 7) {
                if (board.getPiece(new Square(row, c)) != null) return false;
                c += colDir;
            }

            // Check if the rook hasn't moved
            Piece rook = board.getPiece(new Square(row, to.getCol() > from.getCol() ? 7 : 0));
            if (rook instanceof Rook && !rook.hasMoved() && rook.getColor() == getColor()) {
                // Must ensure king isn't currently in check or passing through check
                Square passThrough = new Square(row, from.getCol() + colDir);
                if (!board.isSquareAttacked(from, getColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE) &&
                    !board.isSquareAttacked(passThrough, getColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<Square> getLegalMoves(Board board, Square position) {
        List<Square> moves = new ArrayList<>();
        int[][] dirs = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
        for (int[] d : dirs) {
            int r = position.getRow() + d[0];
            int c = position.getCol() + d[1];
            if (board.isValidCoordinate(r, c)) {
                Square to = new Square(r, c);
                // Temporarily bypass castling in standard check loop to avoid infinite recursion
                Piece target = board.getPiece(to);
                if (target == null || target.getColor() != getColor()) {
                    moves.add(to);
                }
            }
        }
        
        // Add castling moves
        if (!hasMoved()) {
            Square ks = new Square(position.getRow(), position.getCol() + 2);
            if (isValidMove(board, position, ks)) moves.add(ks);
            Square qs = new Square(position.getRow(), position.getCol() - 2);
            if (isValidMove(board, position, qs)) moves.add(qs);
        }
        
        return moves;
    }

    @Override
    public Piece copy() {
        King k = new King(getColor());
        k.setHasMoved(hasMoved());
        return k;
    }
}
