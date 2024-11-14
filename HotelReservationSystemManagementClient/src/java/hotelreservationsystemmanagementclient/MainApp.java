package hotelreservationsystemmanagementclient;

import ejb.session.stateless.AllocationExceptionSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomAllocationSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumerations.EmployeeRoleEnum;
import util.exceptions.InvalidLoginCredentialException;

class MainApp {
    
    private AllocationExceptionSessionBeanRemote allocationExceptionSessionBeanRemote;
    private GuestSessionBeanRemote guestSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private RoomAllocationSessionBeanRemote roomAllocationSessionBeanRemote;
    
    private Employee currentEmployee = null;
    private final Scanner scanner;

    public MainApp(AllocationExceptionSessionBeanRemote allocationExceptionSessionBeanRemote, 
                   GuestSessionBeanRemote guestSessionBeanRemote, 
                   EmployeeSessionBeanRemote employeeSessionBeanRemote, 
                   PartnerSessionBeanRemote partnerSessionBeanRemote,
                   ReservationSessionBeanRemote reservationSessionBeanRemote, 
                   RoomSessionBeanRemote roomSessionBeanRemote, 
                   RoomRateSessionBeanRemote roomRateSessionBeanRemote, 
                   RoomTypeSessionBeanRemote roomTypeSessionBeanRemote,
                   RoomAllocationSessionBeanRemote roomAllocationSessionBeanRemote) {
        this.allocationExceptionSessionBeanRemote = allocationExceptionSessionBeanRemote;
        this.guestSessionBeanRemote = guestSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.roomAllocationSessionBeanRemote = roomAllocationSessionBeanRemote;
        this.scanner = new Scanner(System.in); // Initialize scanner once
    }

    public void runApp() {
        OUTER:
        while (true) {
            try {
                System.out.println("*** Hotel Management System ***");
                if (currentEmployee == null) {
                    System.out.println("1: Login");
                } else {
                    System.out.println("1: Logout");
                }
                System.out.println("2: Exit\n");
                int response = getUserInput();
                switch (response) {
                    case 1:
                        if (currentEmployee == null) {
                            handleLogin();
                        } else {
                            handleLogout();
                        }   break;
                    case 2:
                        break OUTER;
                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;
                }
            }catch (Exception ex) {
                System.out.println("An unexpected error occurred: " + ex.getMessage());
            }
        }
        scanner.close();
    }

    private void handleLogin() {
        try {
            System.out.println("*** Hotel Reservation System Management Client :: LOGIN ***\n");
            System.out.print("Enter username> ");
            String username = scanner.nextLine().trim();
            System.out.print("Enter password> ");
            String password = scanner.nextLine().trim();

            if (username.isEmpty() || password.isEmpty()) {
                System.out.println("Missing login credentials.");
                return;
            }

            currentEmployee = employeeSessionBeanRemote.doLogin(username, password);
            System.out.println("Login successful!");
            showMenu();
        } catch (InvalidLoginCredentialException ex) {
            System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
        }
    }

    private void handleLogout() {
        currentEmployee = null;
        System.out.println("Logout successful!");
    }

    private void showMenu() {
        if (currentEmployee == null) return;

        EmployeeRoleEnum currentRole = currentEmployee.getUserRole();

        switch (currentRole) {
            case SYSTEMADMINISTRATOR:
                new SystemAdministrationModule(employeeSessionBeanRemote, partnerSessionBeanRemote).showMenu(currentEmployee);
                break;
            case OPERATIONMANAGER:
                new HotelOperationModule().showOperationManagerMenu(currentEmployee);
                break;
            case SALESMANAGER:
                new HotelOperationModule().showSalesManagerMenu(currentEmployee);
                break;
            case GUESTRELATIONOFFICER:
                new FrontOfficeModule(reservationSessionBeanRemote, roomSessionBeanRemote, guestSessionBeanRemote, roomAllocationSessionBeanRemote, currentEmployee).showMenu();
                break;
            default:
                System.out.println("Invalid role.");
                handleLogout();
        }
    }

    private int getUserInput() {
        int choice = 0;
        while (true) {
            System.out.print("> ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= 2) {
                    return choice;
                } else {
                    System.out.println("Please enter a valid option (1 or 2).");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}