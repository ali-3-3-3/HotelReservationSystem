package hotelreservationsystemreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Customer;
import entity.Reservation;
import entity.Room;
import entity.RoomType;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolationException;
import static jdk.nashorn.internal.runtime.Debug.id;
import util.enumerations.RoomStatusEnum;
import util.exceptions.CustomerExistException;
import util.exceptions.InputDataValidationException;
import util.exceptions.InvalidLoginCredentialException;
import util.exceptions.InvalidRoomCountException;
import util.exceptions.ReservationNotFoundException;
import util.exceptions.RoomExistException;
import util.exceptions.RoomTypeNotFoundException;
import util.exceptions.RoomTypeUnavailableException;
import util.exceptions.UnknownPersistenceException;

class MainApp {
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    
    private boolean login = false;
    private Customer currentCustomer;
    private int response = 0;
    private final Scanner scanner;
    
    public MainApp() {
        this.scanner = new Scanner(System.in);
    }

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote, RoomTypeSessionBeanRemote roomTypeSessionBeanRemote) {
        this.scanner = new Scanner(System.in);
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
    }

    public void runApp() throws InputDataValidationException, RoomExistException, UnknownPersistenceException, RoomTypeNotFoundException {

        while (true) {
            System.out.println("*** Hotel Reservation System ***");

            if (currentCustomer == null && !login) {
                System.out.println("1: Login");
                System.out.println("2: Register");
                System.out.println("3: Search Hotel Room!");
                System.out.println("4: Exit");
            } else {
                System.out.println("1: Reserve Hotel Room!");
                System.out.println("2: View all my reservations");
                System.out.println("3: View reservation details");
                System.out.println("4: Logout");
            }

            System.out.print("> ");
            response = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (response) {
                case 1:
                    if (currentCustomer == null && !login) {
                        try {
                            doLogin();
                            System.out.println("Login successful!");
                        } catch (InvalidLoginCredentialException ex) {
                            System.out.println("Invalid login credential: " + ex.getMessage());
                        }
                    } else if (currentCustomer != null && login) {
                        searchHotelRooms();
                    }
                    break;

                case 2:
                    if (currentCustomer == null && !login) {
                        try {
                            doRegister();
                        } catch (InputDataValidationException | UnknownPersistenceException | CustomerExistException ex) {
                            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (currentCustomer != null && login) {
                        viewAllReservations();
                    }
                    break;

                case 3:
                    if (currentCustomer == null && !login) {
                        searchHotelRooms();
                    } else if (currentCustomer != null && login) {
                        viewReservationDetails();
                    }
                    break;

                case 4:
                    return; // Exit the application

                default:
                    System.out.println("Invalid option, please try again.");
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
    
    public void doLogout() {
        currentCustomer = null;
        login = false;
        System.out.println("Logout successful!" );
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
            System.out.println("An error occurred during registration: " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    public void searchHotelRooms() {
        Date checkInDate = null;
        Date checkOutDate = null;

        try {
            // Input handling for dates
            while (checkInDate == null) {
                checkInDate = getDateInput("check-in");
                if (checkInDate == null) {
                    System.out.println("Invalid check-in date. Please try again.");
                }
            }

            while (checkOutDate == null) {
                checkOutDate = getDateInput("check-out");
                if (checkOutDate == null) {
                    System.out.println("Invalid check-out date. Please try again.");
                } else if (!checkOutDate.after(checkInDate)) {
                    System.out.println("Check-out date must be after the check-in date. Please try again.");
                    checkOutDate = null;
                }
            }

            List<RoomType> availableRoomTypes = roomSessionBeanRemote.searchAvailableRoomTypes(checkInDate, checkOutDate);

            if (availableRoomTypes != null && !availableRoomTypes.isEmpty()) {
                System.out.println("Available Room Types:");
                for (int i = 0; i < availableRoomTypes.size(); i++) {
                    RoomType roomType = availableRoomTypes.get(i);
                    System.out.println((i + 1) + ": " + roomType.getName());
                }

                if (currentCustomer != null && login == true) {
                    System.out.print("Would you like to make a reservation? (1: Yes, 2: No): ");
                    int choice = Integer.parseInt(scanner.nextLine().trim());

                    if (choice == 1) {
                        int selection = 0;
                        while (selection < 1 || selection > availableRoomTypes.size()) {
                            System.out.print("Select a Room Type by number (1 - " + availableRoomTypes.size() + "): ");
                            try {
                                selection = Integer.parseInt(scanner.nextLine());
                            } catch (NumberFormatException ex) {
                                System.out.println("Invalid input. Please enter a number.");
                            }
                        }

                        RoomType selectedRoomType = availableRoomTypes.get(selection - 1);
                        makeReservation(selectedRoomType, checkInDate, checkOutDate);
                    } else {
                        System.out.println("Reservation process canceled.");
                    }
                }
            } else {
                System.out.println("No rooms available for the selected dates.");
            }

        } catch (NumberFormatException ex) {
            System.err.println("An error occurred while searching for room types: " + ex.getMessage());
        }
    }
    
    private Date getDateInput(String dateType) {
        int year = 0, month = 0, day = 0;

        try {
            System.out.print("Enter " + dateType + " year (e.g., 2024): ");
            year = Integer.parseInt(scanner.nextLine());
            if (year < 1900 || year > 2100) {
                throw new IllegalArgumentException("Please enter a year between 1900 and 2100.");
            }

            System.out.print("Enter " + dateType + " month (e.g., 9 for September): ");
            month = Integer.parseInt(scanner.nextLine()) - 1;
            if (month < 0 || month > 11) {
                throw new IllegalArgumentException("Month must be between 1 and 12.");
            }

            System.out.print("Enter " + dateType + " day (e.g., 7): ");
            day = Integer.parseInt(scanner.nextLine());
            if (day < 1 || day > 31) {
                throw new IllegalArgumentException("Day must be between 1 and 31.");
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setLenient(false); 
            calendar.set(year, month, day, 0, 0, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            return calendar.getTime();
        } catch (NumberFormatException ex) {
            System.err.println("Invalid input: Please enter numeric values for year, month, and day.");
        } catch (IllegalArgumentException ex) {
            System.err.println("Input error: " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("An unexpected error occurred: " + ex.getMessage());
        }

        return null;
    }
    
    private Reservation makeReservation(RoomType roomType, Date checkInDate, Date checkOutDate) {
        System.out.println("Enter number of rooms to reserve (1-9): ");
        int roomCount;

        try {
            roomCount = Integer.parseInt(scanner.nextLine());
            if (roomCount < 1 || roomCount > 9) {
                System.out.println("Invalid room count. Please enter a number between 1 and 9.");
                return null;
            }
        } catch (NumberFormatException ex) {
            System.out.println("Invalid input. Please enter a valid number.");
            return null;
        }

        // Verify RoomType and currentCustomer are initialized
        if (roomType == null) {
            System.out.println("Error: Room type not selected. Please select a valid room type.");
            return null;
        }

        if (currentCustomer == null || currentCustomer.getGuestId() == null) {
            System.out.println("Error: No customer logged in. Please log in to make a reservation.");
            return null;
        }

        try {
            Long customerId = currentCustomer.getGuestId();

            Reservation reservation = reservationSessionBeanRemote.createReservationFromSearch(
                    customerId, roomType, checkInDate, checkOutDate, roomCount);

            if (reservation != null) {
                System.out.println("Reservation successfully created!");
                System.out.println("Reservation Details:");
                System.out.println("Room Type: " + roomType.getName());
                System.out.println("Check-in Date: " + checkInDate);
                System.out.println("Check-out Date: " + checkOutDate);
                System.out.println("Number of Rooms: " + roomCount);
            } else {
                System.out.println("Reservation creation failed. Please try again.");
            }

            return reservation;
            } catch (ConstraintViolationException ex) {
                System.out.println("Bean validation error: " + ex.getMessage());
                ex.getConstraintViolations().stream().map(violation -> {
                    System.out.println("Property: " + violation.getPropertyPath());
                return violation;
            }).map(violation -> {
                System.out.println("Invalid value: " + violation.getInvalidValue());
                return violation;
            }).forEachOrdered(violation -> {
                System.out.println("Error: " + violation.getMessage());
            });
        } catch (InputDataValidationException ex) {
            System.out.println("Data validation error: " + ex.getMessage());
        } catch (InvalidRoomCountException ex) {
            System.out.println("Invalid room count: " + ex.getMessage());
        } catch (RoomTypeUnavailableException ex) {
            System.out.println("Room type unavailable: " + ex.getMessage());
        } catch (UnknownPersistenceException ex) {
            System.out.println("Persistence error: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("An unexpected error occurred: " + ex.getMessage());
            ex.printStackTrace();  
        }

        return null;
    }


    public void viewAllReservations() {
        try {
            List<Reservation> reservations = reservationSessionBeanRemote.retrieveReservationsByGuestId(currentCustomer.getGuestId());

            if (reservations.isEmpty()) {
                System.out.println("No reservations found.");
            } else {
                System.out.println("Reservations (Basic Info):");
                for (Reservation reservation : reservations) {
                    System.out.println("Reservation ID: " + reservation.getReservationId() + 
                                       ", Check-In: " + reservation.getCheckInDate() + 
                                       ", Room Type: " + reservation.getRoomType().getName() +
                                       ", Room Rates: " + reservation.getRoomRates() +
                                       ", Total Cost: " + reservationSessionBeanRemote.calculateTotalReservationFee(reservation.getCheckInDate(), reservation.getCheckOutDate(), reservation.getRoomType(), reservation));
                }
            }
        } catch (Exception ex) {
            System.out.println("An error occurred while retrieving reservations: " + ex.getMessage());
        }
    }
    
    
    public void viewReservationDetails() {
        System.out.print("Enter Reservation ID to view details: ");
        Long reservationId = Long.parseLong(scanner.nextLine().trim());

        try {
            Reservation reservation = reservationSessionBeanRemote.retrieveReservationById(reservationId);

            System.out.println("Reservation Details:");
            System.out.println("Reservation ID: " + reservation.getReservationId());
            System.out.println("Check-In Date: " + reservation.getCheckInDate());
            System.out.println("Check-Out Date: " + reservation.getCheckOutDate());
            System.out.println("Room Type: " + reservation.getRoomType().getName());
            System.out.println("Number of Rooms: " + reservation.getNumOfRooms());
            System.out.println("Reservation Date: " + reservation.getReservationDate());
            System.out.println("Checked-In: " + (reservation.isHasCheckedIn() ? "Yes" : "No"));
            System.out.println("Checked-Out: " + (reservation.isHasCheckedOut() ? "Yes" : "No"));
            System.out.println(", Room Rates: " + reservation.getRoomRates());
            System.out.println("Total Cost: " + reservationSessionBeanRemote.calculateTotalReservationFee(reservation.getCheckInDate(), reservation.getCheckOutDate(), reservation.getRoomType(), reservation));

        } catch (ReservationNotFoundException ex) {
            System.out.println("Reservation not found: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("An error occurred while retrieving reservation details: " + ex.getMessage());
        }
    }

}
