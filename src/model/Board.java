package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents the 8x8 chess board.
 */
public class Board {
    private Piece[][] grid;
    private Square enPassantTarget;
    // 0: White Kingside, 1: White Queenside, 2: Black Kingside, 3: Black Queenside
    private boolean[] castlingRights;
    private int halfMoveClock;
    private int fullMoveNumber;

    /**
     * Constructs a new Board with standard starting positions.
     */
    public Board() {
        grid = new Piece[8][8];
        castlingRights = new boolean[]{true, true, true, true};
        halfMoveClock = 0;
        fullMoveNumber = 1;
        enPassantTarget = null;
        setupBoard();
    }

    /**
     * Private constructor for cloning.
     */
    private Board(Piece[][] grid, Square enPassantTarget, boolean[] castlingRights, 
                  int halfMoveClock, int fullMoveNumber) {
        this.grid = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (grid[i][j] != null) {
                    this.grid[i][j] = grid[i][j].copy();
                }
            }
        }
        this.enPassantTarget = enPassantTarget == null ? null : new Square(enPassantTarget.getRow(), enPassantTarget.getCol());
        this.castlingRights = Arrays.copyOf(castlingRights, castlingRights.length);
        this.halfMoveClock = halfMoveClock;
        this.fullMoveNumber = fullMoveNumber;
    }

    private void setupBoard() {
        // Black pieces
        grid[0][0] = new Rook(PieceColor.BLACK);
        grid[0][1] = new Knight(PieceColor.BLACK);
        grid[0][2] = new Bishop(PieceColor.BLACK);
        grid[0][3] = new Queen(PieceColor.BLACK);
        grid[0][4] = new King(PieceColor.BLACK);
        grid[0][5] = new Bishop(PieceColor.BLACK);
        grid[0][6] = new Knight(PieceColor.BLACK);
        grid[0][7] = new Rook(PieceColor.BLACK);
        for (int i = 0; i < 8; i++) grid[1][i] = new Pawn(PieceColor.BLACK);

        // White pieces
        grid[7][0] = new Rook(PieceColor.WHITE);
        grid[7][1] = new Knight(PieceColor.WHITE);
        grid[7][2] = new Bishop(PieceColor.WHITE);
        grid[7][3] = new Queen(PieceColor.WHITE);
        grid[7][4] = new King(PieceColor.WHITE);
        grid[7][5] = new Bishop(PieceColor.WHITE);
        grid[7][6] = new Knight(PieceColor.WHITE);
        grid[7][7] = new Rook(PieceColor.WHITE);
        for (int i = 0; i < 8; i++) grid[6][i] = new Pawn(PieceColor.WHITE);
    }

    /**
     * Gets the piece at a given square.
     *
     * @param sq the square
     * @return the piece, or null if empty
     */
    public Piece getPiece(Square sq) {
        if (!isValidCoordinate(sq.getRow(), sq.getCol())) return null;
        return grid[sq.getRow()][sq.getCol()];
    }

    /**
     * Sets a piece at a given square.
     *
     * @param sq the square
     * @param piece the piece
     */
    public void setPiece(Square sq, Piece piece) {
        if (isValidCoordinate(sq.getRow(), sq.getCol())) {
            grid[sq.getRow()][sq.getCol()] = piece;
        }
    }

    /**
     * Determines if given coordinates are within the board bounds.
     *
     * @param row sequence row
     * @param col sequence col
     * @return true if valid
     */
    public boolean isValidCoordinate(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    /**
     * Checks if the square is attacked by any piece of the specified color.
     *
     * @param sq the square to check
     * @param attackerColor the attacking piece color
     * @return true if attacked
     */
    public boolean isSquareAttacked(Square sq, PieceColor attackerColor) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                if (p != null && p.getColor() == attackerColor) {
                    Square fromSq = new Square(r, c);
                    if (p.isValidMove(this, fromSq, sq)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if the king of the specified color is in check.
     *
     * @param color the color of the king
     * @return true if in check
     */
    public boolean isInCheck(PieceColor color) {
        Square kingSquare = null;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                if (p instanceof King && p.getColor() == color) {
                    kingSquare = new Square(r, c);
                    break;
                }
            }
        }
        if (kingSquare == null) return false;
        return isSquareAttacked(kingSquare, color == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE);
    }

    /**
     * Creates a deep copy of the board state.
     *
     * @return copy of the board
     */
    public Board copy() {
        return new Board(this.grid, this.enPassantTarget, this.castlingRights, 
                         this.halfMoveClock, this.fullMoveNumber);
    }

    public Square getEnPassantTarget() { return enPassantTarget; }
    public void setEnPassantTarget(Square enPassantTarget) { this.enPassantTarget = enPassantTarget; }

    public boolean[] getCastlingRights() { return castlingRights; }
    public void setCastlingRights(boolean[] cr) { this.castlingRights = cr; }

    public int getHalfMoveClock() { return halfMoveClock; }
    public void setHalfMoveClock(int clock) { this.halfMoveClock = clock; }

    public int getFullMoveNumber() { return fullMoveNumber; }
    public void setFullMoveNumber(int number) { this.fullMoveNumber = number; }

    /**
     * Removes all pieces from the board, leaving every square empty.
     * Used by the tutorial system to set up custom practice positions.
     */
    public void clearAllPieces() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                setPiece(new Square(row, col), null);
            }
        }
    }
}
