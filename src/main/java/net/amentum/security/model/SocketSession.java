package net.amentum.security.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table
public class SocketSession implements Serializable{

    @Id
    private String sessionId;
    private Boolean active;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    private String username;
    private String deviceId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "SocketSession{" +
                "sessionId='" + sessionId + '\'' +
                ", active=" + active +
                ", createdDate=" + createdDate +
                ", username='" + username + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
