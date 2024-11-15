package ejb.session.stateless;

import entity.Reservation;
import entity.Room;
import entity.RoomAllocation;
import entity.RoomType;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;
import util.exceptions.InputDataValidationException;
import util.exceptions.ReservationAddRoomAllocationException;
import util.exceptions.ReservationNotFoundException;
import util.exceptions.ReservationUpdateException;
import util.exceptions.RoomAllocationDeleteException;
import util.exceptions.RoomAllocationException;
import util.exceptions.RoomAllocationNotFoundException;
import util.exceptions.RoomAllocationUpdateException;
import util.exceptions.UnknownPersistenceException;

@Remote
public interface RoomAllocationSessionBeanRemote {
    public RoomAllocation createRoomAllocation(RoomAllocation roomAllocation) throws InputDataValidationException, UnknownPersistenceException;

    public RoomAllocation retrieveRoomAllocationById(Long allocationId) throws RoomAllocationNotFoundException;

    public RoomAllocation updateRoomAllocation(Long allocationId, RoomAllocation updatedRoomAllocation) throws RoomAllocationNotFoundException, RoomAllocationUpdateException;

    public void deleteRoomAllocation(Long allocationId) throws RoomAllocationNotFoundException, RoomAllocationDeleteException;

    public List<RoomAllocation> retrieveRoomAllocationsByReservationId(Long reservationId);

    public List<RoomAllocation> retrieveRoomAllocationsByRoomId(Long roomId);

    public void allocateRoomsForWalkInReservation(Reservation reservation) throws RoomAllocationException, ReservationAddRoomAllocationException, ReservationNotFoundException, ReservationUpdateException;

    public void allocateRooms(Date checkInDate) throws RoomAllocationNotFoundException, RoomAllocationUpdateException, ReservationAddRoomAllocationException, ReservationUpdateException;

    public Map<RoomType, List<Room>> initializeRoomAvailability();
}
