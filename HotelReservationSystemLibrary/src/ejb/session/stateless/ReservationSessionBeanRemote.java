package ejb.session.stateless;

import entity.Reservation;
import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exceptions.InputDataValidationException;
import util.exceptions.InvalidRoomCountException;
import util.exceptions.ReservationDeleteException;
import util.exceptions.ReservationNotFoundException;
import util.exceptions.ReservationUpdateException;
import util.exceptions.RoomTypeUnavailableException;
import util.exceptions.UnknownPersistenceException;

@Remote
public interface ReservationSessionBeanRemote {

    public Reservation createReservation(Reservation reservation) throws InputDataValidationException, UnknownPersistenceException;

    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException;

    public Reservation updateReservation(Long reservationId, Reservation updatedReservation) throws ReservationNotFoundException, ReservationUpdateException;

    public void deleteReservation(Long reservationId) throws ReservationNotFoundException, ReservationDeleteException;

    public List<Reservation> viewAllReservations();

    public List<Reservation> retrieveReservationsByPartnerId(Long partnerId);

    public List<Reservation> retrieveReservationsByGuestId(Long guestId);

    public Reservation createReservationFromSearch(Long customerId, RoomType roomType, Date checkInDate, Date checkOutDate, int roomCount) throws RoomTypeUnavailableException, InvalidRoomCountException, InputDataValidationException, UnknownPersistenceException;
    
}
