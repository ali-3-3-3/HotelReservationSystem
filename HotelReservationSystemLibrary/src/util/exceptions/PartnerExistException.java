package util.exceptions;

public class PartnerExistException extends Exception {

    /**
     * Creates a new instance of <code>PartnerExistException</code> without
     * detail message.
     */
    public PartnerExistException() {
    }

    /**
     * Constructs an instance of <code>PartnerExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PartnerExistException(String msg) {
        super(msg);
    }
}
