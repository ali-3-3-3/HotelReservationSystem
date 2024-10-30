package entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AllocationProcess implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allocationId;
    
    private LocalDate allocationDate;
    private String status;

    public AllocationProcess() {
    }

    public AllocationProcess(Long allocationID, String status) {
        this.allocationId = allocationID;
        this.status = status;
    }

    public AllocationProcess(Long allocationID, LocalDate allocationDate, String status) {
        this.allocationId = allocationID;
        this.allocationDate = allocationDate;
        this.status = status;
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
        if (!(object instanceof AllocationProcess)) {
            return false;
        }
        AllocationProcess other = (AllocationProcess) object;
        if ((this.allocationId == null && other.allocationId != null) || (this.allocationId != null && !this.allocationId.equals(other.allocationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AllocationProcess[ id=" + allocationId + " ]";
    }
    
    public LocalDate getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(LocalDate allocationDate) {
        this.allocationDate = allocationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
