package ejb.session.ws;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.Partner;
import entity.Reservation;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exceptions.*;

@WebService(serviceName = "PartnerWebService")
@Stateless(name = "PartnerWebService")
public class PartnerWebService {

    @EJB(name = "RoomSessionBeanLocal")
    private RoomSessionBeanLocal roomSessionBeanLocal;

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

        for (Reservation reservation : partner.getReservations()) {
            em.detach(reservation);
            reservation.setPartner(null);
            reservation.setGuest(null);
            reservation.setPartner(null);
            reservation.setRoomType(null);
        }

        return partner;
    }

    // Partner Search Room
    @WebMethod(operationName = "searchRoom")
    public List<RoomType> searchRoom(@WebParam(name = "checkinDate") Date checkinDate, @WebParam(name = "checkoutDate") Date checkoutDate, @WebParam(name = "noOfRoom") Integer noOfRoom) throws RoomTypeNotFoundException {
        List<RoomType> availableRoomTypes = roomSessionBeanLocal.searchAvailableRoomTypes(checkinDate, checkoutDate);
        for (RoomType roomType : availableRoomTypes) {
            em.detach(roomType);
            roomType.setNextHigherRoomType(null);
            roomType.getRooms().clear();
            roomType.getRoomRates().clear();
            for (RoomRate rateRate : roomType.getRoomRates()) {
                em.detach(rateRate);
                rateRate.setRoomType(null);
            }

            for (Room room : roomType.getRooms()) {
                em.detach(room);
                room.setRoomType(null);
            }
        }
         return availableRoomTypes;
    }

    //partner reserve room 
    @WebMethod(operationName = "reserveNewReservation")
    public Reservation reserveNewReservation(@WebParam(name = "checkinDate") Date checkinDate, @WebParam(name = "checkoutDate") Date checkoutDate, @WebParam(name = "noOfRoom") Integer noOfRoom, @WebParam(name = "roomType")RoomType roomType,
            @WebParam(name = "guestId")Long guestId) throws RoomTypeUnavailableException, InvalidRoomCountException, InputDataValidationException, UnknownPersistenceException{
        
        Reservation reservation = reservationSessionBeanLocal.createReservationFromSearch(guestId, roomType, checkinDate, checkoutDate, noOfRoom);
        
        em.detach(reservation);
        
        reservation.setGuest(null);
        reservation.setPartner(null);
        reservation.setRoomType(null);
        
        return reservation;
    }
    
    
    
    // View Partner Reservation Details
    @WebMethod(operationName = "viewReservationsByPartnerId")
    public List<Reservation> viewReservationsByPartnerId(@WebParam(name = "partnerId") Long partnerId) {
        List<Reservation> reservations = partnerSessionBeanLocal.retrieveReservationsByPartnerId(partnerId);

        for (Reservation reservation : reservations) {

            em.detach(reservation);

            reservation.setGuest(null);
            reservation.setPartner(null);
            reservation.setRoomType(null);
        }
        return reservations;
    }

    // View All Partner Reservation
    @WebMethod(operationName = "retrievePartnerReservationsByReservationId")
    public Reservation retrievePartnerReservationsByReservationId(@WebParam(name = "reservationId") Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = partnerSessionBeanLocal.getPartnerReservationsByReservationId(reservationId);

        em.detach(reservation);
        reservation.setGuest(null);
        reservation.setPartner(null);
        reservation.setRoomType(null);

        return reservation;
    }
}
