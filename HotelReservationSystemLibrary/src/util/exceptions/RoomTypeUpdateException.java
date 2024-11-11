package util.exceptions;

public class RoomTypeUpdateException extends Exception {

    /**
     * Creates a new instance of <code>RoomTypeUpdateException</code> without
     * detail message.
     */
    public RoomTypeUpdateException() {
    }

    /**
     * Constructs an instance of <code>RoomTypeUpdateException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomTypeUpdateException(String msg) {
        super(msg);
    }
}
