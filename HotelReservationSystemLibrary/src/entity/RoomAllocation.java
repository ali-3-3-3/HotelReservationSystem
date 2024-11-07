package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import util.enumerations.RoomAllocationStatusEnum;

@Entity
public class RoomAllocation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allocationId;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @NotNull
    private Date allocationDate;
    
    @Enumerated(EnumType.STRING)
    private RoomAllocationStatusEnum status;
    
    @ManyToOne(optional = true)
    private Room room;
    
    @ManyToOne(optional = false)
    private Reservation reservation;

    public RoomAllocation() {
    }

    public RoomAllocation(Long allocationId, Date allocationDate, RoomAllocationStatusEnum status, Room room, Reservation reservation) {
        this.allocationId = allocationId;
        this.allocationDate = allocationDate;
        this.status = status;
        this.room = room;
        this.reservation = reservation;
    }
    
    public Long getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(Long allocationId) {
        this.allocationId = allocationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (allocationId != null ? allocationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the allocationId fields are not set
        if (!(object instanceof RoomAllocation)) {
            return false;
        }
        RoomAllocation other = (RoomAllocation) object;
        if ((this.allocationId == null && other.allocationId != null) || (this.allocationId != null && !this.allocationId.equals(other.allocationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomAllocation[ id=" + allocationId + " ]";
    }

    public Date getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(Date allocationDate) {
        this.allocationDate = allocationDate;
    }

    public RoomAllocationStatusEnum getStatus() {
        return status;
    }

    public void setStatus(RoomAllocationStatusEnum status) {
        this.status = status;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
    
}
