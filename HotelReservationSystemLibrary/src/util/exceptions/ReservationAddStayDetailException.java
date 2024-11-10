package util.exceptions;

public class ReservationAddStayDetailException extends Exception {

    /**
     * Creates a new instance of <code>ReservationAddStayDetailException</code>
     * without detail message.
     */
    public ReservationAddStayDetailException() {
    }

    /**
     * Constructs an instance of <code>ReservationAddStayDetailException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ReservationAddStayDetailException(String msg) {
        super(msg);
    }
}
