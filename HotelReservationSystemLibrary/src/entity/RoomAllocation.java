package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class RoomAllocation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allocationId;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @NotNull
    private Date allocationDate;
    
    @NotNull
    private boolean isException;
    
    @ManyToOne (optional = true, fetch = FetchType.EAGER)
    @JoinColumn (name = "roomId", nullable = true)
    @XmlTransient
    private Room room;
    
    @ManyToOne (optional = false, fetch = FetchType.EAGER)
    @JoinColumn (name = "reservationId", nullable = false)
    @NotNull
    @XmlTransient
    private Reservation reservation;
    
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "exceptionId", unique = true)
    @XmlTransient
    private AllocationException exception;

    public RoomAllocation() {
        this.isException = false;
    }

    public RoomAllocation(Date allocationDate) {
        this();
        
        this.allocationDate = allocationDate;
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

    public boolean isIsException() {
        return isException;
    }

    public void setIsException(boolean isException) {
        this.isException = isException;
    }

    public AllocationException getException() {
        return exception;
    }

    public void setException(AllocationException exception) {
        this.exception = exception;
    }
    
}
