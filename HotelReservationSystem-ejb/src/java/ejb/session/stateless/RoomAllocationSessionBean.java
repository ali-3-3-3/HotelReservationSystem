package ejb.session.stateless;

import entity.RoomAllocation;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.Validation;
import util.exceptions.InputDataValidationException;
import util.exceptions.RoomAllocationDeleteException;
import util.exceptions.RoomAllocationNotFoundException;
import util.exceptions.RoomAllocationUpdateException;
import util.exceptions.UnknownPersistenceException;

@Stateless
public class RoomAllocationSessionBean implements RoomAllocationSessionBeanRemote, RoomAllocationSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public RoomAllocationSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    @Transactional
    public RoomAllocation createRoomAllocation(RoomAllocation roomAllocation) throws InputDataValidationException, UnknownPersistenceException {
        Set<ConstraintViolation<RoomAllocation>> constraintViolations = validator.validate(roomAllocation);

        if (!constraintViolations.isEmpty()) {
            StringBuilder errorMsg = new StringBuilder("Input data validation error(s):");
            for (ConstraintViolation<RoomAllocation> violation : constraintViolations) {
                errorMsg.append("\n- ").append(violation.getPropertyPath()).append(": ").append(violation.getMessage());
            }
            throw new InputDataValidationException(errorMsg.toString());
        }
        
        try {
            em.persist(roomAllocation);
            em.flush();
            return roomAllocation;
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                throw new UnknownPersistenceException("Database error: " + ex.getMessage());
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public RoomAllocation retrieveRoomAllocationById(Long allocationId) throws RoomAllocationNotFoundException {
        RoomAllocation roomAllocation = em.find(RoomAllocation.class, allocationId);
        if (roomAllocation == null) {
            throw new RoomAllocationNotFoundException("Room Allocation with ID " + allocationId + " not found.");
        }
        return roomAllocation;
    }
    
    @Override
    @Transactional
    public RoomAllocation updateRoomAllocation(Long allocationId, RoomAllocation updatedRoomAllocation) throws RoomAllocationNotFoundException, RoomAllocationUpdateException {
        RoomAllocation roomAllocation = retrieveRoomAllocationById(allocationId);

        try {
            roomAllocation.setAllocationDate(updatedRoomAllocation.getAllocationDate());
            roomAllocation.setRoom(updatedRoomAllocation.getRoom());
            roomAllocation.setReservation(updatedRoomAllocation.getReservation());
            roomAllocation.setIsException(updatedRoomAllocation.isIsException());
            roomAllocation.setException(updatedRoomAllocation.getException());

            em.merge(roomAllocation);
            return roomAllocation;
        } catch (PersistenceException ex) {
            throw new RoomAllocationUpdateException("Failed to update Room Allocation: " + ex.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void deleteRoomAllocation(Long allocationId) throws RoomAllocationNotFoundException, RoomAllocationDeleteException {
        RoomAllocation roomAllocation = retrieveRoomAllocationById(allocationId);

        try {
            em.remove(roomAllocation);
        } catch (PersistenceException ex) {
            throw new RoomAllocationDeleteException("Failed to delete Room Allocation: " + ex.getMessage());
        }
    }
    
    @Override
    public List<RoomAllocation> retrieveRoomAllocationsByReservationId(Long reservationId) {
        Query query = em.createQuery("SELECT r FROM RoomAllocation r WHERE r.reservation.reservationId = :reservationId");
        query.setParameter("reservationId", reservationId);
        return query.getResultList();
    }
    
    @Override
    public List<RoomAllocation> retrieveRoomAllocationsByRoomId(Long roomId) {
        Query query = em.createQuery("SELECT r FROM RoomAllocation r WHERE r.room.roomId = :roomId");
        query.setParameter("roomId", roomId);
        return query.getResultList();
    }
    
}
