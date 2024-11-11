package ejb.session.stateless;

import entity.RoomAllocation;
import java.util.List;
import javax.ejb.Local;
import util.exceptions.InputDataValidationException;
import util.exceptions.RoomAllocationDeleteException;
import util.exceptions.RoomAllocationNotFoundException;
import util.exceptions.RoomAllocationUpdateException;
import util.exceptions.UnknownPersistenceException;

@Local
public interface RoomAllocationSessionBeanLocal {

    public RoomAllocation createRoomAllocation(RoomAllocation roomAllocation) throws InputDataValidationException, UnknownPersistenceException;

    public RoomAllocation retrieveRoomAllocationById(Long allocationId) throws RoomAllocationNotFoundException;

    public RoomAllocation updateRoomAllocation(Long allocationId, RoomAllocation updatedRoomAllocation) throws RoomAllocationNotFoundException, RoomAllocationUpdateException;

    public void deleteRoomAllocation(Long allocationId) throws RoomAllocationNotFoundException, RoomAllocationDeleteException;

    public List<RoomAllocation> retrieveRoomAllocationsByReservationId(Long reservationId);

    public List<RoomAllocation> retrieveRoomAllocationsByRoomId(Long roomId);
    
}
