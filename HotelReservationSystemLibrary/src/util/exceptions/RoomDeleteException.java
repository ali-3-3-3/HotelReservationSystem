package util.exceptions;

public class RoomDeleteException extends Exception {

    /**
     * Creates a new instance of <code>RoomDeleteException</code> without detail
     * message.
     */
    public RoomDeleteException() {
    }

    /**
     * Constructs an instance of <code>RoomDeleteException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomDeleteException(String msg) {
        super(msg);
    }
}
