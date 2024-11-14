package hotelreservationsystemmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import entity.Employee;
import entity.Partner;
import util.exceptions.EmployeeNotFoundException;
import util.exceptions.PartnerNotFoundException;

import java.util.List;
import java.util.Scanner;
import util.enumerations.EmployeeRoleEnum;
import util.exceptions.EmployeeExistException;
import util.exceptions.InputDataValidationException;
import util.exceptions.PartnerExistException;
import util.exceptions.UnknownPersistenceException;

public class SystemAdministrationModule {
    private final EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private final PartnerSessionBeanRemote partnerSessionBeanRemote;
    private final Scanner scanner;

    public SystemAdministrationModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu(Employee currentEmployee) {
        int response = 0;

        while (response != 5) {
            System.out.println("*** System Administration Menu ***");
            System.out.println("1: Create New Employee");
            System.out.println("2: View All Employees");
            System.out.println("3: Create New Partner");
            System.out.println("4: View All Partners");
            System.out.println("5: Exit");

            try {
                response = Integer.parseInt(scanner.nextLine());

                switch (response) {
                    case 1:
                        createNewEmployee();
                        break;
                    case 2:
                        viewAllEmployees();
                        break;
                    case 3:
                        createNewPartner();
                        break;
                    case 4:
                        viewAllPartners();
                        break;
                    case 5:
                        System.out.println("Exiting the System Administration Menu.");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
            }
        }
    }

    private void createNewEmployee() {
        System.out.println("Enter employee details:");

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        System.out.print("Role (SYSTEMADMINISTRATOR/OPERATIONMANAGER/SALESMANAGER/GUESTRELATIONOFFICER): ");
        String role = scanner.nextLine().trim();

        try {
            Employee newEmployee = new Employee(username, password, EmployeeRoleEnum.valueOf(role), email);
            employeeSessionBeanRemote.createNewEmployee(newEmployee);
            System.out.println("Employee created successfully!");
        } catch (EmployeeExistException | InputDataValidationException | UnknownPersistenceException ex) {
            System.out.println("Error occurred while creating employee: " + ex.getMessage());
        }
    }

    private void viewAllEmployees() {
        try {
            List<Employee> employees = employeeSessionBeanRemote.retrieveAllEmployees();
            System.out.println("List of all employees:");
            for (Employee employee : employees) {
                System.out.println("Employee ID: " + employee.getEmployeeId() + ", Name: " + employee.getUsername() + ", Role: " + employee.getUserRole());
            }
        } catch (EmployeeNotFoundException ex) {
            System.out.println("No employees found.");
        }
    }

    private void createNewPartner() {
        System.out.println("Enter partner details:");

        System.out.print("Partner System Name: ");
        String partnerName = scanner.nextLine().trim();

        System.out.print("Partner Email: ");
        String partnerContact = scanner.nextLine().trim();
        
        System.out.print("Partner Password: ");
        String partnerPassword = scanner.nextLine().trim();

        try {
            Partner newPartner = new Partner(partnerName, partnerContact, partnerPassword);
            partnerSessionBeanRemote.createNewPartner(newPartner);
            System.out.println("Partner created successfully!");
        } catch (InputDataValidationException | PartnerExistException | UnknownPersistenceException ex) {
            System.out.println("Error occurred while creating partner: " + ex.getMessage());
        }
    }

    private void viewAllPartners() {
        try {
            List<Partner> partners = partnerSessionBeanRemote.retrieveAllPartners();
            System.out.println("List of all partners:");
            partners.forEach(partner -> {
                System.out.println("Partner ID: " + partner.getPartnerId() + ", Name: " + partner.getSystemName() + ", Contact: " + partner.getEmail());
            });
        } catch (PartnerNotFoundException ex) {
            System.out.println("No partners found.");
        }
    }
}
