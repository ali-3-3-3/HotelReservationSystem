package util.exceptions;

public class EmployeeExistException extends Exception {

    /**
     * Creates a new instance of <code>EmployeeExistException</code> without
     * detail message.
     */
    public EmployeeExistException() {
    }

    /**
     * Constructs an instance of <code>EmployeeExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EmployeeExistException(String msg) {
        super(msg);
    }
}
