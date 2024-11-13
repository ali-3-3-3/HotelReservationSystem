package util.exceptions;

public class RoomTypeUnavailableException extends Exception {

    /**
     * Creates a new instance of <code>RoomTypeUnavailableException</code>
     * without detail message.
     */
    public RoomTypeUnavailableException() {
    }

    /**
     * Constructs an instance of <code>RoomTypeUnavailableException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomTypeUnavailableException(String msg) {
        super(msg);
    }
}
