package ejb.session.stateless;

import entity.Reservation;
import java.util.List;
import javax.ejb.Local;
import util.exceptions.InputDataValidationException;
import util.exceptions.ReservationDeleteException;
import util.exceptions.ReservationNotFoundException;
import util.exceptions.ReservationUpdateException;
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
}
