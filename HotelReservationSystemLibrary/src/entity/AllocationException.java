package entity;

import java.io.Serializable;
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
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import util.enumerations.ResolutionStatusEnum;

@Entity
public class AllocationException implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exceptionId;
    
    @Column (length = 128, nullable = false)
    @NotNull
    private String errorDescription;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private ResolutionStatusEnum resolutionStatus;

    @ManyToOne(optional = false, cascade = {}, fetch = FetchType.EAGER)
    @JoinColumn(name = "employeeId", nullable = false)
    @NotNull
    private Employee employee;
    
    @OneToOne(mappedBy = "exception", optional = false, fetch = FetchType.EAGER)
    @NotNull
    private RoomAllocation roomAllocation;

    public AllocationException() {
    }

    public AllocationException(String errorDescription, ResolutionStatusEnum resolutionStatus, Employee employee) {
        this.errorDescription = errorDescription;
        this.resolutionStatus = resolutionStatus;
        this.employee = employee;
    }

    public Long getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(Long exceptionId) {
        this.exceptionId = exceptionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (exceptionId != null ? exceptionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the exceptionId fields are not set
        if (!(object instanceof AllocationException)) {
            return false;
        }
        AllocationException other = (AllocationException) object;
        if ((this.exceptionId == null && other.exceptionId != null) || (this.exceptionId != null && !this.exceptionId.equals(other.exceptionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Exception[ id=" + exceptionId + " ]";
    }
    
    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public ResolutionStatusEnum getResolutionStatus() {
        return resolutionStatus;
    }

    public void setResolutionStatus(ResolutionStatusEnum resolutionStatus) {
        this.resolutionStatus = resolutionStatus;
    }
    
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    } 

    public RoomAllocation getRoomAllocation() {
        return roomAllocation;
    }

    public void setRoomAllocation(RoomAllocation roomAllocation) {
        this.roomAllocation = roomAllocation;
    }
}
