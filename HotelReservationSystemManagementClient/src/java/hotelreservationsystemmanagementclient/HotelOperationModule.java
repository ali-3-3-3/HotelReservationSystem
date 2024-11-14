package hotelreservationsystemmanagementclient;

import ejb.session.stateless.AllocationExceptionSessionBeanRemote;
import ejb.session.stateless.RoomAllocationSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.AllocationException;
import entity.RoomType;
import entity.Employee;
import entity.Room;
import entity.RoomRate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import util.exceptions.InputDataValidationException;
import util.exceptions.RoomTypeExistException;
import util.exceptions.RoomTypeNotFoundException;
import util.exceptions.RoomTypeDeleteException;
import util.exceptions.RoomTypeUpdateException;
import util.exceptions.UnknownPersistenceException;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumerations.RateTypeEnum;
import util.enumerations.RoomStatusEnum;
import util.exceptions.ReservationAddRoomAllocationException;
import util.exceptions.ReservationUpdateException;
import util.exceptions.RoomAllocationNotFoundException;
import util.exceptions.RoomAllocationUpdateException;
import util.exceptions.RoomDeleteException;
import util.exceptions.RoomExistException;
import util.exceptions.RoomNotFoundException;
import util.exceptions.RoomRateDeleteException;
import util.exceptions.RoomRateExistException;
import util.exceptions.RoomRateNotFoundException;
import util.exceptions.RoomRateUpdateException;
import util.exceptions.RoomUpdateException;

public class HotelOperationModule {
    private final RoomAllocationSessionBeanRemote roomAllocationSessionBeanRemote;
    private final RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private final RoomSessionBeanRemote roomSessionBeanRemote;
    private final AllocationExceptionSessionBeanRemote allocationExceptionSessionBeanRemote;
    private final RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private final Scanner scanner;
    
    public HotelOperationModule(RoomAllocationSessionBeanRemote roomAllocationSessionBeanRemote,
            RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, 
            RoomSessionBeanRemote roomSessionBeanRemote,
            AllocationExceptionSessionBeanRemote allocationExceptionSessionBeanRemote,
            RoomRateSessionBeanRemote roomRateSessionBeanRemote){
        this.roomAllocationSessionBeanRemote = roomAllocationSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.allocationExceptionSessionBeanRemote = allocationExceptionSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.scanner = new Scanner(System.in);
    }

    public void showOperationManagerMenu(Employee currentEmployee) throws InputDataValidationException {
        int option;
        do {
            System.out.println("\n======Welcome Operation Manager======");
            System.out.println("\n=== Room Type Management ===");
            System.out.println("1. Create New Room Type");
            System.out.println("2. View Room Type Details");
            System.out.println("3. Update Room Type");
            System.out.println("4. Delete Room Type");
            System.out.println("5. View All Room Types");
            System.out.println("\n=== Room Management ===");
            System.out.println("6. Create New Room");
            System.out.println("7. Update Room");
            System.out.println("8. Delete Room");
            System.out.println("9. View All Rooms");
            System.out.println("10. View Room Allocation Exception Reports");
            System.out.println("11. Run roon allocation for current day reservations");
            System.out.println("12. Exit");
            System.out.print("Enter option: ");
            option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    createNewRoomType();
                    break;
                case 2:
                    viewRoomTypeDetails();
                    break;
                case 3:
                    updateRoomType();
                    break;
                case 4:
                    deleteRoomType();
                    break;
                case 5:
                    viewAllRoomTypes();
                    break;
                case 6:
                    createRoom();
                    break;
                case 7:
                    updateRoom();
                    break;
                case 8:
                    deleteRoom();
                    break;
                case 9:
                    viewAllRooms();
                    break;
                case 10:
                    viewAllocationExceptionReports();
                    break;
                case 11:
                    runRoomAllocation();
                    break;
                case 12:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 12);
    }

    public void showSalesManagerMenu(Employee currentEmployee) {
        int option;
        do {
            System.out.println("\n======Welcome Operation Manager======");
            System.out.println("\n=== Room Type Management ===");
            System.out.println("1. Create New Room Rate");
            System.out.println("2. View Room Rate Details");
            System.out.println("3. Update Room Rate");
            System.out.println("4. Delete Room Rate");
            System.out.println("5. View All Room Rates");
            System.out.println("6. Exit");
            System.out.print("Enter option: ");
            option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    createNewRoomRate();
                    break;
                case 2:
                    viewRoomRateDetails();
                    break;
                case 3:
                    updateRoomRate();
                    break;
                case 4:
                    deleteRoomRate();
                    break;
                case 5:
                    viewAllRoomRates();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 12);
    }
    
    public void createNewRoomType() {
        System.out.print("Enter Room Type Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Max Occupancy: ");
        int maxOccupancy = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter Description: ");
        String description = scanner.nextLine().trim();

        RoomType newRoomType = new RoomType(name, maxOccupancy, description);

        try {
            RoomType createdRoomType = roomTypeSessionBeanRemote.createNewRoomType(newRoomType);
            System.out.println("Room Type created successfully: " + createdRoomType.getName());
        } catch (InputDataValidationException | RoomTypeExistException | UnknownPersistenceException e) {
            System.out.println("Error creating Room Type: " + e.getMessage());
        }
    }

    public void viewRoomTypeDetails() {
        System.out.print("Enter Room Type ID: ");
        Long roomTypeId = Long.parseLong(scanner.nextLine().trim());

        try {
            RoomType roomType = roomTypeSessionBeanRemote.retrieveRoomTypeById(roomTypeId);
            System.out.println("Room Type Details:");
            System.out.println("Name: " + roomType.getName());
            System.out.println("Max Occupancy: " + roomType.getMaxOccupancy());
            System.out.println("Description: " + roomType.getDescription());
            System.out.println("Next Higher Room Type: " + (roomType.getNextHigherRoomType() != null ? roomType.getNextHigherRoomType().getName() : "None"));
        } catch (RoomTypeNotFoundException e) {
            System.out.println("Error fetching Room Type details: " + e.getMessage());
        }
    }

    public void updateRoomType() {
        System.out.print("Enter Room Type ID to update: ");
        Long roomTypeId = Long.parseLong(scanner.nextLine().trim());

        System.out.print("Enter New Room Type Name: ");
        String newName = scanner.nextLine().trim();
        System.out.print("Enter New Max Occupancy: ");
        int newMaxOccupancy = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter New Description: ");
        String newDescription = scanner.nextLine().trim();

        RoomType updatedRoomType = new RoomType(newName, newMaxOccupancy, newDescription);

        try {
            RoomType roomType = roomTypeSessionBeanRemote.updateRoomType(roomTypeId, updatedRoomType);
            System.out.println("Room Type updated successfully: " + roomType.getName());
        } catch (RoomTypeNotFoundException | RoomTypeUpdateException e) {
            System.out.println("Error updating Room Type: " + e.getMessage());
        }
    }

    public void deleteRoomType() {
        System.out.print("Enter Room Type ID to delete: ");
        Long roomTypeId = Long.parseLong(scanner.nextLine().trim());

        try {
            roomTypeSessionBeanRemote.deleteRoomType(roomTypeId);
            System.out.println("Room Type deleted successfully.");
        } catch (RoomTypeNotFoundException | RoomTypeDeleteException e) {
            System.out.println("Error deleting Room Type: " + e.getMessage());
        }
    }

    public void viewAllRoomTypes() {
        try {
            List<RoomType> roomTypes = roomTypeSessionBeanRemote.getAllRoomTypes();
            if (roomTypes.isEmpty()) {
                System.out.println("No room types available.");
            } else {
                System.out.println("List of Room Types:");
                roomTypes.forEach(roomType -> {
                    System.out.println("ID: " + roomType.getRoomTypeId() + ", Name: " + roomType.getName());
                });
            }
        } catch (Exception e) {
            System.out.println("Error fetching Room Types: " + e.getMessage());
        }
    }
    
    public void createRoom() throws InputDataValidationException {
        System.out.print("Enter Room Number: ");
        String roomNumber = scanner.nextLine().trim();
        System.out.print("Enter Floor Number: ");
        int floorNumber = Integer.parseInt(scanner.nextLine().trim());

        // Choose Room Type
        System.out.print("Enter Room Type ID: ");
        Long roomTypeId = Long.parseLong(scanner.nextLine().trim());
        RoomType roomType = new RoomType(); // Assume RoomType is fetched by ID from your system
        roomType.setRoomTypeId(roomTypeId);

        System.out.print("Enter Room Status: ");
        String roomStatus = scanner.nextLine().trim();

        Room newRoom = new Room(roomNumber, floorNumber, RoomStatusEnum.valueOf(roomStatus));
        

        try {
            Room createdRoom = roomSessionBeanRemote.createRoom(newRoom);
            System.out.println("Room created successfully: " + createdRoom.getRoomNumber());
        } catch (RoomExistException | UnknownPersistenceException e) {
            System.out.println("Error creating Room: " + e.getMessage());
        }
    }
    
    public void updateRoom() {
        System.out.print("Enter Room ID to update: ");
        Long roomId = Long.parseLong(scanner.nextLine().trim());

        System.out.print("Enter New Room Number: ");
        String newRoomNumber = scanner.nextLine().trim();
        System.out.print("Enter New Floor Number: ");
        int newFloorNumber = Integer.parseInt(scanner.nextLine().trim());

        // Choose Room Type
        System.out.print("Enter New Room Type ID: ");
        Long newRoomTypeId = Long.parseLong(scanner.nextLine().trim());
        RoomType newRoomType = new RoomType(); // Assume RoomType is fetched by ID from your system
        newRoomType.setRoomTypeId(newRoomTypeId);

        System.out.print("Is the room clean? (true/false): ");
        boolean isClean = Boolean.parseBoolean(scanner.nextLine().trim());

        System.out.print("Enter New Room Status (AVAILABLE/RESERVED): ");
        String newRoomStatus = scanner.nextLine().trim();

        Room updatedRoom = new Room(newRoomNumber, newFloorNumber, RoomStatusEnum.valueOf(newRoomStatus));
        updatedRoom.setIsClean(isClean);
        updatedRoom.setRoomType(newRoomType);

        try {
            Room room = roomSessionBeanRemote.updateRoom(roomId, updatedRoom);
            System.out.println("Room updated successfully: " + room.getRoomNumber());
        } catch (RoomNotFoundException | RoomUpdateException e) {
            System.out.println("Error updating Room: " + e.getMessage());
        }
    }
    
    public void deleteRoom() {
        System.out.print("Enter Room ID to delete: ");
        Long roomId = Long.parseLong(scanner.nextLine().trim());

        try {
            roomSessionBeanRemote.deleteRoom(roomId);
            System.out.println("Room deleted successfully.");
        } catch (RoomNotFoundException | RoomDeleteException e) {
            System.out.println("Error deleting Room: " + e.getMessage());
        }
    }

    public void viewAllRooms() {
        try {
            List<Room> rooms = roomSessionBeanRemote.viewAllRooms();
            if (rooms.isEmpty()) {
                System.out.println("No rooms available.");
            } else {
                System.out.println("List of Rooms:");
                rooms.forEach(room -> {
                    System.out.println("ID: " + room.getRoomId() + ", Room Number: " + room.getRoomNumber());
                });
            }
        } catch (Exception e) {
            System.out.println("Error fetching Rooms: " + e.getMessage());
        }
    }

    private void viewAllocationExceptionReports() {
        System.out.println("*** Viewing All Allocation Exception Reports ***\n");

        try {
            List<AllocationException> allocationExceptions = allocationExceptionSessionBeanRemote.viewAllAllocationExceptions();
            
            if (allocationExceptions.isEmpty()) {
                System.out.println("No allocation exceptions found.");
            } else {
                allocationExceptions.stream().map(allocationException -> {
                    System.out.println("ID: " + allocationException.getExceptionId());
                    return allocationException;
                }).map(allocationException -> {
                    System.out.println("Description: " + allocationException.getErrorDescription());
                    return allocationException;
                }).map(allocationException -> {
                    System.out.println("Status: " + allocationException.getResolutionStatus());
                    return allocationException;
                }).map(allocationException -> {
                    System.out.println("Employee: " + (allocationException.getEmployee() != null ? allocationException.getEmployee().getUsername() : "Not assigned"));
                    return allocationException;
                }).map(allocationException -> {
                    System.out.println("Room Allocation ID: " + (allocationException.getRoomAllocation() != null ? allocationException.getRoomAllocation().getAllocationId() : "Not assigned"));
                    return allocationException;
                }).forEachOrdered(_item -> {
                    System.out.println("------------------------------------------------\n");
                });
            }
        } catch (Exception ex) {
            System.err.println("An error occurred while retrieving allocation exceptions: " + ex.getMessage());
        }
    }

    private void runRoomAllocation() {
        System.out.println("*** Room Allocation ***");

        System.out.print("Enter check-in date (yyyy-MM-dd): ");
        String dateInput = scanner.nextLine();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        try {
            Date checkInDate = dateFormat.parse(dateInput);

            roomAllocationSessionBeanRemote.allocateRooms(checkInDate);
            System.out.println("Room allocation process completed for check-in date: " + dateFormat.format(checkInDate));
            
        } catch (ParseException e) {
            System.out.println("Invalid date format! Please enter the date in yyyy-MM-dd format.");
        } catch (RoomAllocationNotFoundException | RoomAllocationUpdateException | ReservationAddRoomAllocationException | ReservationUpdateException e) {
            System.out.println("Error occurred during room allocation: " + e.getMessage());        
        }
    }
    
    public void createNewRoomRate() {
        System.out.println("*** Create New Room Rate ***");

        System.out.print("Enter room rate name: ");
        String name = scanner.nextLine();

        System.out.print("Enter rate type (e.g., NORMAL, PEAK, PROMOTION): ");
        String rateTypeString = scanner.nextLine();
        RateTypeEnum rateType = RateTypeEnum.valueOf(rateTypeString);

        System.out.print("Enter rate per night: ");
        int ratePerNight = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter start date (yyyy-MM-dd): ");
        String startDateString = scanner.nextLine();

        System.out.print("Enter end date (yyyy-MM-dd): ");
        String endDateString = scanner.nextLine();

        // Convert strings to Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);

            RoomRate newRoomRate = new RoomRate(name, rateType, ratePerNight, startDate, endDate);
            roomRateSessionBeanRemote.createRoomRate(newRoomRate);
            System.out.println("Room rate created successfully!");

        } catch (ParseException e) {
            System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
        } catch (RoomRateExistException | UnknownPersistenceException | InputDataValidationException e) {
            System.out.println("Error creating room rate: " + e.getMessage());
        }
    }
    
    public void viewRoomRateDetails() {
        System.out.print("Enter Room Rate ID to view details: ");
        Long roomRateId = scanner.nextLong();

        try {
            RoomRate roomRate = roomRateSessionBeanRemote.retrieveRoomRateById(roomRateId);
            System.out.println("Room Rate ID: " + roomRate.getRoomRateId());
            System.out.println("Name: " + roomRate.getName());
            System.out.println("Rate Type: " + roomRate.getRateType());
            System.out.println("Rate per Night: " + roomRate.getRatePerNight());
            System.out.println("Start Date: " + roomRate.getStartDate());
            System.out.println("End Date: " + roomRate.getEndDate());
            System.out.println("Room Type: " + roomRate.getRoomType().getName());

        } catch (RoomRateNotFoundException e) {
            System.out.println("Room rate not found: " + e.getMessage());
        }
    }
    
    public void updateRoomRate() {
        System.out.print("Enter Room Rate ID to update: ");
        Long roomRateId = scanner.nextLong();
        scanner.nextLine();

        try {
            RoomRate roomRate = roomRateSessionBeanRemote.retrieveRoomRateById(roomRateId);

            System.out.print("Enter new name (current: " + roomRate.getName() + "): ");
            String name = scanner.nextLine();
            roomRate.setName(name);

            System.out.print("Enter new rate type (e.g., NORMAL, PEAK): ");
            String rateTypeString = scanner.nextLine();
            RateTypeEnum rateType = RateTypeEnum.valueOf(rateTypeString);
            roomRate.setRateType(rateType);

            System.out.print("Enter new rate per night: ");
            int ratePerNight = scanner.nextInt();
            roomRate.setRatePerNight(ratePerNight);

            scanner.nextLine(); // Consume newline

            System.out.print("Enter new start date (yyyy-MM-dd): ");
            String startDateString = scanner.nextLine();

            System.out.print("Enter new end date (yyyy-MM-dd): ");
            String endDateString = scanner.nextLine();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            roomRate.setStartDate(dateFormat.parse(startDateString));
            roomRate.setEndDate(dateFormat.parse(endDateString));

            try {
                roomRateSessionBeanRemote.updateRoomRate(roomRateId, roomRate);
            } catch (RoomRateUpdateException ex) {
                Logger.getLogger(HotelOperationModule.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Room rate updated successfully!");

        } catch (RoomRateNotFoundException | ParseException e) {
            System.out.println("Error updating room rate: " + e.getMessage());
        }
    }
    
    public void deleteRoomRate() {
        System.out.print("Enter Room Rate ID to delete: ");
        Long roomRateId = scanner.nextLong();

        try {
            roomRateSessionBeanRemote.deleteRoomRate(roomRateId);
            System.out.println("Room rate deleted successfully!");

        } catch (RoomRateNotFoundException | RoomRateDeleteException e) {
            System.out.println("Error deleting room rate: " + e.getMessage());
        }
    }
    
    public void viewAllRoomRates() {
        try {
            List<RoomRate> roomRates = roomRateSessionBeanRemote.retrieveAllRoomRates();
            System.out.println("*** List of All Room Rates ***");
            roomRates.stream().map(roomRate -> {
                System.out.println("Room Rate ID: " + roomRate.getRoomRateId());
                return roomRate;
            }).map(roomRate -> {
                System.out.println("Name: " + roomRate.getName());
                return roomRate;
            }).map(roomRate -> {
                System.out.println("Rate Type: " + roomRate.getRateType());
                return roomRate;
            }).map(roomRate -> {
                System.out.println("Rate per Night: " + roomRate.getRatePerNight());
                return roomRate;
            }).map(roomRate -> {
                System.out.println("Start Date: " + roomRate.getStartDate());
                return roomRate;
            }).map(roomRate -> {
                System.out.println("End Date: " + roomRate.getEndDate());
                return roomRate;
            }).map(roomRate -> {
                System.out.println("Room Type: " + roomRate.getRoomType().getName());
                return roomRate;
            }).forEachOrdered(_item -> {
                System.out.println("-------------");
            });

        } catch (Exception e) {
            System.out.println("Error retrieving room rates: " + e.getMessage());
        }
    }

}
