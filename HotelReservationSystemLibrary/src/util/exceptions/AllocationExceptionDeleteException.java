package util.exceptions;

public class AllocationExceptionDeleteException extends Exception {

    /**
     * Creates a new instance of <code>AllocationExceptionDeleteException</code>
     * without detail message.
     */
    public AllocationExceptionDeleteException() {
    }

    /**
     * Constructs an instance of <code>AllocationExceptionDeleteException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public AllocationExceptionDeleteException(String msg) {
        super(msg);
    }
}
