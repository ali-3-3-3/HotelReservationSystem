package ejb.session.ws;

import util.exceptions.PartnerEmailExistException;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.Customer;
import entity.Partner;
import entity.Reservation;
import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exceptions.*;

@WebService(serviceName = "PartnerWebService")
@Stateless(name = "PartnerWebService")
public class PartnerWebService {

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBeanLocal;


    
    // Partner Login
       @WebMethod(operationName = "doLogin")
    public Partner partnerLogin(@WebParam(name = "email") String email, @WebParam(name = "password") String password) throws InvalidLoginCredentialException {
        Partner partner = partnerSessionBeanLocal.doLogin(email, password);
        
        em.detach(partner);
        
        for(Reservation reservation : partner.getReservations()){
            em.detach(reservation);
            reservation.setPartner(null);
            reservation.setGuest(null);
            reservation.setPartner(null);
            reservation.setRoomType(null);
        }
       
        return partner;
    }


    // Partner Search Room
    @WebMethod(operationName = "searchAvailableRooms")
    public List<RoomType> searchAvailableRooms(@WebParam(name = "checkInDate") Date checkInDate,
            @WebParam(name = "checkOutDate") Date checkOutDate)
            throws RoomTypeNotFoundException {
        List<RoomType> availableRooms = roomTypeSessionBeanLocal.getAllRoomTypes();
        for (RoomType roomType : availableRooms) {
            em.detach(roomType); // Detach each room type
        }
        return availableRooms;
    }

    // Partner Reserve Room
    @WebMethod(operationName = "reserveRoom")
    public Reservation reserveRoom(@WebParam(name = "partnerId") Long partnerId,
            @WebParam(name = "customerId") Long customerId,
            @WebParam(name = "roomTypeId") Long roomTypeId,
            @WebParam(name = "checkInDate") Date checkInDate,
            @WebParam(name = "checkOutDate") Date checkOutDate,
            @WebParam(name = "roomCount") int roomCount)
            throws PartnerNotFoundException, CustomerNotFoundException, RoomTypeUnavailableException, InvalidRoomCountException, InputDataValidationException, UnknownPersistenceException, RoomTypeNotFoundException {

        // Verify partner existence
        partnerSessionBeanLocal.retrievePartnerById(partnerId);

        // Retrieve room type and customer
        RoomType roomType = roomTypeSessionBeanLocal.retrieveRoomTypeById(roomTypeId);
        Customer customer = customerSessionBeanLocal.findCustomerById(customerId);

        // Create reservation on behalf of the partner's customer
        Reservation reservation = reservationSessionBeanLocal.createReservationFromSearch(customer.getGuestId(), roomType, checkInDate, checkOutDate, roomCount);
        em.detach(reservation);  // Detach to prevent unintended changes
        return reservation;
    }

    // View Partner Reservation Details
    @WebMethod(operationName = "viewReservationDetails")
    public Reservation viewReservationDetails(@WebParam(name = "reservationId") Long reservationId)
            throws ReservationNotFoundException {
        Reservation reservation = reservationSessionBeanLocal.retrieveReservationById(reservationId);
        em.detach(reservation);
        return reservation;
    }

    // View All Partner Reservations
    @WebMethod(operationName = "viewAllPartnerReservations")
    public List<Reservation> viewAllPartnerReservations(@WebParam(name = "partnerId") Long partnerId)
            throws PartnerNotFoundException {
        List<Reservation> reservations = reservationSessionBeanLocal.retrieveReservationsByPartnerId(partnerId);
        for (Reservation reservation : reservations) {
            em.detach(reservation); // Detach each reservation
        }
        return reservations;
    }

}
