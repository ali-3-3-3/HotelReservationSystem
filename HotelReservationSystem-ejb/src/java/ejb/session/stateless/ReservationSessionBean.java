package ejb.session.stateless;

import entity.Guest;
import entity.Partner;
import entity.Reservation;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.util.Calendar;
import java.util.Date;
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
import util.enumerations.RateTypeEnum;
import util.enumerations.RoomStatusEnum;
import util.exceptions.GuestNotFoundException;
import util.exceptions.InputDataValidationException;
import util.exceptions.InvalidRoomCountException;
import util.exceptions.ReservationNotFoundException;
import util.exceptions.ReservationUpdateException;
import util.exceptions.RoomTypeUnavailableException;
import util.exceptions.UnknownPersistenceException;

@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {
    
    @EJB(name = "GuestSessionBeanLocal")
    private GuestSessionBeanLocal guestSessionBeanLocal;
    
    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBeanLocal;
    
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
            constraintViolations.forEach(violation -> {
                errorMsg.append("\n- ").append(violation.getPropertyPath()).append(": ").append(violation.getMessage());
            });
            Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.SEVERE, errorMsg.toString());
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
    public Reservation createReservationFromSearchForPartner(
            Long customerId,
            RoomType roomType,
            Date checkInDate,
            Date checkOutDate,
            int roomCount,
            Partner partner)
            throws RoomTypeUnavailableException, InvalidRoomCountException, InputDataValidationException, UnknownPersistenceException {

        // Validate input
        if (roomCount < 1 || roomCount > 9) {
            Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.WARNING, "Invalid room count: {0}", roomCount);
            throw new InvalidRoomCountException("You can reserve between 1 and 9 rooms.");
        }
        
        Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.INFO,
                "Creating reservation with guestId: {0}, roomType: {1}, checkInDate: {2}, checkOutDate: {3}, roomCount: {4}, partner: {5}",
                new Object[]{customerId, roomType.getRoomTypeId(), checkInDate, checkOutDate, roomCount, partner.getSystemName()});
        
        try {
            // Retrieve guest
            Guest guest = guestSessionBeanLocal.retrieveGuestByGuestId(customerId);
            Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.INFO, "Guest retrieved: {0}", guest);

            // Check for available rooms
            List<Room> availableRooms = em.createQuery(
                    "SELECT r FROM Room r WHERE r.roomType = :roomType AND r.roomStatus = :availableStatus", Room.class)
                    .setParameter("roomType", roomType)
                    .setParameter("availableStatus", RoomStatusEnum.AVAILABLE)
                    .getResultList();
            
            Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.INFO, "Available rooms count: {0}", availableRooms.size());
            
            if (availableRooms.size() < roomCount) {
                Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.WARNING,
                        "Requested room type has only {0} rooms available, but {1} rooms requested.",
                        new Object[]{availableRooms.size(), roomCount});
                throw new RoomTypeUnavailableException("Not enough rooms available for the selected room type.");
            }

            // Retrieve room rates
            List<RoomRate> roomRates = roomRateSessionBeanLocal.getRoomRatesForRoomType(
                    roomType.getRoomTypeId(), checkInDate, checkOutDate);
            
            Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.INFO, "Room rates retrieved: {0}", roomRates);

            // Create reservation
            Reservation reservation = new Reservation(new Date(), checkInDate, checkOutDate, roomCount);
            reservation.setPartner(partner);
            reservation.setGuest(guest);
            reservation.setRoomType(roomType);
            reservation.setRoomRates(roomRates);
            
            
            Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.INFO, "Reservation initialized: {0}", reservation);

            // Persist reservation
            Reservation createdReservation = createReservation(reservation);
            Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.INFO, "Reservation successfully created: {0}", createdReservation);
            
            return createdReservation;
            
        } catch (GuestNotFoundException ex) {
            Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.SEVERE, "Guest not found: {0}", ex.getMessage());
            throw new InputDataValidationException("Guest not found. Please check the guest ID.");
        } catch (Exception ex) {
            Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.SEVERE, "Error creating reservation: {0}", ex.getMessage());
            throw new UnknownPersistenceException("An unexpected error occurred while creating the reservation: " + ex.getMessage());
        }
        
    }
    
    @Override
    public Reservation createReservationFromSearch(
            Long customerId,
            RoomType roomType,
            Date checkInDate,
            Date checkOutDate,
            int roomCount)
            throws RoomTypeUnavailableException, InvalidRoomCountException, InputDataValidationException, UnknownPersistenceException {

        // Validate input
        if (roomCount < 1 || roomCount > 9) {
            Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.WARNING, "Invalid room count: {0}", roomCount);
            throw new InvalidRoomCountException("You can reserve between 1 and 9 rooms.");
        }
        
        Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.INFO,
                "Creating reservation with guestId: {0}, roomType: {1}, checkInDate: {2}, checkOutDate: {3}, roomCount: {4}",
                new Object[]{customerId, roomType.getRoomTypeId(), checkInDate, checkOutDate, roomCount});
        
        try {
            // Retrieve guest
            Guest guest = guestSessionBeanLocal.retrieveGuestByGuestId(customerId);
            Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.INFO, "Guest retrieved: {0}", guest);

            // Check for available rooms
            List<Room> availableRooms = em.createQuery(
                    "SELECT r FROM Room r WHERE r.roomType = :roomType AND r.roomStatus = :availableStatus", Room.class)
                    .setParameter("roomType", roomType)
                    .setParameter("availableStatus", RoomStatusEnum.AVAILABLE)
                    .getResultList();
            
            Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.INFO, "Available rooms count: {0}", availableRooms.size());
            
            if (availableRooms.size() < roomCount) {
                Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.WARNING,
                        "Requested room type has only {0} rooms available, but {1} rooms requested.",
                        new Object[]{availableRooms.size(), roomCount});
                throw new RoomTypeUnavailableException("Not enough rooms available for the selected room type.");
            }

            // Retrieve room rates
            List<RoomRate> roomRates = roomRateSessionBeanLocal.getRoomRatesForRoomType(
                    roomType.getRoomTypeId(), checkInDate, checkOutDate);
            
            Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.INFO, "Room rates retrieved: {0}", roomRates);

            // Create reservation
            Reservation reservation = new Reservation(new Date(), checkInDate, checkOutDate, roomCount);
            reservation.setGuest(guest);
            reservation.setRoomType(roomType);
            reservation.setRoomRates(roomRates);
            
            Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.INFO, "Reservation initialized: {0}", reservation);

            // Persist reservation
            Reservation createdReservation = createReservation(reservation);
            Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.INFO, "Reservation successfully created: {0}", createdReservation);
            
            return createdReservation;
            
        } catch (GuestNotFoundException ex) {
            Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.SEVERE, "Guest not found: {0}", ex.getMessage());
            throw new InputDataValidationException("Guest not found. Please check the guest ID.");
        } catch (Exception ex) {
            Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.SEVERE, "Error creating reservation: {0}", ex.getMessage());
            throw new UnknownPersistenceException("An unexpected error occurred while creating the reservation: " + ex.getMessage());
        }
        
    }
    
    @Transactional
    @Override
    public void checkInReservation(Long reservationId) throws ReservationNotFoundException, RoomTypeUnavailableException {
        Reservation reservation = em.find(Reservation.class, reservationId);
        
        RoomType roomType = reservation.getRoomType();
        RoomType managedRoomType = em.find(RoomType.class, roomType.getRoomTypeId());
        
        if (managedRoomType.getAvailableRoomsCount() < reservation.getNumOfRooms()) {
            throw new RoomTypeUnavailableException("Insufficient rooms available for this room type.");
        }
        
        managedRoomType.decrementAvailableRoomsCount(reservation.getNumOfRooms());
    }
    
    @Transactional
    @Override
    public void checkOutReservation(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = retrieveReservationById(reservationId);
        RoomType roomType = reservation.getRoomType();
        
        roomType.incrementAvailableRoomsCount(reservation.getNumOfRooms());
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

        // Check if the reservation object has any validation violations
        Set<ConstraintViolation<Reservation>> constraintViolations = validator.validate(updatedReservation);
        
        if (!constraintViolations.isEmpty()) {
            StringBuilder errorMsg = new StringBuilder("Input data validation error(s):");
            constraintViolations.forEach(violation -> {
                errorMsg.append("\n- ").append(violation.getPropertyPath()).append(": ").append(violation.getMessage());
            });
            throw new ReservationUpdateException(errorMsg.toString());
        }
        
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
    public void deleteReservation(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = em.find(Reservation.class, reservationId);
        
        if (reservation == null) {
            throw new ReservationNotFoundException("Reservation not found with ID: " + reservationId);
        }

        // Unlink the relationship
        reservation.getRoomRates().forEach(roomRate -> {
            roomRate.getReservations().remove(reservation);
        });
        
        reservation.getRoomRates().clear();
        
        em.remove(reservation);
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
    public List<Reservation> retrieveReservationsByCheckInDate(Date checkInDate) throws ReservationNotFoundException {
        // Constructing a query to retrieve all reservations that match the check-in date
        String jpql = "SELECT r FROM Reservation r WHERE r.checkInDate = :checkInDate";
        Query query = em.createQuery(jpql);
        query.setParameter("checkInDate", checkInDate);

        // Execute the query and retrieve the list of reservations
        List<Reservation> reservations = query.getResultList();

        // If no reservations are found, throw an exception
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException("No reservations found for the given check-in date: " + checkInDate);
        }
        
        return reservations;
    }
    
    @Override
    @Transactional
    public double calculateTotalReservationFee(Date checkInDate, Date checkOutDate, RoomType roomType, Reservation reservation) {
        double totalFee = 0.0;
        Date currentDate = checkInDate;
        
        while (!currentDate.after(checkOutDate)) {
            RoomRate dailyRate = getDailyRateForRoomType(currentDate, roomType);
            if (dailyRate != null) {
                totalFee += dailyRate.getRatePerNight();
            }
            currentDate = getNextDate(currentDate);
        }
        
        return totalFee;
    }
    
    @Override
    @Transactional
    public double calculateTotalReservationFeeForWalkIn(Date checkInDate, Date checkOutDate, RoomType roomType, int roomCount) {
        double totalFee = 0.0;
        
        RoomRate publishedRate = getPublishedRateForRoomType(roomType, checkInDate);
        
        if (publishedRate == null) {
            Logger.getLogger(ReservationSessionBean.class.getName())
                    .log(Level.WARNING, "No Published Rate available for room type: " + roomType.getName());
            return 0.0;            
        }
        
        long durationInMillis = checkOutDate.getTime() - checkInDate.getTime();
        long durationInDays = durationInMillis / (1000 * 60 * 60 * 24);
        
        totalFee = durationInDays * publishedRate.getRatePerNight() * roomCount;
        
        return totalFee;
    }
    
    @Override
    @Transactional
    public double calculateTotalReservationFeeForWalkInPre(Date checkInDate, Date checkOutDate, RoomType roomType) {
        double totalFee = 0.0;
        
        RoomRate publishedRate = getPublishedRateForRoomType(roomType, checkInDate);
        
        if (publishedRate == null) {
            Logger.getLogger(ReservationSessionBean.class.getName())
                    .log(Level.WARNING, "No Published Rate available for room type: " + roomType.getName());
            return 0.0;            
        }
        
        long durationInMillis = checkOutDate.getTime() - checkInDate.getTime();
        long durationInDays = durationInMillis / (1000 * 60 * 60 * 24);
        
        totalFee = durationInDays * publishedRate.getRatePerNight();
        
        return totalFee;
    }
    
    public RoomRate getPublishedRateForRoomType(RoomType roomType, Date date) {
        List<RoomRate> roomRates = roomType.getRoomRates();
        
        for (RoomRate rate : roomRates) {
            if (rate.getRateType() == RateTypeEnum.PUBLISHED && isDateWithinRange(date, rate.getStartDate(), rate.getEndDate())) {
                return rate;
            }
        }
        
        return null;
    }
    
    private boolean isDateWithinRange(Date date, Date startDate, Date endDate) {
        return (date.equals(startDate) || date.after(startDate)) && (date.equals(endDate) || date.before(endDate));
    }
    
    private RoomRate getDailyRateForRoomType(Date date, RoomType roomType) {
        RoomRate selectedRate = null;
        double highestPriorityRate = 0.0;
        
        List<RoomRate> roomRates = roomType.getRoomRates();
        
        for (RoomRate rate : roomRates) {
            if (isDateWithinRange(date, rate.getStartDate(), rate.getEndDate())) {
                switch (rate.getRateType()) {
                    case PROMOTION:
                        return rate;
                    case PEAK:
                        if (highestPriorityRate == 0) {
                            selectedRate = rate;
                        }
                        break;
                    case NORMAL:
                        if (selectedRate == null || highestPriorityRate < 2) {
                            selectedRate = rate;
                        }
                        break;
                    case PUBLISHED:
                        if (selectedRate == null) {
                            selectedRate = rate;
                        }
                        break;
                }
            }
        }
        return selectedRate;
    }
    
    private Date getNextDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
    
    @Override
    public int countReservationsByRoomTypeAndDates(RoomType roomType, Date checkInDate, Date checkOutDate) {
        // JPQL query to count the number of reservations for a specific room type within a date range
        String jpql = "SELECT COUNT(r) FROM Reservation r "
                + "WHERE r.roomType = :roomType "
                + "AND ((r.checkInDate BETWEEN :checkInDate AND :checkOutDate) "
                + "OR (r.checkOutDate BETWEEN :checkInDate AND :checkOutDate) "
                + "OR (r.checkInDate <= :checkInDate AND r.checkOutDate >= :checkOutDate))";
        
        Query query = em.createQuery(jpql);
        query.setParameter("roomType", roomType);
        query.setParameter("checkInDate", checkInDate);
        query.setParameter("checkOutDate", checkOutDate);
        
        return ((Long) query.getSingleResult()).intValue();  // Returns the count as an integer
    }
    
}
