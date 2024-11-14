package hotelreservationsystemmanagementclient;

import ejb.session.stateless.AllocationExceptionSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomAllocationSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import javax.ejb.EJB;

public class Main {
    
    @EJB
    private static AllocationExceptionSessionBeanRemote allocationExceptionSessionBeanRemote;
    @EJB
    private static GuestSessionBeanRemote guestSessionBeanRemote;
    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;
    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;
    @EJB
    private static RoomSessionBeanRemote roomSessionBeanRemote;
    @EJB
    private static RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    @EJB
    private static RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    @EJB
    private static RoomAllocationSessionBeanRemote roomAllocationSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        MainApp mainApp = new MainApp(allocationExceptionSessionBeanRemote, guestSessionBeanRemote, employeeSessionBeanRemote, reservationSessionBeanRemote, roomSessionBeanRemote, roomRateSessionBeanRemote, roomTypeSessionBeanRemote, roomAllocationSessionBeanRemote);
        mainApp.runApp();
    }
    
}
