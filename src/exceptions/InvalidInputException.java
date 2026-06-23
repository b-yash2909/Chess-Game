package exceptions;

/**
 * Custom exception thrown when the user provides malformed input
 * or an unknown command over the console.
 */
public class InvalidInputException extends Exception {

    /**
     * Constructs a new InvalidInputException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidInputException(String message) {
        super(message);
    }
}
