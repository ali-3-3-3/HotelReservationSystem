/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exceptions;

/**
 *
 * @author aliya
 */
public class RoomRateDeleteException extends Exception {

    /**
     * Creates a new instance of <code>RoomRateDeleteException</code> without
     * detail message.
     */
    public RoomRateDeleteException() {
    }

    /**
     * Constructs an instance of <code>RoomRateDeleteException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomRateDeleteException(String msg) {
        super(msg);
    }
}
