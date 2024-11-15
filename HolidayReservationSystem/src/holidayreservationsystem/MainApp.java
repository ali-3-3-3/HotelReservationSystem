/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package holidayreservationsystem;

import java.util.Scanner;
import static sun.security.jgss.GSSUtil.login;
import ws.partner.InvalidLoginCredentialException;
import ws.partner.InvalidLoginCredentialException_Exception;
import ws.partner.Partner;
import ws.partner.PartnerWebService_Service;

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

    public void runApp() throws InvalidLoginCredentialException_Exception {
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

    private void doViewResDetails() {
        Scanner scanner = new Scanner(System.in);
        Long reservationId = null;
        
        System.out.println("*** Holiday Reservation System Reservation Client :: View Partner Reservation Details ***\n");
        System.out.print("Enter reservation ID> ");
        reservationId = new Long(scanner.nextLine().trim());
    }
    
    
    
}
