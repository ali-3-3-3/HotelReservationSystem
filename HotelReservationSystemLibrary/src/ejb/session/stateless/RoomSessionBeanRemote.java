package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exceptions.InputDataValidationException;
import util.exceptions.RoomDeleteException;
import util.exceptions.RoomExistException;
import util.exceptions.RoomNotFoundException;
import util.exceptions.RoomUpdateException;
import util.exceptions.UnknownPersistenceException;

@Remote
public interface RoomSessionBeanRemote {

    public Room createRoom(Room room) throws InputDataValidationException, RoomExistException, UnknownPersistenceException;

    public Room retrieveRoomById(Long roomId) throws RoomNotFoundException;

    public Room updateRoom(Long roomId, Room updatedRoom) throws RoomNotFoundException, RoomUpdateException;

    public void deleteRoom(Long roomId) throws RoomNotFoundException, RoomDeleteException;

    public List<Room> viewAllRooms();
    
    public List<RoomType> searchAvailableRoomTypes(Date checkInDate, Date checkOutDate);

    public List<Room> retrieveAvailableRoomsByRoomType(RoomType roomType);
    
}
