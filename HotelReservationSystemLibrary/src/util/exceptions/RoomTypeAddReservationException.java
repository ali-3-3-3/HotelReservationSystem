package util.exceptions;

public class RoomTypeAddReservationException extends Exception {

    /**
     * Creates a new instance of <code>RoomTypeAddReservationException</code>
     * without detail message.
     */
    public RoomTypeAddReservationException() {
    }

    /**
     * Constructs an instance of <code>RoomTypeAddReservationException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomTypeAddReservationException(String msg) {
        super(msg);
    }
}
