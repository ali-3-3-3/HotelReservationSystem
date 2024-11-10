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
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import util.exceptions.PartnerAddReservationException;

@Entity
public class Partner implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partnerId;
    
    @Column (length = 64, nullable = false, unique = true)
    @NotNull
    private String systemName;
    
    @Column (length = 64, nullable = false, unique = true)
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
    @NotNull
    private String email;
    
    @Column (length = 20, nullable = false)
    @Size(min = 8, max = 20)
    @NotNull
    private String password;
    
    @OneToMany(mappedBy="partner", fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public Partner() {
        this.reservations = new ArrayList<>();
    }

    public Partner(String systemName, String email, String password) {
        this();
        
        this.systemName = systemName;
        this.email = email;
        this.password = password;
    }
    
    public void addReservation(Reservation reservation) throws PartnerAddReservationException 
    {
        if(reservation != null && !this.getReservations().contains(reservation))
        {
            this.getReservations().add(reservation);
        }
        else
        {
            throw new PartnerAddReservationException("Reservation already added to partner");
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partnerId != null ? partnerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the partnerId fields are not set
        if (!(object instanceof Partner)) {
            return false;
        }
        Partner other = (Partner) object;
        if ((this.partnerId == null && other.partnerId != null) || (this.partnerId != null && !this.partnerId.equals(other.partnerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Partner[ id=" + partnerId + " ]";
    }
    
    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
