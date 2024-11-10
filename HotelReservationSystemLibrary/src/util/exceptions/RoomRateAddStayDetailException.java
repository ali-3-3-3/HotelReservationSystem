package util.exceptions;

public class RoomRateAddStayDetailException extends Exception {

    /**
     * Creates a new instance of <code>RoomRateAddStayDetailException</code>
     * without detail message.
     */
    public RoomRateAddStayDetailException() {
    }

    /**
     * Constructs an instance of <code>RoomRateAddStayDetailException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomRateAddStayDetailException(String msg) {
        super(msg);
    }
}
