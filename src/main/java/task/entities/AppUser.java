package task.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

// @Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AppUser implements Serializable {
    @Id
    @GeneratedValue
    private Long                id;
    private String              password;
    private String              username;
    private String              email;

    @ManyToMany( fetch = FetchType.EAGER )
    private Collection<AppRole> roles = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonSetter
    public void setPassword( String password ) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public Collection<AppRole> getRoles() {
        return roles;
    }

    public void setRoles( Collection<AppRole> roles ) {
        this.roles = roles;
    }

}
