package util.exceptions;

public class RoomTypeAddRoomException extends Exception {

    /**
     * Creates a new instance of <code>RoomTypeAddRoomException</code> without
     * detail message.
     */
    public RoomTypeAddRoomException() {
    }

    /**
     * Constructs an instance of <code>RoomTypeAddRoomException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomTypeAddRoomException(String msg) {
        super(msg);
    }
}
