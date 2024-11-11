package util.exceptions;

public class RoomAllocationDeleteException extends Exception {

    /**
     * Creates a new instance of <code>RoomAllocationDeleteException</code>
     * without detail message.
     */
    public RoomAllocationDeleteException() {
    }

    /**
     * Constructs an instance of <code>RoomAllocationDeleteException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomAllocationDeleteException(String msg) {
        super(msg);
    }
}
