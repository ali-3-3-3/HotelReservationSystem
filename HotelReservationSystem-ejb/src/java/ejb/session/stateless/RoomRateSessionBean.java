package ejb.session.stateless;

import entity.Reservation;
import entity.RoomRate;
import java.util.Date;
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
import util.exceptions.RoomRateDeleteException;
import util.exceptions.RoomRateExistException;
import util.exceptions.RoomRateNotFoundException;
import util.exceptions.RoomRateUpdateException;
import util.exceptions.UnknownPersistenceException;

@Stateless
public class RoomRateSessionBean implements RoomRateSessionBeanRemote, RoomRateSessionBeanLocal {
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public RoomRateSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    @Transactional
    public RoomRate createRoomRate(RoomRate roomRate) throws InputDataValidationException, RoomRateExistException, UnknownPersistenceException {
        Set<ConstraintViolation<RoomRate>> constraintViolations = validator.validate(roomRate);

        try {
            if (constraintViolations.isEmpty()) {
                em.persist(roomRate);
                em.flush();
                return roomRate;
            } else {
                throw new InputDataValidationException();
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new RoomRateExistException("Room rate already exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public RoomRate retrieveRoomRateById(Long roomRateId) throws RoomRateNotFoundException {
        RoomRate roomRate = em.find(RoomRate.class, roomRateId);
        if (roomRate == null) {
            throw new RoomRateNotFoundException("RoomRate with ID " + roomRateId + " not found.");
        }
        return roomRate;
    }

    @Override
    @Transactional
    public RoomRate updateRoomRate(Long roomRateId, RoomRate updatedRoomRate) throws RoomRateNotFoundException, RoomRateUpdateException {
        RoomRate roomRate = retrieveRoomRateById(roomRateId); // Reuse retrieveRoomRateById for not found exception

        try {
            roomRate.setRateType(updatedRoomRate.getRateType());
            roomRate.setStartDate(updatedRoomRate.getStartDate());
            roomRate.setEndDate(updatedRoomRate.getEndDate());
            roomRate.setRatePerNight(updatedRoomRate.getRatePerNight());
            roomRate.setRoomType(updatedRoomRate.getRoomType());

            em.merge(roomRate);
            return roomRate;
        } catch (PersistenceException ex) {
            throw new RoomRateUpdateException("Failed to update RoomRate: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteRoomRate(Long roomRateId) throws RoomRateNotFoundException {
        RoomRate roomRate = em.find(RoomRate.class, roomRateId);

        if (roomRate == null) {
            throw new RoomRateNotFoundException("RoomRate not found with ID: " + roomRateId);
        }

        // Unlink the relationship
        roomRate.getReservations().forEach(reservation -> {
            reservation.getRoomRates().remove(roomRate);
        });

        roomRate.getReservations().clear();

        em.remove(roomRate);
    }

    
    @Override
    public List<RoomRate> retrieveAllRoomRates() {
        Query query = em.createQuery("SELECT rr FROM RoomRate rr");
        return query.getResultList();
    }
    
    @Override
    public List<RoomRate> getRoomRatesForRoomType(Long roomTypeId, Date checkInDate, Date checkOutDate) {
        Query query = em.createQuery("SELECT rr FROM RoomRate rr WHERE rr.roomType.roomTypeId = :roomTypeId "
                + "AND ((rr.startDate IS NULL AND rr.endDate IS NULL) "
                + "OR (rr.startDate <= :checkOutDate AND rr.endDate >= :checkInDate))");

        query.setParameter("roomTypeId", roomTypeId);
        query.setParameter("checkInDate", checkInDate);
        query.setParameter("checkOutDate", checkOutDate);

        return query.getResultList();
    }
}