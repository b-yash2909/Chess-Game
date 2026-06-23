package model;

import interfaces.Movable;

/**
 * Represents a generic chess piece.
 */
public abstract class Piece implements Movable {
    private final PieceColor color;
    private boolean hasMoved;

    /**
     * Constructs a Piece with the specified color.
     *
     * @param color the color of the piece
     */
    public Piece(PieceColor color) {
        this.color = color;
        this.hasMoved = false;
    }

    /**
     * Gets the color of the piece.
     *
     * @return the color
     */
    public PieceColor getColor() {
        return color;
    }

    /**
     * Checks whether the piece has moved from its initial position.
     *
     * @return true if piece has moved, false otherwise
     */
    public boolean hasMoved() {
        return hasMoved;
    }

    /**
     * Sets the hasMoved state.
     *
     * @param hasMoved the new state
     */
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    /**
     * Gets the Unicode symbol for the piece based on its type and color.
     *
     * @return the unicode string representation of the piece
     */
    public abstract String getSymbol();
    
    /**
     * Creates a deep copy of this piece.
     *
     * @return copied piece
     */
    public abstract Piece copy();
}
