/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemmanagementclient;

import ejb.session.stateless.AllocationExceptionSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumerations.EmployeeRoleEnum;
import util.exceptions.InvalidLoginCredentialException;

/**
 *
 * @author aliya
 */
class MainApp {
    
    private AllocationExceptionSessionBeanRemote allocationExceptionSessionBeanRemote;
    private GuestSessionBeanRemote guestSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    
    private boolean login = false;
    private Employee currentEmployee = null;
    private int response = 0;
    private Scanner scanner = new Scanner(System.in);
    
    public MainApp() {
    }

    public MainApp(AllocationExceptionSessionBeanRemote allocationExceptionSessionBeanRemote, GuestSessionBeanRemote guestSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote, RoomRateSessionBeanRemote roomRateSessionBeanRemote, RoomTypeSessionBeanRemote roomTypeSessionBeanRemote) {
        this.allocationExceptionSessionBeanRemote = allocationExceptionSessionBeanRemote;
        this.guestSessionBeanRemote = guestSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
    }

    public void runApp() {
        while(true) {
            System.out.println("*** Hotel Management System ***");
            if(currentEmployee == null && login == false) {
                System.out.println("1: Login");
                System.out.println("2: Exit\n");   
            } else {
                System.out.println("1: Logout");
                System.out.println("2: Exit\n");
            }
            
            response = 0;
            
            while (response < 1 || response > 2) {    
                System.out.println("> ");
                response = scanner.nextInt();
                
                if(response == 1 && currentEmployee == null && login == false) {
                    try {
                        doLogin();
                        System.out.println("Login successful!");
                        showMenu();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } 
                else if(response == 1 && currentEmployee != null && login != false) {
                    doLogout();
                    System.out.println("Logout successful!");
                } 
                else if(response == 2) {
                    break;
                } 
                else {
                    System.out.println("Invalid option, please try again!\n");
                }   
            }
            
            if(response == 2) {
                break;
            }
        }
    }
    
    private void doLogin() throws InvalidLoginCredentialException {
        Scanner sc = new Scanner(System.in) ;
        System.out.println("*** Hotel Reservation System Management Client :: LOGIN ***\n");
        System.out.print("Enter username> ");
        String username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        String password = sc.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0) {
            currentEmployee = employeeSessionBeanRemote.doLogin(username, password);
            login = true;
        } else {
            throw new InvalidLoginCredentialException("Missing Login Credentials");
        }
    }
    
    public void doLogout() {
        currentEmployee = null;
        login = false;
        System.out.println("Logout successful!" );
    }
    
    public void showMenu() {
        EmployeeRoleEnum currentRole = currentEmployee.getUserRole();

        switch (currentRole) {
            case SYSTEMADMINISTRATOR:
                SystemAdministrationModule systemAdministrationModule = new SystemAdministrationModule();
                systemAdministrationModule.showMenu(currentEmployee);
                break;
            case OPERATIONMANAGER:
                HotelOperationModule hotelOperationModule1 = new HotelOperationModule();
                hotelOperationModule1.showOperationManagerMenu(currentEmployee);
                break;
            case SALESMANAGER:
                HotelOperationModule hotelOperationModule2 = new HotelOperationModule();
                hotelOperationModule2.showSalesManagerMenu(currentEmployee);
                break;
            case GUESTRELATIONOFFICER:
                FrontOfficeModule frontOfficeModule = new FrontOfficeModule();
                frontOfficeModule.showMenu(currentEmployee);
                break;
            default:
                System.out.println("Invalid role.");
                doLogout();
        }
    }
    
}
