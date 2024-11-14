package ejb.session.stateless;

import entity.AllocationException;
import java.util.List;
import javax.ejb.Local;
import util.exceptions.AllocationExceptionDeleteException;
import util.exceptions.AllocationExceptionExistException;
import util.exceptions.AllocationExceptionNotFoundException;
import util.exceptions.AllocationExceptionUpdateException;
import util.exceptions.InputDataValidationException;
import util.exceptions.UnknownPersistenceException;

@Local
public interface AllocationExceptionSessionBeanLocal {

    public AllocationException createAllocationException(AllocationException allocationException) throws InputDataValidationException, UnknownPersistenceException, AllocationExceptionExistException;

    public AllocationException retrieveAllocationExceptionById(Long exceptionId) throws AllocationExceptionNotFoundException;

    public AllocationException updateAllocationException(Long exceptionId, AllocationException updatedAllocationException) throws AllocationExceptionNotFoundException, AllocationExceptionUpdateException;

    public void deleteAllocationException(Long exceptionId) throws AllocationExceptionNotFoundException, AllocationExceptionDeleteException;

    public List<AllocationException> retrieveAllocationExceptionsByRoomAllocationId(Long roomAllocationId);

    public List<AllocationException> retrieveAllocationExceptionsByEmployeeId(Long employeeId);
    
    public List<AllocationException> viewAllAllocationExceptions();
}
