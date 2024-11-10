package util.exceptions;

public class EmployeeAddExceptionException extends Exception {

    /**
     * Creates a new instance of <code>EmployeeAddExceptionException</code>
     * without detail message.
     */
    public EmployeeAddExceptionException() {
    }

    /**
     * Constructs an instance of <code>EmployeeAddExceptionException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public EmployeeAddExceptionException(String msg) {
        super(msg);
    }
}
