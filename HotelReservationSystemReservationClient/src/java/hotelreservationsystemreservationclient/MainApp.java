package hotelreservationsystemreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Customer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exceptions.CustomerExistException;
import util.exceptions.InputDataValidationException;
import util.exceptions.InvalidLoginCredentialException;
import util.exceptions.UnknownPersistenceException;

class MainApp {
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    
    private boolean login = false;
    private Customer currentCustomer;
    private int response = 0;
    private Scanner scanner = new Scanner(System.in);
    
    public MainApp() {
    }

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote, RoomRateSessionBeanRemote roomRateSessionBeanRemote, RoomTypeSessionBeanRemote roomTypeSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
    }

    public void runApp() {
        while(true) {
            System.out.println("*** Hotel Reservation System ***");
            if(currentCustomer == null && login == false) {
                System.out.println("1: Login");
                System.out.println("2: Register\n"); 
                System.out.println("3: Search Hotel Room\n"); 
                System.out.println("4: Exit\n");   
            } else {
                System.out.println("1: Logout");
                System.out.println("2: Exit\n");
            }
            
            response = 0;
            
            while (response < 1 || response > 4) {    
                System.out.println("> ");
                response = scanner.nextInt();
                
                if(response == 1 && currentCustomer == null && login == false) {
                    try {
                        doLogin();
                        System.out.println("Login successful!");
                        showCustomerMenu();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } 
                else if(response == 1 && currentCustomer != null && login != false) {
                    doLogout();
                    System.out.println("Logout successful!");
                } 
                else if(response == 2 && currentCustomer == null && login == false) {
                    break;
                } 
                else if(response == 2 && currentCustomer == null && login == false) {
                    try {
                        doRegister();
                    } catch (InputDataValidationException | UnknownPersistenceException | CustomerExistException ex) {
                        Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } 
                else if(response == 3 && currentCustomer == null && login == false) {
                    searchHotelRooms();
                } 
                else {
                    System.out.println("Invalid option, please try again!\n");
                }   
            }
            
            if(response == 2 && currentCustomer != null && login == true | response == 4) {
                break;
            }
        }
    }
    
    private void doLogin() throws InvalidLoginCredentialException {
        Scanner sc = new Scanner(System.in) ;
        System.out.println("*** Hotel Reservation System Reservation Client :: LOGIN ***\n");
        System.out.print("Enter username> ");
        String username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        String password = sc.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0) {
            currentCustomer = customerSessionBeanRemote.doLogin(username, password);
            login = true;

        } else {
            throw new InvalidLoginCredentialException("Missing Login Credentials");
        }
    }
    
    private void doLogout() {
        currentCustomer = null;
        login = false;
        System.out.println("Logout successful!" );
    }

    private void showCustomerMenu() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void doRegister() throws InputDataValidationException, UnknownPersistenceException, CustomerExistException {
        try {
                System.out.println("*** Register as Guest ***\n");

        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Enter Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();
        
        Customer customer = new Customer(name, email, phone, username, password);
        
        customerSessionBeanRemote.createNewCustomer(customer);
        
        } catch (InputDataValidationException | UnknownPersistenceException | CustomerExistException ex) {
            ex.getMessage();
        }
    }

    private void searchHotelRooms() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
