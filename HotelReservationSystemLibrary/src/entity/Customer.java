package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class Customer extends Guest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Column (length = 15, nullable = false, unique = true)
    @Pattern(regexp = "^[A-Za-z0-9_]{3,15}$", message = "Username must be 3-15 characters long and contain only letters, numbers, or underscores")
    @NotNull
    private String username;
    
    @Column (length = 20, nullable = false)
    @Size(min = 5, max = 20)
    @NotNull
    private String password;

    public Customer() {
        super();
    }

    public Customer(String name, String email, String phoneNumber, String username, String password) {
        super(name, email, phoneNumber);
        this.username = username;
        this.password = password;
    }
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
