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
            System.out.println("\n======Welcome Sales Manager======");
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
        } while (option != 6);
    }
    
    public void createNewRoomType() {
        try {
            System.out.print("Enter Room Type Name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Room Type Name cannot be empty.");
                return;
            }

            System.out.print("Enter Max Occupancy: ");
            int maxOccupancy;
            try {
                maxOccupancy = Integer.parseInt(scanner.nextLine().trim());
                if (maxOccupancy <= 0) {
                    System.out.println("Max Occupancy must be a positive integer.");
                    return;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input for Max Occupancy. Please enter a valid number.");
                return;
            }

            System.out.print("Enter Description: ");
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                System.out.println("Description cannot be empty.");
                return;
            }
            
            RoomType newRoomType = new RoomType(name, maxOccupancy, description);
            
            System.out.println("Is there a next higher room type?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.print(">");
            
            int choice = 0;
            choice = Integer.parseInt(scanner.nextLine());
            
            if(choice == 1 ) {
                System.out.print("What is the Room Type Name?: " );
                String nextHighestRoom = scanner.nextLine().trim();
                if (nextHighestRoom.isEmpty()) {
                    System.out.println("Description cannot be empty.");
                    return;
                }
                
                RoomType nextHighestRoomType = roomTypeSessionBeanRemote.retrieveRoomTypeByName(nextHighestRoom);
                
                newRoomType.setNextHigherRoomType(nextHighestRoomType);          
            }     

            try {
                RoomType createdRoomType = roomTypeSessionBeanRemote.createNewRoomType(newRoomType);
                System.out.println("Room Type created successfully: " + createdRoomType.getName());
            } catch (InputDataValidationException e) {
                System.out.println("Invalid input: Please ensure all fields are correctly filled.");
            } catch (RoomTypeExistException e) {
                System.out.println("Error: Room Type with this name already exists.");
            } catch (UnknownPersistenceException e) {
                System.out.println("An unexpected error occurred. Please try again.");
                Logger.getLogger(FrontOfficeModule.class.getName()).log(Level.SEVERE, "Error creating Room Type", e);
            }
        } catch (NumberFormatException | RoomTypeNotFoundException ex) {
            System.out.println("An unexpected error occurred. Please try again.");
            Logger.getLogger(FrontOfficeModule.class.getName()).log(Level.SEVERE, "Unexpected error in createNewRoomType", ex);
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
        Long roomTypeId = null;
        String newName;
        int newMaxOccupancy;
        String newDescription;

        try {
            System.out.print("Enter Room Type ID to update: ");
            roomTypeId = Long.parseLong(scanner.nextLine().trim());

            System.out.print("Enter New Room Type Name: ");
            newName = scanner.nextLine().trim();
            if (newName.isEmpty()) {
                System.out.println("Room Type Name cannot be empty.");
                return;
            }

            System.out.print("Enter New Max Occupancy: ");
            newMaxOccupancy = Integer.parseInt(scanner.nextLine().trim());
            if (newMaxOccupancy <= 0) {
                System.out.println("Max Occupancy must be a positive integer.");
                return;
            }

            System.out.print("Enter New Description: ");
            newDescription = scanner.nextLine().trim();
            if (newDescription.isEmpty()) {
                System.out.println("Description cannot be empty.");
                return;
            }

            RoomType updatedRoomType = new RoomType(newName, newMaxOccupancy, newDescription);
            
            System.out.println("Is there a next higher room type?");
            System.out.println("1. Yes");
            System.out.print("2. No");
            System.out.print(">");
            
            int choice = 0;
            choice = Integer.parseInt(scanner.nextLine());
            
            if(choice == 1 ) {
                System.out.print("What is the Room Type Name?: ");
                String nextHighestRoom = scanner.nextLine().trim();
                if (nextHighestRoom.isEmpty()) {
                    System.out.println("Description cannot be empty.");
                    return;
                }
                
                RoomType nextHighestRoomType = roomTypeSessionBeanRemote.retrieveRoomTypeByName(nextHighestRoom);
                
                updatedRoomType.setNextHigherRoomType(nextHighestRoomType);          
            }  

            RoomType roomType = roomTypeSessionBeanRemote.updateRoomType(roomTypeId, updatedRoomType);
            System.out.println("Room Type updated successfully: " + roomType.getName());

        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please enter numeric values where required.");
        } catch (RoomTypeNotFoundException e) {
            System.out.println("Error: Room Type with ID " + roomTypeId + " not found.");
        } catch (RoomTypeUpdateException e) {
            System.out.println("Error updating Room Type: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
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
    
    public void createRoom() {
        String roomNumber;
        int floorNumber;
        Long roomTypeId;
        String roomStatus;

        try {
            System.out.print("Enter Room Number: ");
            roomNumber = scanner.nextLine().trim();
            if (roomNumber.isEmpty()) {
                System.out.println("Room Number cannot be empty.");
                return;
            }

            System.out.print("Enter Floor Number: ");
            floorNumber = Integer.parseInt(scanner.nextLine().trim());
            if (floorNumber < 0) {
                System.out.println("Floor Number must be a positive integer.");
                return;
            }

            System.out.print("Enter Room Type ID: ");
            roomTypeId = Long.parseLong(scanner.nextLine().trim());

            RoomType roomType;
            
            try {
                roomType = roomTypeSessionBeanRemote.retrieveRoomTypeById(roomTypeId);
            } catch (RoomTypeNotFoundException ex) {
                System.out.println("Error: Room Type with ID " + roomTypeId + " not found.");
                return;
            }

            System.out.print("Enter Room Status (e.g., AVAILABLE, RESERVED): ");
            roomStatus = scanner.nextLine().trim();
            try {
                RoomStatusEnum statusEnum = RoomStatusEnum.valueOf(roomStatus);
                Room newRoom = new Room(roomNumber, floorNumber, statusEnum);
                newRoom.setRoomType(roomType);

                Room createdRoom = roomSessionBeanRemote.createRoom(newRoom);
                System.out.println("Room created successfully: " + createdRoom.getRoomNumber());

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Room Status. Please enter a valid status such as AVAILABLE or OCCUPIED.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please enter numeric values where required.");
        } catch (RoomExistException e) {
            System.out.println("Error: A room with this number already exists.");
        } catch (UnknownPersistenceException e) {
            System.out.println("An unexpected error occurred while creating the room. Please try again.");
        } catch (InputDataValidationException e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    
    public void updateRoom() {
        Long roomId;
        String newRoomNumber;
        int newFloorNumber;
        Long newRoomTypeId;
        boolean isClean;
        String newRoomStatus;

        try {
            System.out.print("Enter Room ID to update: ");
            roomId = Long.parseLong(scanner.nextLine().trim());

            System.out.print("Enter New Room Number: ");
            newRoomNumber = scanner.nextLine().trim();
            if (newRoomNumber.isEmpty()) {
                System.out.println("Room Number cannot be empty.");
                return;
            }

            System.out.print("Enter New Floor Number: ");
            newFloorNumber = Integer.parseInt(scanner.nextLine().trim());
            if (newFloorNumber < 0) {
                System.out.println("Floor Number must be a positive integer.");
                return;
            }

            System.out.print("Enter New Room Type ID: ");
            newRoomTypeId = Long.parseLong(scanner.nextLine().trim());

            // Fetch RoomType by ID
            RoomType newRoomType;
            try {
                newRoomType = roomTypeSessionBeanRemote.retrieveRoomTypeById(newRoomTypeId);
            } catch (RoomTypeNotFoundException ex) {
                System.out.println("Error: Room Type with ID " + newRoomTypeId + " not found.");
                return;
            }

            System.out.print("Is the room clean? (true/false): ");
            String isCleanInput = scanner.nextLine().trim();
            if (!isCleanInput.equalsIgnoreCase("true") && !isCleanInput.equalsIgnoreCase("false")) {
                System.out.println("Invalid input for room cleanliness. Please enter 'true' or 'false'.");
                return;
            }
            isClean = Boolean.parseBoolean(isCleanInput);

            System.out.print("Enter New Room Status (AVAILABLE/RESERVED): ");
            newRoomStatus = scanner.nextLine().trim();
            RoomStatusEnum statusEnum;
            try {
                statusEnum = RoomStatusEnum.valueOf(newRoomStatus);
            } catch (IllegalArgumentException ex) {
                System.out.println("Invalid Room Status. Please enter a valid status, such as AVAILABLE or RESERVED.");
                return;
            }

            Room updatedRoom = new Room(newRoomNumber, newFloorNumber, statusEnum);
            updatedRoom.setIsClean(isClean);
            updatedRoom.setRoomType(newRoomType);

            try {
                Room room = roomSessionBeanRemote.updateRoom(roomId, updatedRoom);
                System.out.println("Room updated successfully: " + room.getRoomNumber());
            } catch (RoomNotFoundException e) {
                System.out.println("Error: Room with ID " + roomId + " not found.");
            } catch (RoomUpdateException e) {
                System.out.println("An error occurred while updating the room. Please try again.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please enter numeric values where required.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
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

        // Get room rate name
        System.out.print("Enter room rate name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Room rate name cannot be empty.");
            return;
        }

        // Get rate type with validation
        System.out.print("Enter rate type (PUBLISHED, NORMAL, PEAK, PROMOTION): ");
        String rateTypeString = scanner.nextLine().trim();
        RateTypeEnum rateType;
        try {
            rateType = RateTypeEnum.valueOf(rateTypeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid rate type. Please enter one of the following: NORMAL, PUBLISHED, PEAK, PROMOTION.");
            return;
        }

        // Get rate per night and validate
        System.out.print("Enter rate per night: ");
        int ratePerNight;
        try {
            ratePerNight = Integer.parseInt(scanner.nextLine().trim());
            if (ratePerNight < 0) {
                System.out.println("Rate per night must be a positive integer.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for rate per night. Please enter a numeric value.");
            return;
        }
        
        System.out.print("Enter Room Type ID: ");
        Long roomTypeId = Long.parseLong(scanner.nextLine().trim());

        RoomType roomType;

        try {
            roomType = roomTypeSessionBeanRemote.retrieveRoomTypeById(roomTypeId);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Error: Room Type with ID " + roomTypeId + " not found.");
            return;
        }

        // Get and validate start date
        System.out.print("Enter start date (yyyy-MM-dd): ");
        String startDateString = scanner.nextLine().trim();
        Date startDate;
        Date endDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        try {
            startDate = dateFormat.parse(startDateString);
        } catch (ParseException e) {
            System.out.println("Invalid start date format. Please enter the date in yyyy-MM-dd format.");
            return;
        }

        // Get and validate end date
        System.out.print("Enter end date (yyyy-MM-dd): ");
        String endDateString = scanner.nextLine().trim();
        try {
            endDate = dateFormat.parse(endDateString);
            if (endDate.before(startDate)) {
                System.out.println("End date must be after the start date.");
                return;
            }
        } catch (ParseException e) {
            System.out.println("Invalid end date format. Please enter the date in yyyy-MM-dd format.");
            return;
        }

        // Create RoomRate
        try {
            RoomRate newRoomRate = new RoomRate(name, rateType, ratePerNight, startDate, endDate);
            newRoomRate.setRoomType(roomType);
            roomRateSessionBeanRemote.createRoomRate(newRoomRate);
            System.out.println("Room rate created successfully!");

        } catch (RoomRateExistException e) {
            System.out.println("Error: Room rate with this name already exists. Please choose a different name.");
        } catch (InputDataValidationException e) {
            System.out.println("Input data validation error: " + e.getMessage());
        } catch (UnknownPersistenceException e) {
            System.out.println("An unexpected error occurred while creating the room rate. Please try again later.");
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
        Long roomRateId;
        try {
            roomRateId = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Room Rate ID. Please enter a numeric value.");
            return;
        }

        try {
            RoomRate roomRate = roomRateSessionBeanRemote.retrieveRoomRateById(roomRateId);

            // Update name
            System.out.print("Enter new name (current: " + roomRate.getName() + "): ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                roomRate.setName(name);
            }

            // Update rate type with validation
            System.out.print("Enter new rate type (PUBLISHED, NORMAL, PEAK, PROMOTION) (current: " + roomRate.getRateType() + "): ");
            String rateTypeString = scanner.nextLine().trim();
            try {
                RateTypeEnum rateType = RateTypeEnum.valueOf(rateTypeString.toUpperCase());
                roomRate.setRateType(rateType);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid rate type. Please enter one of the following: PUBLISHED, NORMAL, PEAK, PROMOTION.");
                return;
            }

            // Update rate per night with validation
            System.out.print("Enter new rate per night (current: " + roomRate.getRatePerNight() + "): ");
            int ratePerNight;
            try {
                ratePerNight = Integer.parseInt(scanner.nextLine().trim());
                if (ratePerNight < 0) {
                    System.out.println("Rate per night must be a positive integer.");
                    return;
                }
                roomRate.setRatePerNight(ratePerNight);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for rate per night. Please enter a numeric value.");
                return;
            }

            // Update start and end dates with validation
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);

            System.out.print("Enter new start date (yyyy-MM-dd) (current: " + dateFormat.format(roomRate.getStartDate()) + "): ");
            String startDateString = scanner.nextLine().trim();
            System.out.print("Enter new end date (yyyy-MM-dd) (current: " + dateFormat.format(roomRate.getEndDate()) + "): ");
            String endDateString = scanner.nextLine().trim();

            try {
                Date startDate = dateFormat.parse(startDateString);
                Date endDate = dateFormat.parse(endDateString);
                if (endDate.before(startDate)) {
                    System.out.println("End date must be after the start date.");
                    return;
                }
                roomRate.setStartDate(startDate);
                roomRate.setEndDate(endDate);
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                return;
            }

            // Attempt to update Room Rate
            try {
                roomRateSessionBeanRemote.updateRoomRate(roomRateId, roomRate);
                System.out.println("Room rate updated successfully!");
            } catch (RoomRateUpdateException ex) {
                Logger.getLogger(HotelOperationModule.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("An error occurred while updating the room rate. Please try again.");
            }

        } catch (RoomRateNotFoundException e) {
            System.out.println("Error: Room rate with ID " + roomRateId + " not found.");
        }
    }

    
    public void deleteRoomRate() {
        System.out.print("Enter Room Rate ID to delete: ");
        Long roomRateId = Long.parseLong(scanner.nextLine().trim());

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
