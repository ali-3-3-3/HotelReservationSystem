package util.exceptions;

public class ReservationNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>ReservationNotFoundException</code>
     * without detail message.
     */
    public ReservationNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ReservationNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ReservationNotFoundException(String msg) {
        super(msg);
    }
}
