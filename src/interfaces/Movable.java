package interfaces;

import model.Board;
import model.Square;
import java.util.List;

/**
 * Ensures implementing classes provide mechanics for determining legal moves.
 */
public interface Movable {
    
    /**
     * Checks if a move from one square to another is valid on the given board.
     * 
     * @param board current state of the board
     * @param from starting square
     * @param to destination square
     * @return true if the move is valid for the piece, false otherwise
     */
    boolean isValidMove(Board board, Square from, Square to);

    /**
     * Retrieves all pseudo-legal moves for a piece at the given position.
     * Pseudo-legal means it doesn't account for if the king is left in check.
     *
     * @param board current state of the board
     * @param position current square of the piece
     * @return list of squares the piece can move to
     */
    List<Square> getLegalMoves(Board board, Square position);
}
