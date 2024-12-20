package ejb.session.stateless;

import entity.Reservation;
import entity.Room;
import entity.RoomType;
import java.util.Date;
import java.util.Set;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.Validation;
import util.exceptions.InputDataValidationException;
import util.exceptions.RoomTypeDeleteException;
import util.exceptions.RoomTypeExistException;
import util.exceptions.RoomTypeNotFoundException;
import util.exceptions.RoomTypeUpdateException;
import util.exceptions.UnknownPersistenceException;

@Stateless
public class RoomTypeSessionBean implements RoomTypeSessionBeanRemote, RoomTypeSessionBeanLocal {
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public RoomTypeSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    @Transactional
    public RoomType createNewRoomType(RoomType roomType) throws InputDataValidationException, RoomTypeExistException, UnknownPersistenceException {
        Set<ConstraintViolation<RoomType>>constraintViolations = validator.validate(roomType);
        try {
            if (constraintViolations.isEmpty()){
            em.persist(roomType);
            em.flush();
            return roomType;
            } else {
                 throw new InputDataValidationException();
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new RoomTypeExistException("Room type already exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public RoomType retrieveRoomTypeById(Long id) throws RoomTypeNotFoundException {
        RoomType roomType = em.find(RoomType.class, id);
        if(roomType != null) {
            return roomType;
        } else {
            throw new RoomTypeNotFoundException("RoomType id " + id.toString() + " does not exist!");
        }
    }
    
    @Override
    public RoomType retrieveRoomTypeByName(String name) throws RoomTypeNotFoundException {
        try {
        // Use a JPQL query to find RoomType by name
        RoomType roomType = em.createQuery("SELECT r FROM RoomType r WHERE r.name = :name", RoomType.class)
                              .setParameter("name", name)
                              .getSingleResult();
        return roomType;
        } catch (NoResultException e) {
            // Handle case where no RoomType was found
            throw new RoomTypeNotFoundException("RoomType with name " + name + " does not exist!");
        }
    }
    
    @Override
    @Transactional
    public RoomType updateRoomType(Long roomTypeId, RoomType updatedRoomType) throws RoomTypeNotFoundException, RoomTypeUpdateException {
        RoomType roomType = em.find(RoomType.class, roomTypeId);

        if (roomType == null) {
            throw new RoomTypeNotFoundException("RoomType with ID " + roomTypeId + " not found.");
        }

        try {
            roomType.setName(updatedRoomType.getName());
            roomType.setMaxOccupancy(updatedRoomType.getMaxOccupancy());
            roomType.setDescription(updatedRoomType.getDescription());
            roomType.setNextHigherRoomType(updatedRoomType.getNextHigherRoomType());

            em.merge(roomType);
            return roomType;
        } catch (PersistenceException e) {
            throw new RoomTypeUpdateException("Failed to update RoomType: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException, RoomTypeDeleteException {
        RoomType roomType = em.find(RoomType.class, roomTypeId);

        if (roomType == null) {
            throw new RoomTypeNotFoundException("RoomType with ID " + roomTypeId + " not found.");
        }
        
        // Set roomType to null for all associated rooms
        for (Room room : roomType.getRooms()) {
            room.setRoomType(null);
        }

        try {
            em.remove(roomType);
        } catch (PersistenceException e) {
            throw new RoomTypeDeleteException("Failed to delete RoomType: " + e.getMessage());
        }
    }
    
    @Override
    public List<RoomType> getAllRoomTypes() {
        // Constructing a query to retrieve all room types
        String jpql = "SELECT rt FROM RoomType rt";
        Query query = em.createQuery(jpql);

        return query.getResultList();
    }
    
    @Override
    public List<RoomType> retrieveAvailableRooms(Date checkInDate, Date checkOutDate) throws RoomTypeNotFoundException {
        TypedQuery<RoomType> query = em.createQuery(
            "SELECT rt FROM RoomType rt WHERE rt.id NOT IN (SELECT r.roomType.id FROM Reservation r WHERE r.checkInDate < :checkOutDate AND r.checkOutDate > :checkInDate)", 
            RoomType.class
        );
        query.setParameter("checkInDate", checkInDate);
        query.setParameter("checkOutDate", checkOutDate);

        List<RoomType> availableRoomTypes = query.getResultList();
        if (availableRoomTypes.isEmpty()) {
            throw new RoomTypeNotFoundException("No rooms available for the specified dates.");
        }

        availableRoomTypes.forEach(em::detach);  
        return availableRoomTypes;
    }
    

       
    
    
//     @Override
//    public List<RoomType> searchAvailableRoomTypeForReservation(Date checkinDate, Date checkoutDate, Integer numberOfRooms) throws NoRoomTypeAvaiableForReservationException, RoomTypeNotFoundException {
//       List<RoomType> roomTypeToReturn = new ArrayList<>();
//       
//    }
    

}
