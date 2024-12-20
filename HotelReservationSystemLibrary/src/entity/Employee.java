package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumerations.EmployeeRoleEnum;
import util.exceptions.EmployeeAddExceptionException;

@Entity
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    
    @Column (length = 64, nullable = false)
    @NotNull
    private String username;
    
    @Column (length = 20, nullable = false)
    @Size(min = 8, max = 20)
    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EmployeeRoleEnum UserRole;
    
    @Column (length = 64, nullable = false, unique = true)
    @Email
    @NotNull
    private String email;
    
    public Employee() {
    }

    public Employee(String name, String password, EmployeeRoleEnum role, String email) {
        this();
        
        this.username = name;
        this.password = password;
        this.UserRole = role;
        this.email = email;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeId != null ? employeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the employeeId fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Employee[ id=" + employeeId + " ]";
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EmployeeRoleEnum getUserRole() {
        return UserRole;
    }

    public void setUserRole(EmployeeRoleEnum UserRole) {
        this.UserRole = UserRole;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
