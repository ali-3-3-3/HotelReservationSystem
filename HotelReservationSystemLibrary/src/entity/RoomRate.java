package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;
import util.enumerations.RateTypeEnum;

@Entity
public class RoomRate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomRateId;
    
    @Column (length = 64, nullable = false)
    @NotNull
    private String name;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private RateTypeEnum rateType;

    @Temporal(javax.persistence.TemporalType.DATE)
    @NotNull
    private Date startDate;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @NotNull
    private Date endDate;
    
    @Column(nullable = false)
    @NotNull
    @Digits(integer = 3, fraction = 0)
    private int ratePerNight;

    @ManyToOne (optional = false, cascade = {}, fetch = FetchType.EAGER)
    @JoinColumn (name = "roomTypeId", nullable = false)
    @NotNull
    @XmlTransient
    private RoomType roomType;
    
    @ManyToMany (mappedBy = "roomRates", cascade = {}, fetch = FetchType.LAZY)
    private Set<Reservation> reservations;

    public RoomRate() {
        this.reservations = new HashSet<>();
    }

    public RoomRate(String name, RateTypeEnum rateType, int pricePerNight, Date startDate, Date endDate) {
        this.name = name;
        this.rateType = rateType;
        this.ratePerNight = pricePerNight;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getRoomRateId() {
        return roomRateId;
    }

    public void setRoomRateId(Long roomRateId) {
        this.roomRateId = roomRateId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomRateId != null ? roomRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomRateId fields are not set
        if (!(object instanceof RoomRate)) {
            return false;
        }
        RoomRate other = (RoomRate) object;
        if ((this.roomRateId == null && other.roomRateId != null) || (this.roomRateId != null && !this.roomRateId.equals(other.roomRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomRate[ id=" + roomRateId + " ]";
    }

    public RateTypeEnum getRateType() {
        return rateType;
    }

    public void setRateType(RateTypeEnum rateType) {
        this.rateType = rateType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getRatePerNight() {
        return ratePerNight;
    }

    public void setRatePerNight(int ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
}
