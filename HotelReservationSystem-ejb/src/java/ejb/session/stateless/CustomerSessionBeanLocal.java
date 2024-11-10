package ejb.session.stateless;

import entity.Customer;
import java.util.List;
import javax.ejb.Local;
import util.exceptions.CustomerExistException;
import util.exceptions.CustomerNotFoundException;
import util.exceptions.InputDataValidationException;
import util.exceptions.InvalidLoginCredentialException;
import util.exceptions.UnknownPersistenceException;

@Local
public interface CustomerSessionBeanLocal {
    public Customer createNewCustomer(Customer customer) throws InputDataValidationException, UnknownPersistenceException, CustomerExistException;
    
    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException;
    
    public Customer doLogin(String username, String password) throws InvalidLoginCredentialException;

    public List<Customer> viewAllCustomers();
}
