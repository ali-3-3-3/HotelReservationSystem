package ejb.session.stateless;

import entity.AllocationException;
import entity.Reservation;
import entity.Room;
import entity.RoomAllocation;
import entity.RoomType;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.Validation;
import util.enumerations.ResolutionStatusEnum;
import util.exceptions.AllocationExceptionExistException;
import util.exceptions.InputDataValidationException;
import util.exceptions.ReservationAddRoomAllocationException;
import util.exceptions.ReservationNotFoundException;
import util.exceptions.ReservationUpdateException;
import util.exceptions.RoomAllocationDeleteException;
import util.exceptions.RoomAllocationException;
import util.exceptions.RoomAllocationNotFoundException;
import util.exceptions.RoomAllocationUpdateException;
import util.exceptions.UnknownPersistenceException;

@Stateless
public class RoomAllocationSessionBean implements RoomAllocationSessionBeanRemote, RoomAllocationSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;
    @EJB
    private RoomSessionBeanLocal roomSessionBean;
    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBean;
    @EJB
    private AllocationExceptionSessionBeanLocal allocationExceptionSessionBean;
    

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
            constraintViolations.forEach(violation -> {
                errorMsg.append("\n- ").append(violation.getPropertyPath()).append(": ").append(violation.getMessage());
            });
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
    
    @Override
    public void allocateRooms(Date checkInDate) throws RoomAllocationNotFoundException, RoomAllocationUpdateException, ReservationAddRoomAllocationException, ReservationUpdateException {
        try {
            // Retrieve reservations for the check-in date
            List<Reservation> reservations = reservationSessionBean.retrieveReservationsByCheckInDate(checkInDate);

            // Map to hold available rooms for each room type
            Map<RoomType, List<Room>> availableRoomsByType = initializeRoomAvailability();

            // Iterate over each reservation and attempt room allocation
            for (Reservation reservation : reservations) {
                RoomType reservedRoomType = reservation.getRoomType();
                RoomAllocation roomAllocation = new RoomAllocation(new Date());
                roomAllocation.setReservation(reservation);
                
                try {
                    Room allocatedRoom = allocateRoom(reservedRoomType, availableRoomsByType);
                    roomAllocation.setRoom(allocatedRoom);
                    reservation.addRoomAllocation(roomAllocation);
                    reservationSessionBean.updateReservation(reservation.getReservationId(), reservation);
                    System.out.println("Allocated room: " + allocatedRoom.getRoomNumber() + " for reservation " + reservation.getReservationId());
                } catch (ReservationAddRoomAllocationException | ReservationNotFoundException | ReservationUpdateException | RoomAllocationException ex) {
                    Logger.getLogger(RoomAllocationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                    AllocationException allocationException = new AllocationException(ex.getMessage(), ResolutionStatusEnum.PENDING);
                    allocationException.setRoomAllocation(roomAllocation);
                    roomAllocation.setIsException(true);
                    roomAllocation.setException(allocationException);
                    updateRoomAllocation(roomAllocation.getAllocationId(), roomAllocation);
                    reservation.addRoomAllocation(roomAllocation);
                    reservationSessionBean.updateReservation(reservation.getReservationId(), reservation);
                    allocationExceptionSessionBean.createAllocationException(allocationException);
                }
            }
        } catch (AllocationExceptionExistException | InputDataValidationException | ReservationNotFoundException | UnknownPersistenceException ex) {
            Logger.getLogger(RoomAllocationSessionBean.class.getName()).log(Level.SEVERE, "Error during room allocation", ex);
        }
    }

    private Map<RoomType, List<Room>> initializeRoomAvailability() {
        Map<RoomType, List<Room>> availableRoomsByType = new HashMap<>();
        List<RoomType> roomTypes = roomTypeSessionBean.getAllRoomTypes();

        roomTypes.forEach(roomType -> {
            List<Room> availableRooms = roomSessionBean.retrieveAvailableRoomsByRoomType(roomType);
            availableRoomsByType.put(roomType, availableRooms);
        });
        
        return availableRoomsByType;
    }

    private Room allocateRoom(RoomType roomType, Map<RoomType, List<Room>> availableRoomsByType) throws RoomAllocationException {
        List<Room> rooms = availableRoomsByType.get(roomType);

        // If rooms are available in the desired room type
        if (rooms != null && !rooms.isEmpty()) {
            return rooms.remove(0);
        } else if (roomType.getNextHigherRoomType() != null) {
            // Try the next higher room type if available
            return allocateRoom(roomType.getNextHigherRoomType(), availableRoomsByType);
        } else {
            // If no room is available at any level, throw an exception
            throw new RoomAllocationException("Failed to allocate room. Creating allocation exception report.");
        }
    }
    
    @Override
    public void allocateRoomsForWalkInReservation(Reservation reservation) throws RoomAllocationException, ReservationAddRoomAllocationException, ReservationNotFoundException, ReservationUpdateException {
        // Get the room type and the number of rooms requested for the reservation
        // Map to hold available rooms for each room type
        Map<RoomType, List<Room>> availableRoomsByType = initializeRoomAvailability();;

        RoomType reservedRoomType = reservation.getRoomType();
        RoomAllocation roomAllocation = new RoomAllocation(new Date());
        roomAllocation.setReservation(reservation);

        try {
            Room allocatedRoom = allocateRoom(reservedRoomType, availableRoomsByType);
            roomAllocation.setRoom(allocatedRoom);
            reservation.addRoomAllocation(roomAllocation);
            reservationSessionBean.updateReservation(reservation.getReservationId(), reservation);
            System.out.println("Allocated room: " + allocatedRoom.getRoomNumber() + " for reservation " + reservation.getReservationId());
        } catch (ReservationAddRoomAllocationException | ReservationNotFoundException | ReservationUpdateException  ex) {
            Logger.getLogger(RoomAllocationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            AllocationException allocationException = new AllocationException(ex.getMessage(), ResolutionStatusEnum.PENDING);
            allocationException.setRoomAllocation(roomAllocation);
            roomAllocation.setIsException(true);
            roomAllocation.setException(allocationException);
            try {
                updateRoomAllocation(roomAllocation.getAllocationId(), roomAllocation);
            } catch (RoomAllocationNotFoundException | RoomAllocationUpdateException ex1) {
                Logger.getLogger(RoomAllocationSessionBean.class.getName()).log(Level.SEVERE, null, ex1);
            }
            reservation.addRoomAllocation(roomAllocation);
            reservationSessionBean.updateReservation(reservation.getReservationId(), reservation);
            try {
                allocationExceptionSessionBean.createAllocationException(allocationException);
            } catch (InputDataValidationException | UnknownPersistenceException | AllocationExceptionExistException ex1) {
                Logger.getLogger(RoomAllocationSessionBean.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
}
