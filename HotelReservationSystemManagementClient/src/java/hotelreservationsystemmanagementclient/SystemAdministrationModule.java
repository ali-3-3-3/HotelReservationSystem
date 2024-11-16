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
            
            System.out.print("> ");

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

        String email;
        String username;
        String password;
        EmployeeRoleEnum role;

        // Email validation loop
        while (true) {
            System.out.print("Email: ");
            email = scanner.nextLine().trim();
            if (email.isEmpty()) {
                System.out.println("Error: Email cannot be empty. Please try again.");
            } else {
                break;
            }
        }

        // Username validation loop
        while (true) {
            System.out.print("Username: ");
            username = scanner.nextLine().trim();
            if (username.isEmpty()) {
                System.out.println("Error: Username cannot be empty. Please try again.");
            } else {
                break;
            }
        }

        // Password validation loop
        while (true) {
            System.out.print("Password: ");
            password = scanner.nextLine().trim();
            if (password.isEmpty()) {
                System.out.println("Error: Password cannot be empty. Please try again.");
            } else {
                break;
            }
        }

        // Role validation loop
        while (true) {
            System.out.print("Role (SYSTEMADMINISTRATOR/OPERATIONMANAGER/SALESMANAGER/GUESTRELATIONOFFICER): ");
            String roleInput = scanner.nextLine().trim().toUpperCase();
            try {
                role = EmployeeRoleEnum.valueOf(roleInput);
                break; // Exit loop if role is valid
            } catch (IllegalArgumentException ex) {
                System.out.println("Error: Invalid role. Please enter one of the following: SYSTEMADMINISTRATOR, OPERATIONMANAGER, SALESMANAGER, GUESTRELATIONOFFICER.");
            }
        }

        // Attempt to create the employee
        try {
            Employee newEmployee = new Employee(username, password, role, email);
            employeeSessionBeanRemote.createNewEmployee(newEmployee);
            System.out.println("Employee created successfully!");
        } catch (EmployeeExistException ex) {
            System.out.println("Error: Employee with this email or username already exists.");
        } catch (InputDataValidationException ex) {
            System.out.println("Error: Invalid data provided. Please check all fields and try again.");
        } catch (UnknownPersistenceException ex) {
            System.out.println("An unknown error occurred while creating the employee. Please contact support.");
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

        String partnerName;
        String partnerContact;
        String partnerPassword;

        // Partner name validation loop
        while (true) {
            System.out.print("Partner System Name: ");
            partnerName = scanner.nextLine().trim();
            if (partnerName.isEmpty()) {
                System.out.println("Error: Partner name cannot be empty. Please enter a valid name.");
            } else {
                break;
            }
        }

        // Partner email/contact validation loop
        while (true) {
            System.out.print("Partner Email: ");
            partnerContact = scanner.nextLine().trim();
            if (partnerContact.isEmpty()) {
                System.out.println("Error: Partner email cannot be empty. Please enter a valid email.");
            } else {
                break;
            }
        }

        // Partner password validation loop
        while (true) {
            System.out.print("Partner Password: ");
            partnerPassword = scanner.nextLine().trim();
            if (partnerPassword.isEmpty()) {
                System.out.println("Error: Password cannot be empty. Please enter a valid password.");
            } else {
                break;
            }
        }

        // Attempt to create the partner
        try {
            Partner newPartner = new Partner(partnerName, partnerContact, partnerPassword);
            partnerSessionBeanRemote.createNewPartner(newPartner);
            System.out.println("Partner created successfully!");
        } catch (PartnerExistException ex) {
            System.out.println("Error: A partner with this name or email already exists. Please use a different name or email.");
        } catch (InputDataValidationException ex) {
            System.out.println("Error: Invalid data provided. Please ensure all fields are correctly filled.");
        } catch (UnknownPersistenceException ex) {
            System.out.println("An unexpected error occurred while creating the partner. Please try again later or contact support.");
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
