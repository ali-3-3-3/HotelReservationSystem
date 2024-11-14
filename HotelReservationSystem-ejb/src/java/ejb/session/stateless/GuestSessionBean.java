package ejb.session.stateless;

import entity.Guest;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exceptions.GuestDeleteException;
import util.exceptions.GuestExistException;
import util.exceptions.GuestNotFoundException;
import util.exceptions.InputDataValidationException;
import util.exceptions.UnknownPersistenceException;

@Stateless
public class GuestSessionBean implements GuestSessionBeanRemote, GuestSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public GuestSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator(); 
    }

    @Override
    @Transactional
    public Guest createNewGuest(Guest newGuest) throws GuestExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Guest>>constraintViolations = validator.validate(newGuest);
        try {
            if (constraintViolations.isEmpty()) {
                em.persist(newGuest);
                em.flush();
                return newGuest;
            } else {
                StringBuilder errorMsg = new StringBuilder("Input data validation error(s):");
                for (ConstraintViolation<Guest> violation : constraintViolations) {
                errorMsg.append("\n- ").append(violation.getPropertyPath()).append(": ").append(violation.getMessage());
                }
                
                throw new InputDataValidationException(errorMsg.toString());
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new GuestExistException("Username already exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public List<Guest> retrieveAllGuests() {
        Query query = em.createQuery("SELECT g FROM Guest g");
        return query.getResultList();
    }

    @Override
    public Guest retrieveGuestByGuestId(Long guestId) throws GuestNotFoundException {
        Guest guest = em.find(Guest.class, guestId);
        if (guest == null) {
            throw new GuestNotFoundException("Guest with ID " + guestId + " does not exist!");
        }
        return guest;
    }
    
    @Override
    public Guest retrieveGuestByEmail(String email) throws GuestNotFoundException {
        try {
            return em.createQuery("SELECT g FROM Guest g WHERE g.email = :email", Guest.class)
                     .setParameter("email", email)
                     .getSingleResult();
        } catch (NoResultException ex) {
            throw new GuestNotFoundException("Guest with email " + email + " does not exist!");
        }
    }
    
    @Transactional
    @Override
    public Guest updateGuest(Long guestId, String name, String email, String phoneNumber) 
            throws GuestNotFoundException, InputDataValidationException {
        Guest guest = em.find(Guest.class, guestId);
        if (guest == null) {
            throw new GuestNotFoundException("Guest with ID " + guestId + " not found");
        }
        guest.setName(name);
        guest.setEmail(email);
        guest.setPhoneNumber(phoneNumber);
        
        Set<ConstraintViolation<Guest>> constraintViolations = validator.validate(guest);
        if (!constraintViolations.isEmpty()) {
            StringBuilder errorMsg = new StringBuilder("Input data validation error(s):");
            for (ConstraintViolation<Guest> violation : constraintViolations) {
                errorMsg.append("\n- ").append(violation.getPropertyPath()).append(": ").append(violation.getMessage());
            }
            throw new InputDataValidationException(errorMsg.toString());
        }

        return em.merge(guest);
    }
    
    @Override
    public void deleteGuest(Long guestId) throws GuestNotFoundException, GuestDeleteException {
        Guest guestToRemove = retrieveGuestByGuestId(guestId);
        try {
            em.remove(guestToRemove);
        } catch (PersistenceException ex) {
            throw new GuestDeleteException("Unable to delete guest with ID " + guestId);
        }
    }

}
