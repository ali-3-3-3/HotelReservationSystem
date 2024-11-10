package entity;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.persistence.TemporalType;
import util.exceptions.ReservationAddRoomAllocationException;
import util.exceptions.ReservationAddStayDetailException;

@Entity
public class Reservation implements Serializable {
    
    public static final LocalTime STANDARD_CHECK_IN_TIME = LocalTime.of(14, 0);  // 2 PM
    public static final LocalTime STANDARD_CHECK_OUT_TIME = LocalTime.of(12, 0); // 12 Noon

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date reservationDate;
    
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date checkInDate;
    
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date checkOutDate;
    
    @NotNull
    private LocalTime checkInTime;
    
    @NotNull
    private LocalTime checkOutTime;
    
    @NotNull
    private boolean hasCheckedIn;
    
    @NotNull
    private boolean hasCheckedOut;

    @ManyToOne (optional = false, fetch = FetchType.EAGER)
    @JoinColumn (name = "guestId", nullable = false)
    @NotNull
    private Guest guest;

    @ManyToOne (optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "partnerId", nullable = true)
    private Partner partner;

    @ManyToOne (optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "roomTypeId", nullable = false)
    @NotNull
    private RoomType roomType;
   
    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
    private List<StayDetails> stayDetails;
    
    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
    private List<RoomAllocation> roomAllocations;
    
    public Reservation() {
        this.hasCheckedIn = false;
        this.hasCheckedOut = false;
        this.stayDetails = new ArrayList<>();
        this.roomAllocations = new ArrayList<>();
    }
    
    public Reservation(Date reservationDate, Date checkInDate, Date checkOutDate) {
        this();
        
        this.reservationDate = reservationDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.checkInTime = STANDARD_CHECK_IN_TIME;  // Default check-in time is 2 PM
        this.checkOutTime = STANDARD_CHECK_OUT_TIME;  // Default check-out time is 12 Noon
    }
    
    public void addStayDetails(StayDetails stayDetail) throws ReservationAddStayDetailException {
        if(stayDetail != null && !this.getStayDetails().contains(stayDetail))
        {
            this.getStayDetails().add(stayDetail);
        }
        else
        {
            throw new ReservationAddStayDetailException("StayDetail already added to reservation");
        }
    }
    
    public void addRoomAllocation(RoomAllocation roomAllocation) throws ReservationAddRoomAllocationException {
        if(roomAllocation != null && !this.getRoomAllocations().contains(roomAllocation))
        {
            this.getRoomAllocations().add(roomAllocation);
        }
        else
        {
            throw new ReservationAddRoomAllocationException("Room Allocation already added to reservation");
        }
    }
    
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationId fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reservation[ id=" + reservationId + " ]";
    }
    
    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public List<StayDetails> getStayDetails() {
        return stayDetails;
    }

    public void setStayDetails(List<StayDetails> stayDetails) {
        this.stayDetails = stayDetails;
    }

    public List<RoomAllocation> getRoomAllocations() {
        return roomAllocations;
    }

    public void setRoomAllocations(List<RoomAllocation> roomAllocations) {
        this.roomAllocations = roomAllocations;
    }

    public LocalTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public boolean isHasCheckedIn() {
        return hasCheckedIn;
    }

    public void setHasCheckedIn(boolean hasCheckedIn) {
        this.hasCheckedIn = hasCheckedIn;
    }

    public boolean isHasCheckedOut() {
        return hasCheckedOut;
    }

    public void setHasCheckedOut(boolean hasCheckedOut) {
        this.hasCheckedOut = hasCheckedOut;
    }
}