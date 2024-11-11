package util.exceptions;

public class AllocationExceptionExistException extends Exception {

    /**
     * Creates a new instance of <code>AllocationExceptionExistException</code>
     * without detail message.
     */
    public AllocationExceptionExistException() {
    }

    /**
     * Constructs an instance of <code>AllocationExceptionExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public AllocationExceptionExistException(String msg) {
        super(msg);
    }
}
