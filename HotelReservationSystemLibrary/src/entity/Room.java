package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import util.enumerations.RoomStatusEnum;
import util.exceptions.RoomAddRoomAllocationException;

@Entity
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    
    @Column(length = 4, nullable = false, unique = true)
    @Pattern(regexp = "^[0-9]{2}[0-9]{2}$", message = "Room number must follow the format of a two-digit floor and a two-digit sequence, e.g., 2015.")
    @NotNull
    private String roomNumber;
    
    @Column (length = 2, nullable = false)
    @NotNull
    private int floorNumber;
    
    @NotNull
    private boolean isClean;
    
    @NotNull
    private RoomStatusEnum roomStatus;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "roomTypeId", nullable = false)
    @NotNull
    private RoomType roomType;
    
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<RoomAllocation> roomAllocations;

    public Room() {
        this.isClean = false;
        this.roomAllocations = new ArrayList<>();
    }

    public Room(String roomNumber, int floorNumber, RoomStatusEnum roomStatus) {
        this();
        
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
        this.roomStatus = roomStatus;
    }

    
    public void addRoomAllocation(RoomAllocation roomAllocation) throws RoomAddRoomAllocationException {
        if(roomAllocation != null && !this.getRoomAllocations().contains(roomAllocation))
        {
            this.getRoomAllocations().add(roomAllocation);
        }
        else
        {
            throw new RoomAddRoomAllocationException("Room Allocation already added to reservation");
        }
    }
    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomId != null ? roomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomId fields are not set
        if (!(object instanceof Room)) {
            return false;
        }
        Room other = (Room) object;
        if ((this.roomId == null && other.roomId != null) || (this.roomId != null && !this.roomId.equals(other.roomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Room[ id=" + roomId + " ]";
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public boolean isIsClean() {
        return isClean;
    }

    public void setIsClean(boolean isClean) {
        this.isClean = isClean;
    }

    public List<RoomAllocation> getRoomAllocations() {
        return roomAllocations;
    }

    public void setRoomAllocations(List<RoomAllocation> roomAllocations) {
        this.roomAllocations = roomAllocations;
    }

    public RoomStatusEnum getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(RoomStatusEnum roomStatus) {
        this.roomStatus = roomStatus;
    }
    
}
