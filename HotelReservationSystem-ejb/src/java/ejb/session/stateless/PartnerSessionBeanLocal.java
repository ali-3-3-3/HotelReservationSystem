package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Local;
import util.exceptions.InputDataValidationException;
import util.exceptions.InvalidLoginCredentialException;
import util.exceptions.PartnerExistException;
import util.exceptions.PartnerNotFoundException;
import util.exceptions.UnknownPersistenceException;

@Local
public interface PartnerSessionBeanLocal {
    public Partner createNewPartner(Partner partner) throws InputDataValidationException, PartnerExistException, UnknownPersistenceException;
    
    public Partner retrievePartnerByEmail(String email) throws PartnerNotFoundException;
    
    public Partner doLogin(String email, String password) throws InvalidLoginCredentialException;

    public Partner retrievePartnerById(Long id) throws PartnerNotFoundException;

    public List<Partner> viewAllPartners();
}
