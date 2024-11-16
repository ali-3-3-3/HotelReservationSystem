/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package holidayreservationsystem;

import ws.partner.InputDataValidationException_Exception;
import ws.partner.InvalidLoginCredentialException_Exception;
import ws.partner.PartnerWebService_Service;
import ws.partner.ReservationNotFoundException_Exception;
import ws.partner.RoomTypeNotFoundException_Exception;

/**
 *
 * @author castellelow
 */
public class HolidayReservationSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InvalidLoginCredentialException_Exception, ReservationNotFoundException_Exception, RoomTypeNotFoundException_Exception, InputDataValidationException_Exception {
        // TODO code application logic here
        MainApp mainApp = new MainApp();
        mainApp.runApp(); //
    }
    
}
