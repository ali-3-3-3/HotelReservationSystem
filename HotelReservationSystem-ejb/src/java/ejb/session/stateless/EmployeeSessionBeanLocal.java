package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Local;
import util.exceptions.EmployeeExistException;
import util.exceptions.EmployeeNotFoundException;
import util.exceptions.InputDataValidationException;
import util.exceptions.InvalidLoginCredentialException;
import util.exceptions.UnknownPersistenceException;

@Local
public interface EmployeeSessionBeanLocal {
    public Employee createNewEmployee(Employee employee) throws InputDataValidationException, EmployeeExistException, UnknownPersistenceException;
    
    public Employee retrieveEmployeeById(Long id) throws EmployeeNotFoundException;
    
    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;
    
    public Employee doLogin(String email, String password) throws InvalidLoginCredentialException;
    
    public List<Employee> viewAllEmployees();
    
    public List<Employee> retrieveAllEmployees() throws EmployeeNotFoundException;
}   

