package util.exceptions;

public class GuestDeleteException extends Exception {

    /**
     * Creates a new instance of <code>GuestDeleteException</code> without
     * detail message.
     */
    public GuestDeleteException() {
    }

    /**
     * Constructs an instance of <code>GuestDeleteException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public GuestDeleteException(String msg) {
        super(msg);
    }
}
