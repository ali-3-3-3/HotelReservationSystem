package util.exceptions;

public class RoomAllocationNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>RoomAllocationNotFoundException</code>
     * without detail message.
     */
    public RoomAllocationNotFoundException() {
    }

    /**
     * Constructs an instance of <code>RoomAllocationNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomAllocationNotFoundException(String msg) {
        super(msg);
    }
}
