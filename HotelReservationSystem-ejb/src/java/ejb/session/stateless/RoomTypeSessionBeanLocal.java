package ejb.session.stateless;

import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exceptions.InputDataValidationException;
import util.exceptions.RoomTypeDeleteException;
import util.exceptions.RoomTypeExistException;
import util.exceptions.RoomTypeNotFoundException;
import util.exceptions.RoomTypeUpdateException;
import util.exceptions.UnknownPersistenceException;

@Local
public interface RoomTypeSessionBeanLocal {
     public RoomType createNewRoomType(RoomType roomType) throws InputDataValidationException, RoomTypeExistException, UnknownPersistenceException;

    public RoomType retrieveRoomTypeById(Long id) throws RoomTypeNotFoundException;

    public RoomType updateRoomType(Long roomTypeId, RoomType updatedRoomType) throws RoomTypeNotFoundException, RoomTypeUpdateException;

    public void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException, RoomTypeDeleteException;

    public List<RoomType> getAllRoomTypes();

    public List<RoomType> retrieveAvailableRooms(Date checkInDate, Date checkOutDate) throws RoomTypeNotFoundException;

    public RoomType retrieveRoomTypeByName(String name) throws RoomTypeNotFoundException;
}
