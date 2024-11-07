package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Entity
public class Customer extends Guest implements Serializable {
    
    @Column (length = 32, nullable = false, unique = true)
    @NotNull
    private String username;
    
    @Column (length = 32, nullable = false)
    @NotNull
    private String password;

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
