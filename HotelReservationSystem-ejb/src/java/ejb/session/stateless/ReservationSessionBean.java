package ejb.session.stateless;

import entity.Reservation;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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
import util.enumerations.RoomStatusEnum;
import util.exceptions.CustomerNotFoundException;
import util.exceptions.InputDataValidationException;
import util.exceptions.InvalidRoomCountException;
import util.exceptions.ReservationDeleteException;
import util.exceptions.ReservationNotFoundException;
import util.exceptions.ReservationUpdateException;
import util.exceptions.RoomTypeUnavailableException;
import util.exceptions.UnknownPersistenceException;

@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

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
    @Transactional
    public Reservation createReservationFromSearch(Long customerId, RoomType roomType, Date checkInDate, Date checkOutDate, int roomCount) 
        throws RoomTypeUnavailableException, InvalidRoomCountException, InputDataValidationException, UnknownPersistenceException {
    
        try {
            if (roomCount < 1 || roomCount > 9) {
                throw new InvalidRoomCountException("You can reserve between 1 and 9 rooms.");
            }
            
            List<Room> availableRooms = em.createQuery(
                    "SELECT r FROM Room r WHERE r.roomType = :roomType AND r.roomStatus = :availableStatus", Room.class)
                    .setParameter("roomType", roomType)
                    .setParameter("availableStatus", RoomStatusEnum.AVAILABLE)
                    .getResultList();
            
            if (availableRooms.size() < roomCount) {
                throw new RoomTypeUnavailableException("Requested room type has only " + availableRooms.size() + " rooms available.");
            }
            
            
            Reservation reservation = new Reservation(new Date(), checkInDate, checkOutDate, roomCount);
            
            reservation.setGuest(customerSessionBean.findCustomerById(customerId));
            reservation.setRoomType(roomType);
            
            return createReservation(reservation);
        } catch (CustomerNotFoundException ex) {
            Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
            reservation.setRoomRates(updatedReservation.getRoomRates());

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
    
    @Override
    @Transactional
    public double calculateTotalReservationFee(Date checkInDate, Date checkOutDate, RoomType roomType, Reservation reservation) {
        double totalFee = 0.0;
        Date currentDate = checkInDate;

        reservation.setRoomRates(new HashSet<>()); 

        while (!currentDate.after(checkOutDate)) {
            RoomRate dailyRate = getDailyRateForRoomType(currentDate, roomType);
            if (dailyRate != null) {
                totalFee += dailyRate.getRatePerNight();
                reservation.getRoomRates().add(dailyRate);
            }
            currentDate = getNextDate(currentDate);
        }
        
        try {
            updateReservation(reservation.getReservationId(), reservation);
        } catch (ReservationNotFoundException | ReservationUpdateException ex) {
            Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return totalFee;
    }

    private RoomRate getDailyRateForRoomType(Date date, RoomType roomType) {
        RoomRate selectedRate = null;
        double highestPriorityRate = 0.0;

        List<RoomRate> roomRates = roomType.getRoomRates();

        // Prioritize Promotion Rate > Peak Rate > Normal Rate > Published Rate
        for (RoomRate rate : roomRates) {
            if (isDateWithinRange(date, rate.getStartDate(), rate.getEndDate())) {
                switch (rate.getRateType()) {
                    case PROMOTION:
                        return rate;
                    case PEAK:
                        if (highestPriorityRate == 0) selectedRate = rate;
                        break;
                    case NORMAL:
                        if (selectedRate == null || highestPriorityRate < 2) selectedRate = rate;
                        break;
                    case PUBLISHED:
                        if (selectedRate == null) selectedRate = rate;
                        break;
                }
            }
        }
        return selectedRate;
    }

    private boolean isDateWithinRange(Date date, Date startDate, Date endDate) {
        return (date.equals(startDate) || date.after(startDate)) && (date.equals(endDate) || date.before(endDate));
    }

    private Date getNextDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
}
