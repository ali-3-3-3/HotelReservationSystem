/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.exceptions;

/**
 *
 * @author castellelow
 */
public class PartnerEmailExistException extends Exception {
    public PartnerEmailExistException() {
    }

    /**
     * Constructs an instance of <code>PartnerAddReservationException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public PartnerEmailExistException(String msg) {
        super(msg);
    }
}
