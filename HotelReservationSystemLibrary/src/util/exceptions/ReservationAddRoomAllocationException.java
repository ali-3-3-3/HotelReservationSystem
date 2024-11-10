package util.exceptions;

public class ReservationAddRoomAllocationException extends Exception {

    /**
     * Creates a new instance of
     * <code>ReservationAddRoomAllocationException</code> without detail
     * message.
     */
    public ReservationAddRoomAllocationException() {
    }

    /**
     * Constructs an instance of
     * <code>ReservationAddRoomAllocationException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public ReservationAddRoomAllocationException(String msg) {
        super(msg);
    }
}
