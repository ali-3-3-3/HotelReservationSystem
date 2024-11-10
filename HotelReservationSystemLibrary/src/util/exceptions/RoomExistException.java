package util.exceptions;

public class RoomExistException extends Exception {

    /**
     * Creates a new instance of <code>RoomExistException</code> without detail
     * message.
     */
    public RoomExistException() {
    }

    /**
     * Constructs an instance of <code>RoomExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomExistException(String msg) {
        super(msg);
    }
}
