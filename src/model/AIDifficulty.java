package model;

/**
 * Represents the search depth and description for each AI difficulty level.
 */
public enum AIDifficulty {
    /**
     * Easy difficulty: Depth 1, material-only evaluation, 2-second delay.
     */
    EASY(1, "Easy — Perfect for learning"),

    /**
     * Medium difficulty: Depth 3, standard evaluation, no delay.
     */
    MEDIUM(3, "Medium — Balanced challenge"),

    /**
     * Hard difficulty: Depth 5, enhanced positional bonuses, no delay.
     */
    HARD(5, "Hard — Tough opponent"),

    /**
     * Expert difficulty: Depth 6, maximum positional bonuses, no delay.
     */
    EXPERT(6, "Expert — Nearly unbeatable");

    private final int searchDepth;
    private final String description;

    /**
     * Constructs an AIDifficulty level.
     *
     * @param searchDepth the search depth for minimax
     * @param description user-friendly description of the level
     */
    AIDifficulty(int searchDepth, String description) {
        this.searchDepth = searchDepth;
        this.description = description;
    }

    /**
     * Gets the search depth associated with this difficulty level.
     *
     * @return the search depth
     */
    public int getSearchDepth() {
        return searchDepth;
    }

    /**
     * Gets the user-friendly description of this difficulty level.
     *
     * @return the description string
     */
    public String getDescription() {
        return description;
    }
}
