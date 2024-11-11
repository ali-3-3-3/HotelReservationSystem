package ejb.session.stateless;

import entity.Customer;
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
import util.exceptions.CustomerExistException;
import util.exceptions.CustomerNotFoundException;
import util.exceptions.InputDataValidationException;
import util.exceptions.InvalidLoginCredentialException;
import util.exceptions.UnknownPersistenceException;

@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CustomerSessionBean() {
       validatorFactory = Validation.buildDefaultValidatorFactory();
       validator = validatorFactory.getValidator(); 
    }

    @Override
    @Transactional
    public Customer createNewCustomer(Customer customer) throws InputDataValidationException, UnknownPersistenceException, CustomerExistException {
        Set<ConstraintViolation<Customer>>constraintViolations = validator.validate(customer);
        try {
            if (constraintViolations.isEmpty()) {
                em.persist(customer);
                em.flush();
            } else {
                StringBuilder errorMsg = new StringBuilder("Input data validation error(s):");
                for (ConstraintViolation<Customer> violation : constraintViolations) {
                errorMsg.append("\n- ").append(violation.getPropertyPath()).append(": ").append(violation.getMessage());
                
                throw new InputDataValidationException(errorMsg.toString());
                }
            }
            return customer;
        } catch (PersistenceException ex) {
              if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new CustomerExistException("ID Number/Contact Number/Username already exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException{
        Query query = em.createQuery("SELECT e FROM Customer e WHERE e.username = :user");
        query.setParameter("user", username);
        
        try{
            return (Customer)query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer does not exist!");
        }
    }
    
    @Override
    public Customer doLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            Customer customer = retrieveCustomerByUsername(username);
            if(customer.getPassword().equals(password)) {
                return customer;
            } else {
                throw new InvalidLoginCredentialException("Wrong password input!");
            }
        } catch (CustomerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist!");
        }
        
    }
    
    @Override
    public List<Customer> viewAllCustomers() {
        return em.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
    }
}
