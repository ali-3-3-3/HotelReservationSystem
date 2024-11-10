package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import util.enumerations.RateTypeEnum;

@Entity
public class RoomRate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomRateId;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private RateTypeEnum rateType;

    @Temporal(javax.persistence.TemporalType.DATE)
    @NotNull
    private Date startDate;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @NotNull
    private Date endDate;
    
    @Column(nullable = false, precision = 6, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 4, fraction = 2)
    private BigDecimal pricePerNight;

    @ManyToOne (optional = false, fetch = FetchType.EAGER)
    @JoinColumn (name = "roomTypeId", nullable = false)
    @NotNull
    private RoomType roomType;
    
    @OneToMany(mappedBy = "roomRate", fetch = FetchType.LAZY)
    private List<StayDetails> stayDetails;

    public RoomRate() {
        this.stayDetails = new ArrayList<>();
    }

    public RoomRate(RateTypeEnum rateType, Date startDate, Date endDate, BigDecimal pricePerNight, RoomType roomType) {
        this.rateType = rateType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pricePerNight = pricePerNight;
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

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
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
