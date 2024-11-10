/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Remote;
import util.exceptions.InputDataValidationException;
import util.exceptions.InvalidLoginCredentialException;
import util.exceptions.PartnerExistException;
import util.exceptions.PartnerNotFoundException;
import util.exceptions.UnknownPersistenceException;

/**
 *
 * @author aliya
 */
@Remote
public interface PartnerSessionBeanRemote {

    public Partner createNewPartner(Partner partner) throws InputDataValidationException, PartnerExistException, UnknownPersistenceException;

    public Partner retrievePartnerByEmail(String email) throws PartnerNotFoundException;

    public Partner doLogin(String email, String password) throws InvalidLoginCredentialException;

    public Partner retrievePartnerById(Long id) throws PartnerNotFoundException;
    
    public List<Partner> viewAllPartners();
}
