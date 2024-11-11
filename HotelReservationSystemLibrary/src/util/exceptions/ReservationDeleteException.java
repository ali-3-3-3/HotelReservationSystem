package util.exceptions;

public class ReservationDeleteException extends Exception {

    /**
     * Creates a new instance of <code>ReservationDeleteException</code> without
     * detail message.
     */
    public ReservationDeleteException() {
    }

    /**
     * Constructs an instance of <code>ReservationDeleteException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ReservationDeleteException(String msg) {
        super(msg);
    }
}
