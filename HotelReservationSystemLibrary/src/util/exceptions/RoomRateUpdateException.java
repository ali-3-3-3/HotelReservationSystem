/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exceptions;

/**
 *
 * @author aliya
 */
public class RoomRateUpdateException extends Exception {

    /**
     * Creates a new instance of <code>RoomRateUpdateException</code> without
     * detail message.
     */
    public RoomRateUpdateException() {
    }

    /**
     * Constructs an instance of <code>RoomRateUpdateException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomRateUpdateException(String msg) {
        super(msg);
    }
}
