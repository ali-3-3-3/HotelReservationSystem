package hotelreservationsystemreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import entity.Customer;
import entity.Reservation;
import entity.RoomAllocation;
import entity.RoomRate;
import entity.RoomType;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import util.exceptions.CustomerExistException;
import util.exceptions.InputDataValidationException;
import util.exceptions.InvalidLoginCredentialException;
import util.exceptions.InvalidRoomCountException;
import util.exceptions.RoomTypeUnavailableException;
import util.exceptions.UnknownPersistenceException;

class MainApp {
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    
    private boolean login = false;
    private Customer currentCustomer;
    private int response = 0;
    private final Scanner scanner;
    
    public MainApp() {
        this.scanner = new Scanner(System.in);
    }

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote) {
        this.scanner = new Scanner(System.in);
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
    }

    public void runApp() {
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
                System.out.println("3: Logout");
                System.out.println("4: Exit");
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
                        viewReservations();
                    }
                    break;

                case 3:
                    if (currentCustomer == null && !login) {
                        searchHotelRooms();
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
            // Input handling for dates (as shown previously)
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
                
                if(currentCustomer != null && login == true) {
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
                    
                }
        } else {
            System.out.println("No rooms available for the selected dates.");
        }

        } catch (Exception ex) {
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


    private void viewReservations() {
        List<Reservation> reservations = customerSessionBeanRemote.getReservationsForCustomer(currentCustomer.getGuestId());

        reservations.forEach(reservation -> {
            System.out.println("Reservation ID: " + reservation.getReservationId());
            System.out.println("Reservation Date: " + formatDate(reservation.getReservationDate()));
            System.out.println("Check-In Date: " + formatDate(reservation.getCheckInDate()));
            System.out.println("Check-Out Date: " + formatDate(reservation.getCheckOutDate()));
            System.out.println("Check-In Time: " + formatTime(reservation.getCheckInTime()));
            System.out.println("Check-Out Time: " + formatTime(reservation.getCheckOutTime()));
            System.out.println("Has Checked In: " + reservation.isHasCheckedIn());
            System.out.println("Has Checked Out: " + reservation.isHasCheckedOut());
            System.out.println("Number of Rooms: " + reservation.getNumOfRooms());
            System.out.println("Total cost: " + reservationSessionBeanRemote.calculateTotalReservationFee(reservation.getCheckInDate(), reservation.getCheckOutDate(), reservation.getRoomType(), reservation));

            RoomType roomType = reservation.getRoomType();
            System.out.println("Room Type: " + (roomType != null ? roomType.getName() : "Not specified"));

            Set<RoomRate> roomRates = reservation.getRoomRates();
            if (roomRates != null && !roomRates.isEmpty()) {
                System.out.println("Room Rates:");
                roomRates.forEach(rate -> {
                    System.out.println(" - " + rate.getRateType() + ": " + rate.getRatePerNight());
                });
            } else {
                System.out.println("Room Rates: Not specified");
            }

            List<RoomAllocation> roomAllocations = reservation.getRoomAllocations();
            if (roomAllocations != null && !roomAllocations.isEmpty()) {
                System.out.println("Room Allocations:");
                roomAllocations.forEach(allocation -> {
                    System.out.println(" - Allocation ID: " + allocation.getAllocationId());
                });
            } else {
                System.out.println("Room Allocations: Not specified");
            }
            System.out.println("-------------------------------------------------");
        });
    }

    // Helper methods for date and time formatting
    private String formatDate(Date date) {
        return date != null ? new SimpleDateFormat("yyyy-MM-dd").format(date) : "Not specified";
    }

    private String formatTime(LocalTime time) {
        return time != null ? time.toString() : "Not specified";
    }
}
