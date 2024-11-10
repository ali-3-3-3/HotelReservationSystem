package ejb.session.stateless;

import entity.RoomType;
import javax.ejb.Remote;
import util.exceptions.InputDataValidationException;
import util.exceptions.RoomTypeDeleteException;
import util.exceptions.RoomTypeExistException;
import util.exceptions.RoomTypeNotFoundException;
import util.exceptions.RoomTypeUpdateException;
import util.exceptions.UnknownPersistenceException;

@Remote
public interface RoomTypeSessionBeanRemote {

    public RoomType createNewRoomType(RoomType roomType) throws InputDataValidationException, RoomTypeExistException, UnknownPersistenceException;

    public RoomType retrieveRoomTypeById(Long id) throws RoomTypeNotFoundException;

    public RoomType updateRoomType(Long roomTypeId, RoomType updatedRoomType) throws RoomTypeNotFoundException, RoomTypeUpdateException;

    public void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException, RoomTypeDeleteException;
    
}
