/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package holidayreservationsystem;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import ws.partner.InvalidLoginCredentialException_Exception;
import ws.partner.Partner;
import ws.partner.PartnerWebService_Service;
import ws.partner.Reservation;
import ws.partner.ReservationNotFoundException_Exception;
import ws.partner.RoomAllocation;
import ws.partner.RoomRate;
import ws.partner.RoomType;

/**
 *
 * @author castellelow
 */
class MainApp {

    PartnerWebService_Service service = new PartnerWebService_Service();
    
    private boolean login = false;

    public MainApp() {

    }

    private Partner currentPartner = null;

    public void runApp() throws InvalidLoginCredentialException_Exception, ReservationNotFoundException_Exception {
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
                                System.out.println("Login successful!");
                        } 
                        break;
                    case 2:
                        doSearchHotelRoom();
                        break;
                    case 3:
                        
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

    private void doLogin() throws InvalidLoginCredentialException_Exception {
        Scanner scanner = new Scanner(System.in);
        String email = "";
        String password = "";
        
        System.out.println("*** Holiday Reservation System Reservation Client :: Login ***");
        System.out.print("Enter email> ");
        email = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        currentPartner = service.getPartnerWebServicePort().doLogin(email, password);
     }

    private void doSearchHotelRoom() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewResDetails() throws ReservationNotFoundException_Exception {
        Scanner scanner = new Scanner(System.in);
        Long reservationId = null;
        Reservation reservation;
        
        System.out.println("*** Holiday Reservation System Reservation Client :: View Partner Reservation Details ***\n");
        System.out.print("Enter reservation ID> ");
        reservationId = new Long(scanner.nextLine().trim());
        
        reservation = service.getPartnerWebServicePort().retrievePartnerReservationsByReservationId(reservationId);
        System.out.printf("%5s%40s%40s%40s\n","ID", "Reservation Type",  "Check-in Date", "Check-out Date");
        System.out.printf("%5s%40s%40s%40s\n",reservation.getReservationId(),reservation.getRoomType(), 
                        reservation.getCheckInDate().toString(), reservation.getCheckOutDate().toString());
        System.out.println("\n");
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
}
    
    
    

