package ejb.session.stateless;

import entity.Employee;
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
import util.exceptions.EmployeeExistException;
import util.exceptions.EmployeeNotFoundException;
import util.exceptions.InputDataValidationException;
import util.exceptions.InvalidLoginCredentialException;
import util.exceptions.UnknownPersistenceException;

@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {
    
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public EmployeeSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    @Transactional
    public Employee createNewEmployee(Employee employee) throws InputDataValidationException, EmployeeExistException, UnknownPersistenceException {
        Set<ConstraintViolation<Employee>>constraintViolations = validator.validate(employee);
        try {
            if(constraintViolations.isEmpty()) {
            em.persist(employee);
            em.flush();
            return employee;
            } else {
                StringBuilder errorMsg = new StringBuilder("Input data validation error(s):");
                for (ConstraintViolation<Employee> violation : constraintViolations) {
                errorMsg.append("\n- ").append(violation.getPropertyPath()).append(": ").append(violation.getMessage());
                }
                
                throw new InputDataValidationException(errorMsg.toString());
            }
        } catch  (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new EmployeeExistException("An employee with the same email exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public Employee retrieveEmployeeById(Long id) throws EmployeeNotFoundException {
        Employee employee = em.find(Employee.class, id);
        
        if(employee != null) {
            return employee;
        } else {
            throw new EmployeeNotFoundException("Employee id " + id.toString() + " does not exist!");
        }
    }
    
    @Override
    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException{
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :username");
        query.setParameter("username", username);
        
        try{
            return (Employee)query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new EmployeeNotFoundException("Employee does not exist!");
        }
    }
    
    @Override
    public Employee doLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            Employee employee = retrieveEmployeeByUsername(username);
            if(employee.getPassword().equals(password)) {
                return employee;
            } else {
                throw new InvalidLoginCredentialException("Wrong password input!");
            }
        } catch (EmployeeNotFoundException ex) {
            throw new InvalidLoginCredentialException("Email does not exist!");
        }
        
    }
    
    @Override
    public List<Employee> viewAllEmployees() {
        return em.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
    }
}
