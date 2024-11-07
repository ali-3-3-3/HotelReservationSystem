package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import util.enumerations.RoomStatusEnum;

@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @NotNull
    private Date reservationDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    @NotNull
    private Date checkInDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    @NotNull
    private Date checkOutDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RoomStatusEnum status;

    @ManyToOne (optional = false)
    @JoinColumn (name = "guestId", nullable = false)
    @NotNull
    private Guest guest;

    @ManyToOne (optional = true)
    @JoinColumn(name = "partnerId", nullable = true)
    private Partner partner;

    @ManyToOne (optional = true)
    @JoinColumn(name = "roomTypeId", nullable = true)
    private RoomType roomType;
   
    @OneToMany(mappedBy = "reservation")
    private List<StayDetails> stayDetails;
    
    @OneToMany(mappedBy = "reservation")
    private RoomAllocation roomAllocation;
    
    public Reservation() {
    }

    public Reservation(Long reservationId, Date reservationDate, Date checkInDate, Date checkOutDate, RoomStatusEnum status, Guest customer, Partner partner, RoomType roomType, List<StayDetails> stayDetails) {
        this.reservationId = reservationId;
        this.reservationDate = reservationDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
        this.guest = customer;
        this.partner = partner;
        this.roomType = roomType;
        this.stayDetails = stayDetails;
    }

    public Reservation(Long reservationId, Date reservationDate, Date checkInDate, Date checkOutDate, RoomStatusEnum status, Guest customer, RoomType roomType, List<StayDetails> stayDetails) {
        this.reservationId = reservationId;
        this.reservationDate = reservationDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
        this.guest = customer;
        this.roomType = roomType;
        this.stayDetails = stayDetails;
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

    public RoomStatusEnum getStatus() {
        return status;
    }

    public void setStatus(RoomStatusEnum status) {
        this.status = status;
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
}