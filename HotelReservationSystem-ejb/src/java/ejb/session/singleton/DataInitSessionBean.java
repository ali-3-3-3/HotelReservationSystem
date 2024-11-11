package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.RoomRateSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.Employee;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import util.enumerations.EmployeeRoleEnum;
import util.enumerations.RateTypeEnum;
import util.enumerations.RoomStatusEnum;
import util.exceptions.EmployeeExistException;
import util.exceptions.EmployeeNotFoundException;
import util.exceptions.InputDataValidationException;
import util.exceptions.RoomExistException;
import util.exceptions.RoomRateExistException;
import util.exceptions.RoomTypeExistException;
import util.exceptions.RoomTypeNotFoundException;
import util.exceptions.RoomTypeUpdateException;
import util.exceptions.UnknownPersistenceException;


@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {
    @EJB(name = "RoomSessionBeanLocal")
    private RoomSessionBeanLocal roomSessionBeanLocal;

    @EJB(name = "RoomRateSessionBeanLocal")
    private RoomRateSessionBeanLocal roomRateSessionBeanLocal;

    @EJB(name = "RoomTypeSessionBeanLocal")
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    @EJB(name = "EmployeeSessionBeanLocal")
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    public DataInitSessionBean() {
    }
    
    @PostConstruct
    public void postConstruct()
    {
        try {
            employeeSessionBeanLocal.retrieveEmployeeByUsername("sysadmin");
        }
        catch(EmployeeNotFoundException ex) {
            initialiseData();
        }
    }

    public void initialiseData() {
        try {
            // Employee
            employeeSessionBeanLocal.createNewEmployee(new Employee("sysadmin", "password", EmployeeRoleEnum.SYSTEMADMINISTRATOR, "admin1@gmail.com"));
            employeeSessionBeanLocal.createNewEmployee(new Employee("opmanager", "password", EmployeeRoleEnum.OPERATIONMANAGER, "admin2@gmail.com"));
            employeeSessionBeanLocal.createNewEmployee(new Employee("salesmanager", "password", EmployeeRoleEnum.SALESMANAGER, "admin3@gmail.com"));
            employeeSessionBeanLocal.createNewEmployee(new Employee("guestrelo", "password", EmployeeRoleEnum.GUESTRELATIONOFFICER, "admin4@gmail.com"));
            
            // RoomType
            RoomType deluxeRoom = roomTypeSessionBeanLocal.createNewRoomType(new RoomType("Deluxe Room", 2, "description"));
            RoomType premierRoom = roomTypeSessionBeanLocal.createNewRoomType(new RoomType("Premier Room", 3, "description"));
            RoomType familyRoom = roomTypeSessionBeanLocal.createNewRoomType(new RoomType("Family Room", 4, "description"));
            RoomType juniorSuite = roomTypeSessionBeanLocal.createNewRoomType(new RoomType("Junior Suite", 5, "description"));
            RoomType grandSuite = roomTypeSessionBeanLocal.createNewRoomType(new RoomType("Grand Suite", 6, "description"));
            
            deluxeRoom.setNextHigherRoomType(premierRoom);
            premierRoom.setNextHigherRoomType(familyRoom);
            familyRoom.setNextHigherRoomType(juniorSuite);
            juniorSuite.setNextHigherRoomType(grandSuite);
            grandSuite.setNextHigherRoomType(null);
            
            roomTypeSessionBeanLocal.updateRoomType(deluxeRoom.getRoomTypeId(), deluxeRoom);
            roomTypeSessionBeanLocal.updateRoomType(premierRoom.getRoomTypeId(), premierRoom);
            roomTypeSessionBeanLocal.updateRoomType(familyRoom.getRoomTypeId(), familyRoom);
            roomTypeSessionBeanLocal.updateRoomType(juniorSuite.getRoomTypeId(), juniorSuite);
            roomTypeSessionBeanLocal.updateRoomType(grandSuite.getRoomTypeId(), grandSuite);
            
            // RoomRate
            RoomRate roomRate = new RoomRate("Deluxe Room Published", RateTypeEnum.PUBLISHED, 100, new Date(1672531199000L), new Date (1735689600000L));
            roomRate.setRoomType(deluxeRoom);
            roomRateSessionBeanLocal.createRoomRate(roomRate);
            
            roomRate = new RoomRate("Deluxe Room Normal", RateTypeEnum.NORMAL, 50, new Date(1672531199000L), new Date (1735689600000L));
            roomRate.setRoomType(deluxeRoom);
            roomRateSessionBeanLocal.createRoomRate(roomRate);
            
            roomRate = new RoomRate("Premier Room Published", RateTypeEnum.PUBLISHED, 200, new Date(1672531199000L), new Date (1735689600000L));
            roomRate.setRoomType(premierRoom);
            roomRateSessionBeanLocal.createRoomRate(roomRate);
            
            roomRate = new RoomRate("Premier Room Normal", RateTypeEnum.NORMAL, 100, new Date(1672531199000L), new Date (1735689600000L));
            roomRate.setRoomType(premierRoom);
            roomRateSessionBeanLocal.createRoomRate(roomRate);
            
            roomRate = new RoomRate("Family Room Published", RateTypeEnum.PUBLISHED, 300, new Date(1672531199000L), new Date (1735689600000L));
            roomRate.setRoomType(familyRoom);
            roomRateSessionBeanLocal.createRoomRate(roomRate);
            
            roomRate = new RoomRate("Family Room Normal", RateTypeEnum.NORMAL, 150, new Date(1672531199000L), new Date (1735689600000L));
            roomRate.setRoomType(familyRoom);
            roomRateSessionBeanLocal.createRoomRate(roomRate);
            
            roomRate = new RoomRate("Junior Suite Published", RateTypeEnum.PUBLISHED, 400, new Date(1672531199000L), new Date (1735689600000L));
            roomRate.setRoomType(juniorSuite);
            roomRateSessionBeanLocal.createRoomRate(roomRate);
            
            roomRate = new RoomRate("Junior Suite Normal", RateTypeEnum.NORMAL, 200, new Date(1672531199000L), new Date (1735689600000L));
            roomRate.setRoomType(juniorSuite);
            roomRateSessionBeanLocal.createRoomRate(roomRate);
            
            roomRate = new RoomRate("Grand Suite Published", RateTypeEnum.PUBLISHED, 500, new Date(1672531199000L), new Date (1735689600000L));
            roomRate.setRoomType(grandSuite);
            roomRateSessionBeanLocal.createRoomRate(roomRate);
            
            roomRate = new RoomRate("Grand Suite Normal", RateTypeEnum.NORMAL, 250, new Date(1672531199000L), new Date (1735689600000L));
            roomRate.setRoomType(grandSuite);
            roomRateSessionBeanLocal.createRoomRate(roomRate);
            
            // Room
            Room room = new Room("0101", 01, RoomStatusEnum.AVAILABLE);
            room.setRoomType(deluxeRoom);
            roomSessionBeanLocal.createRoom(room);
            room = new Room("0201", 02, RoomStatusEnum.AVAILABLE);
            room.setRoomType(deluxeRoom);
            roomSessionBeanLocal.createRoom(room);
            room = new Room("0301", 03, RoomStatusEnum.AVAILABLE);
            room.setRoomType(deluxeRoom);
            roomSessionBeanLocal.createRoom(room);
            room = new Room("0401", 04, RoomStatusEnum.AVAILABLE);
            room.setRoomType(deluxeRoom);
            roomSessionBeanLocal.createRoom(room);
            room = new Room("0501", 05, RoomStatusEnum.AVAILABLE);
            room.setRoomType(deluxeRoom);
            roomSessionBeanLocal.createRoom(room);
            
            room = new Room("0102", 01, RoomStatusEnum.AVAILABLE);
            room.setRoomType(premierRoom);
            roomSessionBeanLocal.createRoom(room);
            room = new Room("0202", 02, RoomStatusEnum.AVAILABLE);
            room.setRoomType(premierRoom);
            roomSessionBeanLocal.createRoom(room);
            room = new Room("0302", 03, RoomStatusEnum.AVAILABLE);
            room.setRoomType(premierRoom);
            roomSessionBeanLocal.createRoom(room);
            room = new Room("0402", 04, RoomStatusEnum.AVAILABLE);
            room.setRoomType(premierRoom);
            roomSessionBeanLocal.createRoom(room);
            room = new Room("0502", 05, RoomStatusEnum.AVAILABLE);
            room.setRoomType(premierRoom);
            roomSessionBeanLocal.createRoom(room);
            
            room = new Room("0103", 01, RoomStatusEnum.AVAILABLE);
            room.setRoomType(familyRoom);
            roomSessionBeanLocal.createRoom(room);
            room = new Room("0203", 02, RoomStatusEnum.AVAILABLE);
            room.setRoomType(familyRoom);
            roomSessionBeanLocal.createRoom(room);
            room = new Room("0303", 03, RoomStatusEnum.AVAILABLE);
            room.setRoomType(familyRoom);
            roomSessionBeanLocal.createRoom(room);
            room = new Room("0403", 04, RoomStatusEnum.AVAILABLE);
            room.setRoomType(familyRoom);
            roomSessionBeanLocal.createRoom(room);
            room = new Room("0503", 05, RoomStatusEnum.AVAILABLE);
            room.setRoomType(familyRoom);
            roomSessionBeanLocal.createRoom(room);
            
            room = new Room("0104", 01, RoomStatusEnum.AVAILABLE);
            room.setRoomType(juniorSuite);
            roomSessionBeanLocal.createRoom(room);
            room = new Room("0204", 02, RoomStatusEnum.AVAILABLE);
            room.setRoomType(juniorSuite);
            roomSessionBeanLocal.createRoom(room);
            room = new Room("0304", 03, RoomStatusEnum.AVAILABLE);
            room.setRoomType(juniorSuite);
            roomSessionBeanLocal.createRoom(room);
            room = new Room("0404", 04, RoomStatusEnum.AVAILABLE);
            room.setRoomType(juniorSuite);
            roomSessionBeanLocal.createRoom(room);
            room = new Room("0504", 05, RoomStatusEnum.AVAILABLE);
            room.setRoomType(juniorSuite);
            roomSessionBeanLocal.createRoom(room);
            
            room = new Room("0105", 01, RoomStatusEnum.AVAILABLE);
            room.setRoomType(grandSuite);
            roomSessionBeanLocal.createRoom(room);
            room = new Room("0205", 02, RoomStatusEnum.AVAILABLE);
            room.setRoomType(grandSuite);
            roomSessionBeanLocal.createRoom(room);
            room = new Room("0305", 03, RoomStatusEnum.AVAILABLE);
            room.setRoomType(grandSuite);
            roomSessionBeanLocal.createRoom(room);
            room = new Room("0405", 04, RoomStatusEnum.AVAILABLE);
            room.setRoomType(grandSuite);
            roomSessionBeanLocal.createRoom(room);
            room = new Room("0505", 05, RoomStatusEnum.AVAILABLE);
            room.setRoomType(grandSuite);
            roomSessionBeanLocal.createRoom(room);
            
            
        } catch(RoomExistException | RoomRateExistException | EmployeeExistException | RoomTypeNotFoundException | RoomTypeUpdateException | RoomTypeExistException | InputDataValidationException | UnknownPersistenceException ex) {
            ex.printStackTrace();
        }
        
    }
}
