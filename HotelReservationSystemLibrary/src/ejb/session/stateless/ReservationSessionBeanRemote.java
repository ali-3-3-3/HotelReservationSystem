/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import java.util.List;
import javax.ejb.Remote;
import util.exceptions.InputDataValidationException;
import util.exceptions.ReservationDeleteException;
import util.exceptions.ReservationNotFoundException;
import util.exceptions.ReservationUpdateException;
import util.exceptions.UnknownPersistenceException;

/**
 *
 * @author aliya
 */
@Remote
public interface ReservationSessionBeanRemote {

    public Reservation createReservation(Reservation reservation) throws InputDataValidationException, UnknownPersistenceException;

    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException;

    public Reservation updateReservation(Long reservationId, Reservation updatedReservation) throws ReservationNotFoundException, ReservationUpdateException;

    public void deleteReservation(Long reservationId) throws ReservationNotFoundException, ReservationDeleteException;

    public List<Reservation> viewAllReservations();

    public List<Reservation> retrieveReservationsByPartnerId(Long partnerId);

    public List<Reservation> retrieveReservationsByGuestId(Long guestId);
    
}