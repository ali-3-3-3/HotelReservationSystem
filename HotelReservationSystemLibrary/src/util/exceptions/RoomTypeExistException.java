package util.exceptions;

public class RoomTypeExistException extends Exception {

    /**
     * Creates a new instance of <code>RoomTypeExistException</code> without
     * detail message.
     */
    public RoomTypeExistException() {
    }

    /**
     * Constructs an instance of <code>RoomTypeExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomTypeExistException(String msg) {
        super(msg);
    }
}
