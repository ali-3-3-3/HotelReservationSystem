package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exceptions.InputDataValidationException;
import util.exceptions.RoomDeleteException;
import util.exceptions.RoomExistException;
import util.exceptions.RoomNotFoundException;
import util.exceptions.RoomUpdateException;
import util.exceptions.UnknownPersistenceException;

@Stateless
public class RoomSessionBean implements RoomSessionBeanRemote, RoomSessionBeanLocal {
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public RoomSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    @Transactional
    public Room createRoom(Room room) throws InputDataValidationException, RoomExistException, UnknownPersistenceException {
        Set<ConstraintViolation<Room>>constraintViolations = validator.validate(room);
        try {
            if (constraintViolations.isEmpty()){
            em.persist(room);
            em.flush();
            return room;
            } else {
                 throw new InputDataValidationException();
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new RoomExistException("Room type already exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public Room retrieveRoomById(Long roomId) throws RoomNotFoundException {
        Room room = em.find(Room.class, roomId);
        if (room == null) {
            throw new RoomNotFoundException("Room with ID " + roomId + " not found.");
        }
        return room;
    }

    @Override
    @Transactional
    public Room updateRoom(Long roomId, Room updatedRoom) throws RoomNotFoundException, RoomUpdateException {
        Room room = retrieveRoomById(roomId); // Reuse retrieveRoomById to handle not found exception

        try {
            room.setRoomNumber(updatedRoom.getRoomNumber());
            room.setFloorNumber(updatedRoom.getFloorNumber());
            room.setRoomType(updatedRoom.getRoomType());
            room.setIsClean(updatedRoom.isIsClean());
            room.setRoomStatus(updatedRoom.getRoomStatus());

            em.merge(room);
            return room;
        } catch (PersistenceException e) {
            throw new RoomUpdateException("Failed to update Room: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteRoom(Long roomId) throws RoomNotFoundException, RoomDeleteException {
        Room room = retrieveRoomById(roomId); // Reuse retrieveRoomById to handle not found exception

        try {
            em.remove(room);
        } catch (PersistenceException e) {
            throw new RoomDeleteException("Failed to delete Room: " + e.getMessage());
        }
    }

    @Override
    public List<Room> viewAllRooms() {
        return em.createQuery("SELECT r FROM Room r", Room.class).getResultList();
    }
    
    @Override
    public List<RoomType> searchAvailableRoomTypes(Date checkInDate, Date checkOutDate) {
        List<Room> rooms = em.createQuery("SELECT r FROM Room r", Room.class).getResultList();

        Set<RoomType> availableRoomTypes = new HashSet<>();

        for (Room room : rooms) {
            boolean isAvailable = room.getRoomAllocations().stream().noneMatch(roomAllocation -> {
                Date allocationStart = roomAllocation.getReservation().getCheckInDate();
                Date allocationEnd = roomAllocation.getReservation().getCheckOutDate();
                return (checkInDate.before(allocationEnd) && checkOutDate.after(allocationStart));
            });

            if (isAvailable) {
                availableRoomTypes.add(room.getRoomType());
            }
        }

        return new ArrayList<>(availableRoomTypes);
    }
}
