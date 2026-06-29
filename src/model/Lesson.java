package model;

import java.util.List;

/**
 * Represents a single tutorial lesson with instructional text and a practice board setup.
 */
public class Lesson {
    private final String title;
    private final String[] instructionParagraphs;
    private final PracticeMode practiceMode;
    private final List<PiecePlacement> setup;
    private final String hintText;

    /**
     * Constructs a Lesson.
     *
     * @param title                 the lesson title
     * @param instructionParagraphs paragraphs of instruction text
     * @param practiceMode         the behavior mode of the board during this lesson
     * @param setup                the list of piece placements to setup the board
     * @param hintText             an optional tip displayed under the board
     */
    public Lesson(String title, String[] instructionParagraphs, PracticeMode practiceMode,
                  List<PiecePlacement> setup, String hintText) {
        this.title = title;
        this.instructionParagraphs = instructionParagraphs;
        this.practiceMode = practiceMode;
        this.setup = setup;
        this.hintText = hintText;
    }

    /**
     * Gets the lesson title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the instruction paragraphs.
     *
     * @return the instruction paragraphs
     */
    public String[] getInstructionParagraphs() {
        return instructionParagraphs;
    }

    /**
     * Gets the practice mode for this lesson.
     *
     * @return the practice mode
     */
    public PracticeMode getPracticeMode() {
        return practiceMode;
    }

    /**
     * Gets the list of piece placements to setup the lesson board.
     *
     * @return the piece placements
     */
    public List<PiecePlacement> getSetup() {
        return setup;
    }

    /**
     * Gets the helper hint text for this lesson.
     *
     * @return the hint text
     */
    public String getHintText() {
        return hintText;
    }

    /**
     * Represents a single piece placed on the board for a lesson's practice setup.
     */
    public static class PiecePlacement {
        /** The color of the piece to be placed. */
        public final PieceColor color;
        /** The class of the piece to be placed (e.g. Pawn.class, Rook.class). */
        public final Class<? extends Piece> pieceClass;
        /** The destination square for the piece. */
        public final Square square;

        /**
         * Constructs a PiecePlacement.
         *
         * @param color      the color of the piece
         * @param pieceClass the class of the piece
         * @param square     the square to place the piece on
         */
        public PiecePlacement(PieceColor color, Class<? extends Piece> pieceClass, Square square) {
            this.color = color;
            this.pieceClass = pieceClass;
            this.square = square;
        }
    }
}
