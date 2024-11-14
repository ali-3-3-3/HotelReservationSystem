package ejb.session.stateless;

import entity.AllocationException;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exceptions.AllocationExceptionDeleteException;
import util.exceptions.AllocationExceptionExistException;
import util.exceptions.AllocationExceptionNotFoundException;
import util.exceptions.AllocationExceptionUpdateException;
import util.exceptions.InputDataValidationException;
import util.exceptions.UnknownPersistenceException;

@Stateless
public class AllocationExceptionSessionBean implements AllocationExceptionSessionBeanRemote, AllocationExceptionSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public AllocationExceptionSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    @Transactional
    public AllocationException createAllocationException(AllocationException allocationException) throws InputDataValidationException, UnknownPersistenceException, AllocationExceptionExistException {

        Set<ConstraintViolation<AllocationException>> constraintViolations = validator.validate(allocationException);

        try {
            if (constraintViolations.isEmpty()) {
                em.persist(allocationException);
                em.flush();
            } else {
                StringBuilder errorMsg = new StringBuilder("Input data validation error(s):");
                for (ConstraintViolation<AllocationException> violation : constraintViolations) {
                    errorMsg.append("\n- ").append(violation.getPropertyPath()).append(": ").append(violation.getMessage());
                }
                throw new InputDataValidationException(errorMsg.toString());
            }
            return allocationException;
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new AllocationExceptionExistException("The allocation exception already exists with the same details!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public AllocationException retrieveAllocationExceptionById(Long exceptionId) throws AllocationExceptionNotFoundException {
        AllocationException allocationException = em.find(AllocationException.class, exceptionId);
        if (allocationException == null) {
            throw new AllocationExceptionNotFoundException("Allocation Exception with ID " + exceptionId + " not found.");
        }
        return allocationException;
    }
    
    @Override
    @Transactional
    public AllocationException updateAllocationException(Long exceptionId, AllocationException updatedAllocationException) throws AllocationExceptionNotFoundException, AllocationExceptionUpdateException {
        AllocationException allocationException = retrieveAllocationExceptionById(exceptionId);

        try {
            allocationException.setErrorDescription(updatedAllocationException.getErrorDescription());
            allocationException.setResolutionStatus(updatedAllocationException.getResolutionStatus());
            allocationException.setEmployee(updatedAllocationException.getEmployee());
            allocationException.setRoomAllocation(updatedAllocationException.getRoomAllocation());

            em.merge(allocationException);
            return allocationException;
        } catch (PersistenceException ex) {
            throw new AllocationExceptionUpdateException("Failed to update Allocation Exception: " + ex.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void deleteAllocationException(Long exceptionId) throws AllocationExceptionNotFoundException, AllocationExceptionDeleteException {
        AllocationException allocationException = retrieveAllocationExceptionById(exceptionId);

        try {
            em.remove(allocationException);
        } catch (PersistenceException ex) {
            throw new AllocationExceptionDeleteException("Failed to delete Allocation Exception: " + ex.getMessage());
        }
    }
    
    @Override
    public List<AllocationException> retrieveAllocationExceptionsByRoomAllocationId(Long roomAllocationId) {
        Query query = em.createQuery("SELECT a FROM AllocationException a WHERE a.roomAllocation.allocationId = :roomAllocationId");
        query.setParameter("roomAllocationId", roomAllocationId);
        return query.getResultList();
    }
    
    @Override
    public List<AllocationException> retrieveAllocationExceptionsByEmployeeId(Long employeeId) {
        Query query = em.createQuery("SELECT a FROM AllocationException a WHERE a.employee.employeeId = :employeeId");
        query.setParameter("employeeId", employeeId);
        return query.getResultList();
    }
    
    @Override
    public List<AllocationException> viewAllAllocationExceptions() {
        return em.createQuery("SELECT a FROM AllocationException a", AllocationException.class).getResultList();
    }

}
