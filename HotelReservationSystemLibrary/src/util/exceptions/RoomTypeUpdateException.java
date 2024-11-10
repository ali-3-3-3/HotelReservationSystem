/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exceptions;

/**
 *
 * @author aliya
 */
public class RoomTypeUpdateException extends Exception {

    /**
     * Creates a new instance of <code>RoomTypeUpdateException</code> without
     * detail message.
     */
    public RoomTypeUpdateException() {
    }

    /**
     * Constructs an instance of <code>RoomTypeUpdateException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomTypeUpdateException(String msg) {
        super(msg);
    }
}
