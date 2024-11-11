package util.exceptions;

public class RoomTypeNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>RoomTypeNotFoundException</code> without
     * detail message.
     */
    public RoomTypeNotFoundException() {
    }

    /**
     * Constructs an instance of <code>RoomTypeNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomTypeNotFoundException(String msg) {
        super(msg);
    }
}
