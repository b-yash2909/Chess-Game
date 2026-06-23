package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Knight piece.
 */
public class Knight extends Piece {

    /**
     * Constructs a Knight.
     *
     * @param color color of the knight
     */
    public Knight(PieceColor color) {
        super(color);
    }

    @Override
    public String getSymbol() {
        return getColor() == PieceColor.WHITE ? "♘" : "♞";
    }

    @Override
    public boolean isValidMove(Board board, Square from, Square to) {
        int dRow = Math.abs(to.getRow() - from.getRow());
        int dCol = Math.abs(to.getCol() - from.getCol());
        
        if ((dRow == 2 && dCol == 1) || (dRow == 1 && dCol == 2)) {
            Piece targetObj = board.getPiece(to);
            return targetObj == null || targetObj.getColor() != this.getColor();
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
        Knight k = new Knight(getColor());
        k.setHasMoved(hasMoved());
        return k;
    }
}
