package net.amentum.security.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table
public class RecoverPasswordRequest implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recoverPasswordRequestId;
    @Column(name="username")
    @NotEmpty
    private String username;
    @NotEmpty
    private String hash;
    private Date createdDate;
    @Column(name="active")
    private Boolean active;
    @Column(name="time_before_expire")
    private Date expireDate;

    public Long getRecoverPasswordRequestId() {
        return recoverPasswordRequestId;
    }

    public void setRecoverPasswordRequestId(Long recoverPasswordRequestId) {
        this.recoverPasswordRequestId = recoverPasswordRequestId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public String toString() {
        return "RecoverPasswordRequest{" +
                "recoverPasswordRequestId=" + recoverPasswordRequestId +
                ", username='" + username + '\'' +
                ", hash='" + hash + '\'' +
                ", createdDate=" + createdDate +
                ", active=" + active +
                ", expireDate=" + expireDate +
                '}';
    }
}
