/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package holidayreservationsystem;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.partner.InputDataValidationException_Exception;
import ws.partner.InvalidLoginCredentialException_Exception;
import ws.partner.InvalidRoomCountException_Exception;
import ws.partner.Partner;
import ws.partner.PartnerWebService;
import ws.partner.PartnerWebService_Service;
import ws.partner.Reservation;
import ws.partner.ReservationNotFoundException_Exception;
import ws.partner.RoomAllocation;
import ws.partner.RoomRate;
import ws.partner.RoomType;
import ws.partner.RoomTypeNotFoundException_Exception;
import ws.partner.RoomTypeUnavailableException_Exception;
import ws.partner.UnknownPersistenceException_Exception;

/**
 *
 * @author castellelow
 */
class MainApp {

    PartnerWebService_Service service = new PartnerWebService_Service();
    PartnerWebService port = service.getPartnerWebServicePort();

    private boolean login = false;

    public MainApp() {

    }

    private Partner currentPartner = null;

    public void runApp() throws InvalidLoginCredentialException_Exception, ReservationNotFoundException_Exception, RoomTypeNotFoundException_Exception, InputDataValidationException_Exception {
        // Enable SOAP logging
        System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dumpThreshold", "999999");

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to Holiday Reservation System Client ***");
            System.out.println("1: Partner Login");
            System.out.println("2: Search Hotel Room");
            System.out.println("3: Reserve Hotel Room");
            System.out.println("4: View Partner Reservation Details");
            System.out.println("5: View All Partner Reservations");
            System.out.println("6: Exit\n");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");

                response = scanner.nextInt();

                switch (response) {
                    case 1:
                        if (currentPartner == null && !login) {
                            doLogin();

                        }
                        break;
                    case 2:
                        doSearchHotelRoom();
                        break;
                    case 3:
                        doReserveHotel();
                        break;
                    case 4:
                        doViewResDetails();
                        break;
                    case 5:
                        doViewAllRes();
                        break;
                    case 6:

                        break;

                }
            }
        }
    }

    private void doLogin() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter email> ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();

        try {
            PartnerWebService_Service service = new PartnerWebService_Service();
            Partner partner = service.getPartnerWebServicePort().doLogin(email, password);
            currentPartner = partner;
            System.out.println("Login successful! Welcome, " + partner.getSystemName());
        } catch (InvalidLoginCredentialException_Exception e) {
            System.err.println("Invalid login credentials: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private void doSearchHotelRoom() throws InputDataValidationException_Exception, RoomTypeNotFoundException_Exception {
        System.out.println("*** Search Hotel Room ***\n");
        Scanner scanner = new Scanner(System.in);

        Date checkInDate = null;
        Date checkOutDate = null;

        try {
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

            // Convert Date to XMLGregorianCalendar
            XMLGregorianCalendar checkInXML = convertToXMLGregorianCalendar(checkInDate);
            XMLGregorianCalendar checkOutXML = convertToXMLGregorianCalendar(checkOutDate);

            // Call searchRoom
            List<RoomType> availableRoomTypes = service.getPartnerWebServicePort().searchRoom(checkInXML, checkOutXML);

            if (availableRoomTypes != null && !availableRoomTypes.isEmpty()) {
                System.out.println("Available Room Types:");
                for (int i = 0; i < availableRoomTypes.size(); i++) {
                    RoomType roomType = availableRoomTypes.get(i);
                    System.out.println((i + 1) + ": " + roomType.getName());

                    // Call calculatePre for each room type
                    // Convert XMLGregorianCalendar to Date
//                    Date checkIn = convertToDate(checkInXML);
//                    Date checkOut = convertToDate(checkOutXML);


                    double totalCost = service.getPartnerWebServicePort().calculatePre(checkOutXML, checkOutXML, roomType);
                    System.out.println("Total cost: $" + totalCost);
                }
            } else {
                System.out.println("No available room types found.");
            }

        } catch (Exception ex) {
            System.err.println("An error occurred while searching for room types: " + ex.getMessage());
        }
    }

    private Date convertToDate(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) {
            return null;
        }
        return xmlGregorianCalendar.toGregorianCalendar().getTime();
    }

    private XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {
        if (date == null) {
            return null;
        }
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        } catch (DatatypeConfigurationException ex) {
            System.err.println("Error converting Date to XMLGregorianCalendar: " + ex.getMessage());
            return null;
        }
    }

    private Date getDateInput(String dateType) {
        int year = 0, month = 0, day = 0;
        Scanner scanner = new Scanner(System.in);

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

    private void doViewResDetails() {
    Scanner scanner = new Scanner(System.in);

    try {
        System.out.println("*** View Partner Reservation Details ***");
        System.out.print("Enter reservation ID> ");
        Long reservationId = Long.parseLong(scanner.nextLine().trim());

        // Retrieve reservation from the web service
        Reservation reservation = service.getPartnerWebServicePort().retrievePartnerReservationsByReservationId(reservationId);

        if (reservation == null) {
            System.out.println("Reservation not found for ID: " + reservationId);
            return;
        }

        // Print reservation details
        System.out.println("Reservation Details:");
        System.out.println("Reservation ID: " + reservation.getReservationId());
        System.out.println("Check-In Date: " + reservation.getCheckInDate());
        System.out.println("Check-Out Date: " + reservation.getCheckOutDate());

        if (reservation.getRoomType() == null) {
            System.out.println("Room Type: Not specified");
        } else {
            System.out.println("Room Type: " + reservation.getRoomType().getName());
        }

    } catch (ReservationNotFoundException_Exception ex) {
        System.err.println("Error: Reservation not found. " + ex.getMessage());
    } catch (NumberFormatException ex) {
        System.err.println("Invalid reservation ID. Please enter a valid number.");
    } catch (Exception ex) {
        System.err.println("An unexpected error occurred: " + ex.getMessage());
    }
}


    private void doViewAllRes() {
        Scanner scanner = new Scanner(System.in);
        Long partnerId = null;
        List<Reservation> reservations;

        System.out.println("*** Holiday Reservation System Reservation Client :: View Partner Reservation Details ***\n");
        System.out.print("Enter partner ID> ");
        partnerId = new Long(scanner.nextLine().trim());

        reservations = service.getPartnerWebServicePort().viewReservationsByPartnerId(partnerId);

        reservations.forEach(reservation -> {
            System.out.println("Reservation ID: " + reservation.getReservationId());
            System.out.println("Has Checked In: " + reservation.isHasCheckedIn());
            System.out.println("Has Checked Out: " + reservation.isHasCheckedOut());
            System.out.println("Number of Rooms: " + reservation.getNumOfRooms());

            RoomType roomType = reservation.getRoomType();
            System.out.println("Room Type: " + (roomType != null ? roomType.getName() : "Not specified"));

            List<RoomRate> roomRates = reservation.getRoomRates();
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
        System.out.println("\n");
    }
    // Helper methods for date and time formatting

    private String formatDate(Date date) {
        return date != null ? new SimpleDateFormat("yyyy-MM-dd").format(date) : "Not specified";
    }

    private String formatTime(LocalTime time) {
        return time != null ? time.toString() : "Not specified";
    }

    private void doReserveHotel() {
        try {
            Scanner scanner = new Scanner(System.in);
            Long typeId;
            int noOfRooms;
            Long guestId;
            String checkinDate, checkoutDate;
            Reservation reservation;

            System.out.println("*** Reservation ***\n");

            System.out.print("Select a room type> ");
            typeId = scanner.nextLong();
            scanner.nextLine(); // Consume leftover newline

            System.out.print("Enter Check-in Date (yyyy-MM-dd)> ");
            checkinDate = scanner.nextLine().trim();

            System.out.print("Enter Check-out Date (yyyy-MM-dd)> ");
            checkoutDate = scanner.nextLine().trim();

            System.out.print("Enter number of rooms> ");
            noOfRooms = scanner.nextInt();
            scanner.nextLine(); // Consume leftover newline

            System.out.print("Enter guestId> ");
            guestId = scanner.nextLong();

            Reservation reserve = service.getPartnerWebServicePort()
                    .reserveNewReservation(checkinDate, checkoutDate, noOfRooms, typeId, guestId);

            reserve.setPartner(currentPartner);
            if (reserve != null) {
                System.out.println("Reservation successful!\n");
                System.out.println("Reservation successfully created!");
                System.out.println("Reservation Details:");
                System.out.println("ReservationID: " + reserve.getReservationId());
            } else {
                System.out.println("Reservation failed\n");
            }
        } catch (InputDataValidationException_Exception ex) {
            System.err.println("Validation Error: " + ex.getMessage());
        } catch (InvalidRoomCountException_Exception ex) {
            System.err.println("Invalid Room Count: " + ex.getMessage());
        } catch (RoomTypeUnavailableException_Exception ex) {
            System.err.println("Room Unavailable: " + ex.getMessage());
        } catch (UnknownPersistenceException_Exception ex) {
            System.err.println("Persistence Error: " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("An unexpected error occurred: " + ex.getMessage());
        }
    }

}
