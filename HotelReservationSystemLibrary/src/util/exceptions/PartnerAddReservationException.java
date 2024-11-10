package util.exceptions;

public class PartnerAddReservationException extends Exception {

    /**
     * Creates a new instance of <code>PartnerAddReservationException</code>
     * without detail message.
     */
    public PartnerAddReservationException() {
    }

    /**
     * Constructs an instance of <code>PartnerAddReservationException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public PartnerAddReservationException(String msg) {
        super(msg);
    }
}
