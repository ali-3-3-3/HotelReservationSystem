package ejb.session.ws;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.GuestSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.Guest;
import entity.Partner;
import entity.Reservation;
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
import javax.jws.WebResult;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.datatype.XMLGregorianCalendar;
import util.exceptions.*;

@WebService(serviceName = "PartnerWebService")
@Stateless(name = "PartnerWebService")
public class PartnerWebService {

    @EJB(name = "GuestSessionBeanLocal")
    private GuestSessionBeanLocal guestSessionBeanLocal;

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
    @WebResult(name = "doLoginResponse")
    public Partner doLogin(
            @WebParam(name = "email") String email,
            @WebParam(name = "password") String password
    ) throws InvalidLoginCredentialException {
        return partnerSessionBeanLocal.doLogin(email, password);
    }

    // Partner Search Room - the wsdl keeps failing to update whenever I make changes. 
    @WebMethod(operationName = "searchRoom")
    public List<RoomType> searchRoom(
            @WebParam(name = "checkInDate") Date checkInDate,
            @WebParam(name = "checkOutDate") Date checkOutDate)
            throws RoomTypeNotFoundException, InputDataValidationException {

        dateFormat.setLenient(false);

        // Ensure check-out date is after check-in date
        if (checkOutDate.before(checkInDate)) {
            throw new InputDataValidationException("Check-out date must be after check-in date.");
        }

        try {
            List<RoomType> availableRoomTypes = roomSessionBeanLocal.searchAvailableRoomTypes(checkInDate, checkOutDate);
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
        } catch (Exception ex) {
            // Log unexpected exception and throw a general input validation exception
            System.err.println("An unexpected error occurred while searching for room types: " + ex.getMessage());
            throw new InputDataValidationException("An unexpected error occurred. Please try again later.");
        }
    }

    private Date convertToDate(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) {
            return null;
        }
        return xmlGregorianCalendar.toGregorianCalendar().getTime();
    }

@WebMethod(operationName = "calculatePre")
public double calculatePre(
        @WebParam(name = "checkInDate") XMLGregorianCalendar checkInDate,
        @WebParam(name = "checkOutDate") XMLGregorianCalendar checkOutDate,
        @WebParam(name = "roomType") RoomType roomType) {

    if (checkInDate == null || checkOutDate == null || roomType == null) {
        throw new IllegalArgumentException("Check-in date, check-out date, and room type must not be null.");
    }

    Date checkIn = convertToDate(checkInDate);
    Date checkOut = convertToDate(checkOutDate);

    try {
        return reservationSessionBeanLocal.calculateTotalReservationFeeForWalkInPre(checkIn, checkOut, roomType);
    } catch (Exception e) {
        Logger.getLogger(PartnerWebService.class.getName())
              .log(Level.SEVERE, "Error in calculatePre: {0}", e.getMessage());
        throw new RuntimeException("Error calculating pre-payment fee: " + e.getMessage(), e);
    }
}


    // Partner reserve room - the wsdl keeps failing to update whenever I make changes. 
    @WebMethod(operationName = "reserveNewReservation")
    @WebResult(targetNamespace = "")
    public Reservation reserveNewReservation(
            @WebParam(name = "checkinDate", targetNamespace = "") String checkinDateString,
            @WebParam(name = "checkoutDate", targetNamespace = "") String checkoutDateString,
            @WebParam(name = "noOfRoom", targetNamespace = "") Integer noOfRoom,
            @WebParam(name = "roomTypeId", targetNamespace = "") Long roomTypeId,
            @WebParam(name = "guestId", targetNamespace = "") Long guestId)
            throws RoomTypeUnavailableException, InvalidRoomCountException, InputDataValidationException, UnknownPersistenceException {

        if (noOfRoom == null || noOfRoom <= 0) {
            throw new InvalidRoomCountException("Number of rooms must be greater than zero.");
        }

        dateFormat.setLenient(false);

        Date checkinDate;
        Date checkoutDate;

        try {
            checkinDate = dateFormat.parse(checkinDateString);
            checkoutDate = dateFormat.parse(checkoutDateString);

            if (checkoutDate.before(checkinDate)) {
                throw new InputDataValidationException("Check-out date must be after check-in date.");
            }
        } catch (ParseException e) {
            throw new InputDataValidationException("Invalid date format. Please use yyyy-MM-dd.");
        }

        RoomType roomType;
        try {
            roomType = roomTypeSessionBeanLocal.retrieveRoomTypeById(roomTypeId);
            em.detach(roomType);
            roomType.setNextHigherRoomType(null);
            roomType.setReservations(null);
            roomType.setRoomRates(null);
            roomType.setRooms(null);
        } catch (RoomTypeNotFoundException ex) {
            throw new InputDataValidationException("Specified room type does not exist.");
        }

        Reservation reservation;
        try {
            reservation = reservationSessionBeanLocal.createReservationFromSearchForPartner(guestId, roomType, checkinDate, checkoutDate, noOfRoom, partner);
            if (reservation == null) {
                throw new UnknownPersistenceException("Failed to create reservation. Please check input data.");
            }

            // Ensure partner is set
            if (partner == null) {
                throw new UnknownPersistenceException("Partner information is missing. Please log in again.");
            }
            reservation.setPartner(partner);

            reservationSessionBeanLocal.updateReservation(reservation.getReservationId(), reservation);

            em.detach(reservation);
            reservation.setGuest(null);
            reservation.setPartner(null);
            reservation.setRoomType(null);
            reservation.setRoomAllocations(null);
            reservation.setRoomRates(null);

        } catch (Exception ex) {
            throw new UnknownPersistenceException("Error occurred while updating the reservation: " + ex.getMessage());
        }

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

    @WebMethod(operationName = "retrieveGuestByGuestId")
    public Guest retrieveGuestByGuestId(@WebParam(name = "guestId") Long guestId) throws GuestNotFoundException {
        Guest guest = guestSessionBeanLocal.retrieveGuestByGuestId(guestId);

        em.detach(guest);
        guest.setReservations(null);

        return guest;
    }

}
