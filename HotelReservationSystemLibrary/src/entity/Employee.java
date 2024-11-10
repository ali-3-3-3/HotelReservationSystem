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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    private String name;
    
    @Column (length = 64, nullable = false)
    @NotNull
    private String position;
    
    @Column (length = 64, nullable = false, unique = true)
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
    @NotNull
    private String email;
    
    @Column (length = 20, nullable = false)
    @Size(min = 8, max = 20)
    @NotNull
    private String password;
    
    @Column (length = 8, nullable = false, unique = true)
    @NotNull
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EmployeeRoleEnum employeeRole;
    
    @OneToMany(mappedBy="employee", fetch = FetchType.LAZY)
    private List<AllocationException> exceptions;

    public Employee() {
        this.exceptions = new ArrayList<>();
    }

    public Employee(String name, String position, String email, String password, String phoneNumber, EmployeeRoleEnum role) {
        this();
        
        this.name = name;
        this.position = position;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.employeeRole = role;
    }
    
    public void addException(AllocationException exception) throws EmployeeAddExceptionException {
        if(exception != null && !this.getExceptions().contains(exception))
        {
            this.getExceptions().add(exception);
        }
        else
        {
            throw new EmployeeAddExceptionException("Exception already added to employee");
        }
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
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public EmployeeRoleEnum getEmployeeRole() {
        return employeeRole;
    }

    public void setEmployeeRole(EmployeeRoleEnum employeeRole) {
        this.employeeRole = employeeRole;
    }

    public List<AllocationException> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<AllocationException> exceptions) {
        this.exceptions = exceptions;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
