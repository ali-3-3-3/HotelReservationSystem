package util.exceptions;

public class InvalidRoomCountException extends Exception {

    /**
     * Creates a new instance of <code>InvalidRoomCountException</code> without
     * detail message.
     */
    public InvalidRoomCountException() {
    }

    /**
     * Constructs an instance of <code>InvalidRoomCountException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidRoomCountException(String msg) {
        super(msg);
    }
}
