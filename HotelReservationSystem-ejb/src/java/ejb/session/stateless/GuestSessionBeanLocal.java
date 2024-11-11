package ejb.session.stateless;

import entity.Guest;
import java.util.List;
import javax.ejb.Local;
import util.exceptions.GuestDeleteException;
import util.exceptions.GuestExistException;
import util.exceptions.GuestNotFoundException;
import util.exceptions.InputDataValidationException;
import util.exceptions.UnknownPersistenceException;

@Local
public interface GuestSessionBeanLocal {

    public Guest createNewGuest(Guest newGuest) throws GuestExistException, UnknownPersistenceException, InputDataValidationException;

    public List<Guest> retrieveAllGuests();

    public Guest retrieveGuestByGuestId(Long guestId) throws GuestNotFoundException;

    public Guest updateGuest(Long guestId, String name, String email, String phoneNumber) throws GuestNotFoundException, InputDataValidationException;

    public void deleteGuest(Long guestId) throws GuestNotFoundException, GuestDeleteException;
    
}
