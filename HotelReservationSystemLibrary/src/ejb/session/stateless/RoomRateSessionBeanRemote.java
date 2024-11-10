package ejb.session.stateless;

import entity.RoomRate;
import javax.ejb.Remote;
import util.exceptions.InputDataValidationException;
import util.exceptions.RoomRateDeleteException;
import util.exceptions.RoomRateExistException;
import util.exceptions.RoomRateNotFoundException;
import util.exceptions.RoomRateUpdateException;
import util.exceptions.UnknownPersistenceException;

@Remote
public interface RoomRateSessionBeanRemote {

    public RoomRate createRoomRate(RoomRate roomRate) throws InputDataValidationException, RoomRateExistException, UnknownPersistenceException;

    public RoomRate retrieveRoomRateById(Long roomRateId) throws RoomRateNotFoundException;

    public RoomRate updateRoomRate(Long roomRateId, RoomRate updatedRoomRate) throws RoomRateNotFoundException, RoomRateUpdateException;

    public void deleteRoomRate(Long roomRateId) throws RoomRateNotFoundException, RoomRateDeleteException;
    
}