package util.exceptions;

public class GuestNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>GuestNotFoundException</code> without
     * detail message.
     */
    public GuestNotFoundException() {
    }

    /**
     * Constructs an instance of <code>GuestNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public GuestNotFoundException(String msg) {
        super(msg);
    }
}
