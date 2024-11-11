/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exceptions.InputDataValidationException;
import util.exceptions.ReservationDeleteException;
import util.exceptions.ReservationNotFoundException;
import util.exceptions.ReservationUpdateException;
import util.exceptions.UnknownPersistenceException;

/**
 *
 * @author aliya
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public ReservationSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    @Transactional
    public Reservation createReservation(Reservation reservation) throws InputDataValidationException, UnknownPersistenceException {
        Set<ConstraintViolation<Reservation>> constraintViolations = validator.validate(reservation);

        if (!constraintViolations.isEmpty()) {
            StringBuilder errorMsg = new StringBuilder("Input data validation error(s):");
            for (ConstraintViolation<Reservation> violation : constraintViolations) {
                errorMsg.append("\n- ").append(violation.getPropertyPath()).append(": ").append(violation.getMessage());
            }
            throw new InputDataValidationException(errorMsg.toString());
        }
        
        try {
            em.persist(reservation);
            em.flush();
            return reservation;
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                throw new UnknownPersistenceException("Database error: " + ex.getMessage());
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = em.find(Reservation.class, reservationId);
        if (reservation == null) {
            throw new ReservationNotFoundException("Reservation ID " + reservationId + " not found.");
        }
        return reservation;
    }

    @Override
    @Transactional
    public Reservation updateReservation(Long reservationId, Reservation updatedReservation) throws ReservationNotFoundException, ReservationUpdateException {
        Reservation reservation = retrieveReservationById(reservationId);
        
        try {
            reservation.setReservationDate(updatedReservation.getReservationDate());
            reservation.setCheckInDate(updatedReservation.getCheckInDate());
            reservation.setCheckOutDate(updatedReservation.getCheckOutDate());
            reservation.setCheckInTime(updatedReservation.getCheckInTime());
            reservation.setCheckOutTime(updatedReservation.getCheckOutTime());
            reservation.setHasCheckedIn(updatedReservation.isHasCheckedIn());
            reservation.setHasCheckedOut(updatedReservation.isHasCheckedOut());
            reservation.setGuest(updatedReservation.getGuest());
            reservation.setPartner(updatedReservation.getPartner());
            reservation.setRoomType(updatedReservation.getRoomType());

            em.merge(reservation);
            return reservation;
        } catch (PersistenceException ex) {
            throw new ReservationUpdateException("Failed to update reservation: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteReservation(Long reservationId) throws ReservationNotFoundException, ReservationDeleteException {
        Reservation reservation = retrieveReservationById(reservationId);
        
        try {
            em.remove(reservation);
        } catch (PersistenceException ex) {
            throw new ReservationDeleteException("Failed to delete reservation: " + ex.getMessage());
        }
    }

    @Override
    public List<Reservation> viewAllReservations() {
        return em.createQuery("SELECT r FROM Reservation r", Reservation.class).getResultList();
    }
    
    @Override
    public List<Reservation> retrieveReservationsByPartnerId(Long partnerId) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.partner.partnerId = :partnerId");
        query.setParameter("partnerId", partnerId);
        
        return query.getResultList();
    }
    
    @Override
    public List<Reservation> retrieveReservationsByGuestId(Long guestId) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.guest.guestId = :guestId");
        query.setParameter("guestId", guestId);
        
        return query.getResultList();
    }
    
}
