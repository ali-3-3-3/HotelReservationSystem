package ejb.session.stateless;

import entity.Customer;
import java.util.List;
import javax.ejb.Remote;
import util.exceptions.CustomerExistException;
import util.exceptions.CustomerNotFoundException;
import util.exceptions.InputDataValidationException;
import util.exceptions.InvalidLoginCredentialException;
import util.exceptions.UnknownPersistenceException;

@Remote
public interface CustomerSessionBeanRemote {

    public Customer createNewCustomer(Customer customer) throws InputDataValidationException, UnknownPersistenceException, CustomerExistException;

    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException;

    public Customer doLogin(String username, String password) throws InvalidLoginCredentialException;

    public List<Customer> viewAllCustomers();
    
}
