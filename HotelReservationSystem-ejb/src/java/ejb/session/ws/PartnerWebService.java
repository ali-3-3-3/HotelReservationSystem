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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    private Partner partner;
    
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    

    // Partner Login
    @WebMethod(operationName = "doLogin")
    public Partner doLogin(@WebParam(name = "email") String email, @WebParam(name = "password") String password) throws InvalidLoginCredentialException {
        partner = partnerSessionBeanLocal.doLogin(email, password);
        
        em.detach(partner);
        partner.getReservations().stream().map(reservation -> {
            em.detach(reservation);
            return reservation;
        }).map(reservation -> {
            reservation.setPartner(null);
            return reservation;
        }).map(reservation -> {
            reservation.setGuest(null);
            return reservation;
        }).map(reservation -> {
            reservation.setPartner(null);
            return reservation;
        }).forEachOrdered(reservation -> {
            reservation.setRoomType(null);
        });
        return partner;
    }

    // Partner Search Room
    @WebMethod(operationName = "searchRoom")
    public List<RoomType> searchRoom(
            @WebParam(name = "checkinDateString") String checkinDateString, 
            @WebParam(name = "checkoutDateString") String checkoutDateString) 
            throws RoomTypeNotFoundException, InputDataValidationException {
        
        dateFormat.setLenient(false);
        
        Date checkinDate;
        Date checkoutDate;

        try {
            checkinDate = dateFormat.parse(checkinDateString);
            checkoutDate = dateFormat.parse(checkoutDateString);

            // Ensure check-out date is after check-in date
            if (checkoutDate.before(checkinDate)) {
                throw new InputDataValidationException("Check-out date must be after check-in date.");
            }
        } catch (ParseException e) {
            throw new InputDataValidationException("Invalid date format. Please use yyyy-MM-dd.");
        }

        try {
            List<RoomType> availableRoomTypes = roomSessionBeanLocal.searchAvailableRoomTypes(checkinDate, checkoutDate);
            availableRoomTypes.stream().map(r -> {
                em.detach(r);
                return r;
            }).map(r -> {
                r.setRooms(null);
                return r;
            }).map(r -> {
                r.setReservations(null);
                return r;
            }).forEachOrdered(r -> {
                r.setRoomRates(null);
            });
            return availableRoomTypes != null ? availableRoomTypes : new ArrayList<>();  // Return an empty list instead of null
        }catch (Exception ex) {
            // Log unexpected exception and throw a general input validation exception
            System.err.println("An unexpected error occurred while searching for room types: " + ex.getMessage());
            throw new InputDataValidationException("An unexpected error occurred. Please try again later.");
        } 
    }

    // Partner reserve room 
    @WebMethod(operationName = "reserveNewReservation")
    public Reservation reserveNewReservation(
            @WebParam(name = "checkinDate") String checkinDateString,
            @WebParam(name = "checkoutDate") String checkoutDateString,
            @WebParam(name = "noOfRoom") Integer noOfRoom,
            @WebParam(name = "roomTypeId") Long roomTypeId,
            @WebParam(name = "guestId") Long guestId)
            throws RoomTypeUnavailableException, InvalidRoomCountException, InputDataValidationException, UnknownPersistenceException {

        // Validate number of rooms
        if (noOfRoom == null || noOfRoom <= 0) {
            throw new InvalidRoomCountException("Number of rooms must be greater than zero.");
        }
        
        dateFormat.setLenient(false);
        
        Date checkinDate;
        Date checkoutDate;

        try {
            checkinDate = dateFormat.parse(checkinDateString);
            checkoutDate = dateFormat.parse(checkoutDateString);

            // Ensure check-out date is after check-in date
            if (checkoutDate.before(checkinDate)) {
                throw new InputDataValidationException("Check-out date must be after check-in date.");
            }
        } catch (ParseException e) {
            throw new InputDataValidationException("Invalid date format. Please use yyyy-MM-dd.");
        }

        // Retrieve room type
        RoomType roomType;
        try {
            roomType = roomTypeSessionBeanLocal.retrieveRoomTypeById(roomTypeId);
            em.detach(roomType);
            roomType.setNextHigherRoomType(null);
            roomType.setReservations(null);
            roomType.setRoomRates(null);
            roomType.setRooms(null);
        } catch (RoomTypeNotFoundException ex) {
            Logger.getLogger(PartnerWebService.class.getName()).log(Level.SEVERE, "Room type not found: " + roomTypeId, ex);
            throw new InputDataValidationException("Specified room type does not exist.");
        }

        // Create reservation
        Reservation reservation;
        try {
            reservation = reservationSessionBeanLocal.createReservationFromSearch(guestId, roomType, checkinDate, checkoutDate, noOfRoom);
            
        } catch (InputDataValidationException | InvalidRoomCountException | RoomTypeUnavailableException | UnknownPersistenceException ex) {
            Logger.getLogger(PartnerWebService.class.getName()).log(Level.SEVERE, "Error creating reservation", ex);
            throw new UnknownPersistenceException("An error occurred while creating the reservation. Please try again later.");
        }

        // Associate reservation with partner and update reservation
        try {
            reservation.setPartner(partner); // Ensure 'partner' is properly defined in this context
            reservationSessionBeanLocal.updateReservation(reservation.getReservationId(), reservation);
            em.detach(reservation);
            reservation.setGuest(null);
            reservation.setPartner(null);
            reservation.setRoomType(null);
            reservation.setRoomAllocations(null);
            reservation.setRoomRates(null);
        } catch (ReservationNotFoundException | ReservationUpdateException ex) {
            Logger.getLogger(PartnerWebService.class.getName()).log(Level.SEVERE, "Error updating reservation", ex);
            throw new UnknownPersistenceException("An error occurred while updating the reservation. Please try again later.");
        }

        System.out.println("Reservation created successfully with ID: " + reservation.getReservationId());
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
            reservation.setRoomAllocations(null);
            reservation.setRoomRates(null);

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
        reservation.setRoomAllocations(null);
        reservation.setRoomRates(null);

        return reservation;
    }
}
