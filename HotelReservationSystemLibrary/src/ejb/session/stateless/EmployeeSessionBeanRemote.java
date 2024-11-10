/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Remote;
import util.exceptions.EmployeeExistException;
import util.exceptions.EmployeeNotFoundException;
import util.exceptions.InputDataValidationException;
import util.exceptions.InvalidLoginCredentialException;
import util.exceptions.UnknownPersistenceException;

/**
 *
 * @author aliya
 */
@Remote
public interface EmployeeSessionBeanRemote {

    public Employee createNewEmployee(Employee employee) throws InputDataValidationException, EmployeeExistException, UnknownPersistenceException;

    public Employee retrieveEmployeeById(Long id) throws EmployeeNotFoundException;

    public Employee retrieveEmployeeByEmail(String email) throws EmployeeNotFoundException;

    public Employee doLogin(String email, String password) throws InvalidLoginCredentialException;

    public List<Employee> viewAllEmployees();
    
}
