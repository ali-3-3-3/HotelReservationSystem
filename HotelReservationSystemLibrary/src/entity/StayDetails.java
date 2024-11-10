package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Entity
public class StayDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stayDetailsId;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @NotNull
    private Date stayDate;
    
    @Column(nullable = false, precision = 6, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 4, fraction = 2)
    private BigDecimal pricePerDay;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "reservationId", nullable = false)
    @NotNull
    private Reservation reservation;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "roomRateId", nullable = false)
    @NotNull
    private RoomRate roomRate;

    public StayDetails() {
    }

    public StayDetails(Date stayDate, BigDecimal pricePerDay, Reservation reservation, RoomRate roomRate) {
        this.stayDate = stayDate;
        this.pricePerDay = pricePerDay;
        this.reservation = reservation;
        this.roomRate = roomRate;
    }

    public Long getStayDetailsId() {
        return stayDetailsId;
    }

    public void setStayDetailsId(Long stayDetailsId) {
        this.stayDetailsId = stayDetailsId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stayDetailsId != null ? stayDetailsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the stayDetailsId fields are not set
        if (!(object instanceof StayDetails)) {
            return false;
        }
        StayDetails other = (StayDetails) object;
        if ((this.stayDetailsId == null && other.stayDetailsId != null) || (this.stayDetailsId != null && !this.stayDetailsId.equals(other.stayDetailsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.StayDetails[ id=" + stayDetailsId + " ]";
    }

    public Date getStayDate() {
        return stayDate;
    }

    public void setStayDate(Date stayDate) {
        this.stayDate = stayDate;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public RoomRate getRoomRate() {
        return roomRate;
    }

    public void setRoomRate(RoomRate roomRate) {
        this.roomRate = roomRate;
    }
    
}
