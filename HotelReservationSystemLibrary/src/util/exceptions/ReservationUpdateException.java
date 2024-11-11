/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exceptions;

/**
 *
 * @author aliya
 */
public class ReservationUpdateException extends Exception {

    /**
     * Creates a new instance of <code>ReservationUpdateException</code> without
     * detail message.
     */
    public ReservationUpdateException() {
    }

    /**
     * Constructs an instance of <code>ReservationUpdateException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ReservationUpdateException(String msg) {
        super(msg);
    }
}
