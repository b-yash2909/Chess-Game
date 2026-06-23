package model;

import java.util.Objects;

/**
 * Represents a square on the 8x8 chess board.
 */
public class Square {
    private final int row;
    private final int col;

    /**
     * Constructs a Square with a specific row and column.
     * row is 0-7 where 0 is 8th rank and 7 is 1st rank.
     * col is 0-7 where 0 is 'a' file and 7 is 'h' file.
     *
     * @param row row index (0-7)
     * @param col column index (0-7)
     */
    public Square(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Gets the row index.
     *
     * @return row index
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column index.
     *
     * @return column index
     */
    public int getCol() {
        return col;
    }

    /**
     * Compares this square to the specified object.
     *
     * @param o the object to compare to
     * @return true if the object is a square with same row and col, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Square square = (Square) o;
        return row == square.row && col == square.col;
    }

    /**
     * Returns a hash code for this square.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * Returns the algebraic notation for this square (e.g. "e4").
     *
     * @return algebraic notation
     */
    @Override
    public String toString() {
        char file = (char) ('a' + col);
        int rank = 8 - row;
        return "" + file + rank;
    }
}
