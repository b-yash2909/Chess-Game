package model;

/**
 * Defines how a tutorial lesson's practice board behaves.
 */
public enum PracticeMode {
    /**
     * Any piece can be moved by anyone, no turns, no check rules.
     * Used for piece-movement lessons (pawn, rook, knight, etc.).
     */
    FREE_MOVE,

    /**
     * Uses full GameState rules (turns, check, checkmate).
     * Used for castling, en passant, promotion, check, checkmate, stalemate.
     */
    GAME_RULE_DEMO
}
