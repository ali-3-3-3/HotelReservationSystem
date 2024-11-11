package util.exceptions;

public class RoomAllocationUpdateException extends Exception {

    /**
     * Creates a new instance of <code>RoomAllocationUpdateException</code>
     * without detail message.
     */
    public RoomAllocationUpdateException() {
    }

    /**
     * Constructs an instance of <code>RoomAllocationUpdateException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomAllocationUpdateException(String msg) {
        super(msg);
    }
}
