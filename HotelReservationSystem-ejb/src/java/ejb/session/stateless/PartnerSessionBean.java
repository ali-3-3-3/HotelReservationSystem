package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exceptions.InputDataValidationException;
import util.exceptions.InvalidLoginCredentialException;
import util.exceptions.PartnerExistException;
import util.exceptions.PartnerNotFoundException;
import util.exceptions.UnknownPersistenceException;

@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public PartnerSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    @Transactional
    public Partner createNewPartner(Partner partner) throws InputDataValidationException, PartnerExistException, UnknownPersistenceException {
        Set<ConstraintViolation<Partner>>constraintViolations = validator.validate(partner);
        try {
            if (constraintViolations.isEmpty()){
            em.persist(partner);
            em.flush();
            return partner;
            } else {
                 throw new InputDataValidationException();
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new PartnerExistException("Partner with the same email exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
     
    @Override
    public Partner retrievePartnerByEmail(String email) throws PartnerNotFoundException {
        Query query = em.createQuery("SELECT p FROM Partner p WHERE p.email = :email");
        query.setParameter("email", email);
        
        try {
        return (Partner) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new PartnerNotFoundException("Partner does not exist!");
        }
    }
    
    @Override
    public Partner retrievePartnerById(Long id) throws PartnerNotFoundException {
        Partner partner = em.find(Partner.class, id);
        if(partner != null) {
            return partner;
        } else {
            throw new PartnerNotFoundException("Partner id " + id.toString() + " does not exist!");
        }
    }

    @Override
    public Partner doLogin(String email, String password) throws InvalidLoginCredentialException {
        try {
            Partner partner = retrievePartnerByEmail(email);
            
            if(partner.getPassword().equals(password)) {
                return partner;
            } else {
                throw new InvalidLoginCredentialException("Login Credentials are invalid. Please try again.\n");
            }
        } catch (PartnerNotFoundException ex) {
                throw new InvalidLoginCredentialException("Login Credentials are invalid. Please try again.\n");
        }
    }
    
    @Override
    public List<Partner> viewAllPartners() {
        return em.createQuery("SELECT p FROM Partner p", Partner.class).getResultList();
    }

}
