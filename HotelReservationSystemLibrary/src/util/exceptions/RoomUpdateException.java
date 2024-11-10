package util.exceptions;

public class RoomUpdateException extends Exception {

    /**
     * Creates a new instance of <code>RoomUpdateException</code> without detail
     * message.
     */
    public RoomUpdateException() {
    }

    /**
     * Constructs an instance of <code>RoomUpdateException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomUpdateException(String msg) {
        super(msg);
    }
}
