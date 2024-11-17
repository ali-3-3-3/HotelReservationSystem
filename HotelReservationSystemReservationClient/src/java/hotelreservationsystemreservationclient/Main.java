package hotelreservationsystemreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import javax.ejb.EJB;
import util.exceptions.InputDataValidationException;
import util.exceptions.RoomExistException;
import util.exceptions.RoomTypeNotFoundException;
import util.exceptions.UnknownPersistenceException;

public class Main {

    @EJB
    private static RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    
    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;
    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;
    @EJB
    private static RoomSessionBeanRemote roomSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InputDataValidationException, RoomExistException, RoomExistException, RoomExistException, RoomExistException, RoomExistException, RoomExistException, RoomExistException, UnknownPersistenceException, RoomTypeNotFoundException {
        
        MainApp mainApp = new MainApp(customerSessionBeanRemote, reservationSessionBeanRemote, roomSessionBeanRemote,roomTypeSessionBeanRemote);
        mainApp.runApp();
    }
    
}
