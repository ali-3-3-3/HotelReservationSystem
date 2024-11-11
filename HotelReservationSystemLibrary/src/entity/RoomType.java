package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import util.exceptions.RoomTypeAddReservationException;
import util.exceptions.RoomTypeAddRoomException;
import util.exceptions.RoomTypeAddRoomRateException;

@Entity
public class RoomType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeId;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private String name;
    
    @Digits(integer = 2, fraction = 0)
    @NotNull
    private int maxOccupancy;
    
    @Column(length = 256, nullable = false)
    @NotNull
    private String description;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nextHigherRoomTypeId")
    private RoomType nextHigherRoomType;
    
    @OneToMany(mappedBy = "roomType", fetch = FetchType.LAZY)
    private List<Room> rooms;
    
    @OneToMany(mappedBy = "roomType", fetch = FetchType.LAZY)
    private List<Reservation> reservations;
    
    @OneToMany(mappedBy = "roomType", fetch = FetchType.LAZY)
    private List<RoomRate> roomRates;
    
    public RoomType() {
        this.rooms = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.roomRates = new ArrayList<>();
    }

    public RoomType(String typeName, int maxOccupancy, String description) {
        this();
        
        this.name = typeName;
        this.maxOccupancy = maxOccupancy;
        this.description = description;
    }
    
    public void addReservation(Reservation reservation) throws RoomTypeAddReservationException {
        if(reservation != null && !this.getReservations().contains(reservation))
        {
            this.getReservations().add(reservation);
        }
        else
        {
            throw new RoomTypeAddReservationException("Reservation already added to room type");
        }
    }
    
    public void addRoom(Room room) throws RoomTypeAddRoomException {
        if(room != null && !this.getRooms().contains(room))
        {
            this.getRooms().add(room);
        }
        else
        {
            throw new RoomTypeAddRoomException("Room already added to room type");
        }
    }
    
    public void addRoomRate(RoomRate roomRate) throws RoomTypeAddRoomRateException {
        if(roomRate != null && !this.getRoomRates().contains(roomRate))
        {
            this.getRoomRates().add(roomRate);
        }
        else
        {
            throw new RoomTypeAddRoomRateException("RoomRate already added to room type");
        }
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomTypeId != null ? roomTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomTypeId fields are not set
        if (!(object instanceof RoomType)) {
            return false;
        }
        RoomType other = (RoomType) object;
        if ((this.roomTypeId == null && other.roomTypeId != null) || (this.roomTypeId != null && !this.roomTypeId.equals(other.roomTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomType[ id=" + roomTypeId + " ]";
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<RoomRate> getRoomRates() {
        return roomRates;
    }

    public void setRoomRates(List<RoomRate> roomRates) {
        this.roomRates = roomRates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoomType getNextHigherRoomType() {
        return nextHigherRoomType;
    }

    public void setNextHigherRoomType(RoomType nextHigherRoomType) {
        this.nextHigherRoomType = nextHigherRoomType;
    }
    
}
