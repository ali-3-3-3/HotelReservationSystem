package ejb.session.stateless;

import entity.Partner;
import entity.Reservation;
import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exceptions.InputDataValidationException;
import util.exceptions.InvalidRoomCountException;
import util.exceptions.ReservationDeleteException;
import util.exceptions.ReservationNotFoundException;
import util.exceptions.ReservationUpdateException;
import util.exceptions.RoomTypeUnavailableException;
import util.exceptions.UnknownPersistenceException;

@Local
public interface ReservationSessionBeanLocal {
    
  
    public Reservation createReservation(Reservation reservation) throws InputDataValidationException, UnknownPersistenceException;

    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException;

    public Reservation updateReservation(Long reservationId, Reservation updatedReservation) throws ReservationNotFoundException, ReservationUpdateException;

    public void deleteReservation(Long reservationId) throws ReservationNotFoundException, ReservationDeleteException;

    public List<Reservation> viewAllReservations();

    public List<Reservation> retrieveReservationsByPartnerId(Long partnerId);

    public List<Reservation> retrieveReservationsByGuestId(Long guestId);

    public Reservation createReservationFromSearch(Long customerId, RoomType roomType, Date checkInDate, Date checkOutDate, int roomCount) throws RoomTypeUnavailableException, InvalidRoomCountException, InputDataValidationException, UnknownPersistenceException;

    public double calculateTotalReservationFee(Date checkInDate, Date checkOutDate, RoomType roomType, Reservation reservation);

    public List<Reservation> retrieveReservationsByCheckInDate(Date checkInDate) throws ReservationNotFoundException;

    public int countReservationsByRoomTypeAndDates(RoomType roomType, Date checkInDate, Date checkOutDate);

    public double calculateTotalReservationFeeForWalkIn(Date checkInDate, Date checkOutDate, RoomType roomType, int roomCount);

    public void checkOutReservation(Long reservationId) throws ReservationNotFoundException;

    public void checkInReservation(Long reservationId) throws ReservationNotFoundException, RoomTypeUnavailableException;
    public double calculateTotalReservationFeeForWalkInPre(Date checkInDate, Date checkOutDate, RoomType roomType);

    public Reservation createReservationFromSearchForPartner(Long customerId, RoomType roomType, Date checkInDate, Date checkOutDate, int roomCount, Partner partner) throws RoomTypeUnavailableException, InvalidRoomCountException, InputDataValidationException, UnknownPersistenceException;
    
}
