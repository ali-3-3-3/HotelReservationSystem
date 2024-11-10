package util.exceptions;

public class RoomAddRoomAllocationException extends Exception {

    /**
     * Creates a new instance of <code>RoomAddRoomAllocationException</code>
     * without detail message.
     */
    public RoomAddRoomAllocationException() {
    }

    /**
     * Constructs an instance of <code>RoomAddRoomAllocationException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomAddRoomAllocationException(String msg) {
        super(msg);
    }
}
