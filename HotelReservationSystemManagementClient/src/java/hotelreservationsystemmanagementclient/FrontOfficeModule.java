package hotelreservationsystemmanagementclient;

import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomAllocationSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import entity.Customer;
import entity.Employee;
import entity.Guest;
import entity.Reservation;
import entity.Room;
import entity.RoomAllocation;
import entity.RoomType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exceptions.GuestExistException;
import util.exceptions.GuestNotFoundException;
import util.exceptions.InputDataValidationException;
import util.exceptions.InvalidRoomCountException;
import util.exceptions.ReservationAddRoomAllocationException;
import util.exceptions.ReservationNotFoundException;
import util.exceptions.ReservationUpdateException;
import util.exceptions.RoomAllocationException;
import util.exceptions.RoomTypeUnavailableException;
import util.exceptions.UnknownPersistenceException;

public class FrontOfficeModule {
    
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    
    private RoomSessionBeanRemote roomSessionBeanRemote;
    
    private GuestSessionBeanRemote guestSessionBeanRemote;
    
    private RoomAllocationSessionBeanRemote roomAllocationSessionBean;
    
    
    private Employee currentEmployee;
    
    private int response = 0;
    
    private final Scanner scanner;

    public FrontOfficeModule() {
        this.scanner = new Scanner(System.in);
    }

    FrontOfficeModule(ReservationSessionBeanRemote reservationSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote, GuestSessionBeanRemote guestSessionBeanRemote, RoomAllocationSessionBeanRemote roomAllocationSessionBean, Employee currentEmployee) {
        this();
        
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.guestSessionBeanRemote = guestSessionBeanRemote;
        this.roomAllocationSessionBean = roomAllocationSessionBean;
        this.currentEmployee = currentEmployee;
    }

    void showMenu() {
        response = 0;

        while (true) {
            System.out.println("*** Hotel Reservation System :: Front Office Module ***\n");
            System.out.println("1: Check-In Guest");
            System.out.println("2: Check-Out Guest");
            System.out.println("3: Walk-in Search Room");
            System.out.println("4: Back\n");
            response = 0;

            OUTER:
            while (response < 1 || response > 12) {
                System.out.print("> ");
                response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1:
                        doCheckInGuest();
                        break;
                    case 2:
                        doCheckOutGuest();
                        break;
                    case 3:
                        searchHotelRooms();
                        break;
                    case 4:
                        break OUTER;
                    default:
                        System.out.println("Invalid option, please try again.\n");
                        break;
                }
            }
            if(response == 4) {
                break;
            }
        }
    }

    private void doCheckInGuest() {
        System.out.println("*** Hotel Reservation System :: Front Office Module :: Check-In Guest ***\n");

        try {
            System.out.print("Enter Reservation ID> ");
            Long reservationId = Long.parseLong(scanner.nextLine());

            Reservation reservation = reservationSessionBeanRemote.retrieveReservationById(reservationId);

            if (reservation.isHasCheckedIn()) {
                System.out.println("Guest is already checked in.\n");
            } else {
                List<RoomAllocation> roomAllocations = reservation.getRoomAllocations();
                printRoomAllocationsDetails(roomAllocations);
                if(roomAllocations != null) {
                    reservation.setHasCheckedIn(true);
                    reservationSessionBeanRemote.updateReservation(reservationId, reservation);
                    reservationSessionBeanRemote.checkInReservation(reservationId);
                    System.out.println("Guest check-in successful!\n");
                }
            }
        } catch (ReservationNotFoundException ex) {
            System.out.println("An error occurred: Reservation with the specified ID was not found.\n");
        } catch (ReservationUpdateException ex) {
            System.out.println("An error occurred while updating the reservation. Please try again.\n");
            Logger.getLogger(FrontOfficeModule.class.getName()).log(Level.SEVERE, "Error updating reservation", ex);
        } catch (InputMismatchException ex) {
            System.out.println("Invalid input: Please enter a valid numeric Reservation ID.\n");
            scanner.nextLine(); // Clear invalid input from scanner
        } catch (RoomTypeUnavailableException ex) {
            Logger.getLogger(FrontOfficeModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void doCheckOutGuest() {
        System.out.println("*** Hotel Reservation System :: Front Office Module :: Check-Out Guest ***\n");

        try {
            System.out.print("Enter Reservation ID> ");
            Long reservationId = Long.parseLong(scanner.nextLine());
            
            Reservation reservation = reservationSessionBeanRemote.retrieveReservationById(reservationId);

            if (reservation.isHasCheckedOut()) {
                System.out.println("Guest is already checked out.\n");
            } else {
                reservation.setHasCheckedOut(true);
                reservationSessionBeanRemote.updateReservation(reservationId, reservation);
                reservationSessionBeanRemote.checkOutReservation(reservationId);
                System.out.println("Guest check-out successful!\n");
            }
        } catch (ReservationNotFoundException ex) {
            System.out.println("An error occurred: Reservation with the specified ID was not found.\n");
        } catch (ReservationUpdateException ex) {
            System.out.println("An error occurred while updating the reservation. Please try again.\n");
            Logger.getLogger(FrontOfficeModule.class.getName()).log(Level.SEVERE, "Error updating reservation", ex);
        } catch (InputMismatchException ex) {
            System.out.println("Invalid input: Please enter a valid numeric Reservation ID.\n");
            scanner.nextLine(); 
        }
    }


    public void searchHotelRooms() {
        Guest currentCustomer = null;
        System.out.print("Enter email address: ");
        String email = scanner.nextLine().trim();

        try {
            // Attempt to retrieve guest by email
            currentCustomer = guestSessionBeanRemote.retrieveGuestByEmail(email);
            System.out.println("Welcome back, " + currentCustomer.getName() + "!");
        } catch (GuestNotFoundException ex) {
            System.out.println("Guest not found. Creating a new account.");

            // Gather guest details for new account creation
            System.out.print("Enter name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter phone number: ");
            String phoneNumber = scanner.nextLine().trim();

            try {
                currentCustomer = new Guest(name, email, phoneNumber);
                currentCustomer = guestSessionBeanRemote.createNewGuest(currentCustomer);
                System.out.println("New account created successfully!");
            } catch (GuestExistException | UnknownPersistenceException | InputDataValidationException ex1) {
                System.out.println("Error creating account: " + ex1.getMessage());
                return; // Exit if the account creation fails
            }
        }

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
                    System.out.println("Total cost: " + reservationSessionBeanRemote.calculateTotalReservationFeeForWalkInPre(checkInDate, checkOutDate, roomType));
                }

                if (currentCustomer != null) {
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
                        makeReservation(currentCustomer, selectedRoomType, checkInDate, checkOutDate);
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
    
    private Reservation makeReservation(Guest currentCustomer, RoomType roomType, Date checkInDate, Date checkOutDate) {
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

            if (reservation != null && checkRoomAvailability(checkInDate, checkOutDate, roomType)) {
                System.out.println("Reservation successfully created!");
                System.out.println("Reservation Details:");
                System.out.println("ReservationID: " + reservation.getReservationId());
                System.out.println("Room Type: " + roomType.getName());
                System.out.println("Check-in Date: " + checkInDate);
                System.out.println("Check-out Date: " + checkOutDate);
                System.out.println("Number of Rooms: " + roomCount);
                System.out.println("Total cost: " + reservationSessionBeanRemote.calculateTotalReservationFeeForWalkIn(reservation.getCheckInDate(), reservation.getCheckOutDate(), reservation.getRoomType(), roomCount));
            } else {
                System.out.println("Reservation creation failed. Please try again.");
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Compare only the date part
            if (sdf.format(checkInDate).equals(sdf.format(new Date()))) {
                System.out.println("ALLOCATING CURRENT DAY WALK-IN RESERVATION...");
                roomAllocationSessionBean.allocateRoomsForWalkInReservation(reservation);
                printRoomAllocationsDetails(reservation.getRoomAllocations());
            }

            return reservation;

        } catch (InputDataValidationException ex) {
            System.out.println("Data validation error: " + ex.getMessage());
        } catch (InvalidRoomCountException ex) {
            System.out.println("Invalid room count: " + ex.getMessage());
        } catch (RoomTypeUnavailableException ex) {
            System.out.println("Room type unavailable: " + ex.getMessage());
        } catch (UnknownPersistenceException ex) {
            System.out.println("Persistence error: " + ex.getMessage());
        } catch (ReservationAddRoomAllocationException | ReservationNotFoundException | ReservationUpdateException | RoomAllocationException ex) {
            System.out.println("An unexpected error occurred: " + ex.getMessage());
            ex.printStackTrace();  
        }

        return null;
    }

    
    public boolean checkRoomAvailability(Date checkInDate, Date checkOutDate, RoomType roomType) {
        // Retrieve room availability from the server
        Map<RoomType, List<Room>> availableRoomsByType = roomAllocationSessionBean.initializeRoomAvailability();

        // Check if the room type exists in the availability map
        if (availableRoomsByType.containsKey(roomType)) {
            List<Room> availableRooms = availableRoomsByType.get(roomType);

            // Count reservations for the specific room type and dates
            int reservationCount = reservationSessionBeanRemote.countReservationsByRoomTypeAndDates(roomType, checkInDate, checkOutDate);

            // Check availability: more available rooms than current reservations
            return reservationCount < availableRooms.size();
        }

        // Room type is not found in the availability map, consider it unavailable
        return false;
    }

    
    
    public void printRoomAllocationsDetails(List<RoomAllocation> roomAllocations) {
        if (roomAllocations == null || roomAllocations.isEmpty()) {
            System.out.println("No room allocations available.");
            return;
        }

        for (RoomAllocation roomAllocation : roomAllocations) {
            System.out.println("Allocation ID: " + roomAllocation.getAllocationId());

            Room room = roomAllocation.getRoom();
            if (room != null) {
                System.out.println("Room: " + room.getRoomNumber());
            } else {
                System.out.println("Room: Not allocated.");
            }

            System.out.println("Is Exception: " + (roomAllocation.isIsException() ? "Yes" : "No"));
            System.out.println("--------------------------------------------");
        }
    }
}
