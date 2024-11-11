package util.exceptions;

public class AllocationExceptionNotFoundException extends Exception {

    /**
     * Creates a new instance of
     * <code>AllocationExceptionNotFoundException</code> without detail message.
     */
    public AllocationExceptionNotFoundException() {
    }

    /**
     * Constructs an instance of
     * <code>AllocationExceptionNotFoundException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public AllocationExceptionNotFoundException(String msg) {
        super(msg);
    }
}
