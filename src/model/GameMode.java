package model;

/**
 * Represents the selected game mode for a chess session.
 * Supports Player vs Player (Human vs Human) and Player vs Computer.
 */
public class GameMode {
    /**
     * Enumerates the primary game mode types.
     */
    public enum Type {
        HUMAN_VS_COMPUTER,
        HUMAN_VS_HUMAN
    }

    private Type type;
    private AIDifficulty difficulty;  // only set if type == HUMAN_VS_COMPUTER

    /**
     * Constructs a GameMode with a default difficulty of MEDIUM.
     *
     * @param type the type of game mode
     */
    public GameMode(Type type) {
        this.type = type;
        this.difficulty = AIDifficulty.MEDIUM;  // default
    }

    /**
     * Constructs a GameMode with a specific type and AI difficulty.
     *
     * @param type the type of game mode
     * @param difficulty the AI difficulty level
     */
    public GameMode(Type type, AIDifficulty difficulty) {
        this.type = type;
        this.difficulty = difficulty;
    }

    /**
     * Gets the game mode type.
     *
     * @return the game mode type
     */
    public Type getType() {
        return type;
    }

    /**
     * Gets the AI difficulty.
     *
     * @return the AI difficulty
     */
    public AIDifficulty getDifficulty() {
        return difficulty;
    }

    private boolean hintsEnabled = false;

    /**
     * Checks whether hints are enabled for the game session.
     *
     * @return true if hints are enabled, false otherwise
     */
    public boolean isHintsEnabled() {
        return hintsEnabled;
    }

    /**
     * Sets whether hints are enabled for the game session.
     *
     * @param hintsEnabled true to enable hints, false to disable
     */
    public void setHintsEnabled(boolean hintsEnabled) {
        this.hintsEnabled = hintsEnabled;
    }

    /**
     * Sets the AI difficulty.
     *
     * @param difficulty the AI difficulty level to set
     */
    public void setDifficulty(AIDifficulty difficulty) {
        this.difficulty = difficulty;
    }
}
