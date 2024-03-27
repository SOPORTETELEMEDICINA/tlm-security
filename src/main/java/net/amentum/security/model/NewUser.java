package net.amentum.security.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "new_user_request")
public class NewUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newUserRequestId;
    private Boolean active;
    private Date createdDate;
    private Date timeBeforeExpire;
    @NotEmpty
    private String hash;
    @NotEmpty
    private String username;
    private Long idGroup;
    @NotEmpty
    private String idUsuario;
    @NotEmpty
    private String name;

    public Long getNewUserRequestId() {
        return newUserRequestId;
    }

    public void setNewUserRequestId(Long newUserRequestId) {
        this.newUserRequestId = newUserRequestId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getTimeBeforeExpire() {
        return timeBeforeExpire;
    }

    public void setTimeBeforeExpire(Date timeBeforeExpire) {
        this.timeBeforeExpire = timeBeforeExpire;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(Long idGroup) {
        this.idGroup = idGroup;
    }

    public String getidUsuario() {
        return idUsuario;
    }

    public void setidUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NewUser{" +
                "newUserRequestId=" + newUserRequestId +
                ", active=" + active +
                ", createdDate=" + createdDate +
                ", timeBeforeExpire=" + timeBeforeExpire +
                ", hash='" + hash + '\'' +
                ", username='" + username + '\'' +
                ", idGroup=" + idGroup +
                ", idUsuario='" + idUsuario + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
