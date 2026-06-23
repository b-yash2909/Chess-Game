package exceptions;

/**
 * Custom exception thrown when an illegal or invalid chess move is attempted.
 */
public class InvalidMoveException extends Exception {
    
    /**
     * Constructs a new InvalidMoveException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidMoveException(String message) {
        super(message);
    }
}
