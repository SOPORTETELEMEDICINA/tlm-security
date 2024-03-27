package net.amentum.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dev06 on 15/03/17.
 */
@Entity
public class UserExtraInfo {

    @Column(name="id_user_extra_info")
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userExtraInfoId;

    @Column(name="key_")
    @NotEmpty(message = "Agregue el nombre para el campo")
    private String key;

    @Column(name="value_")
    private String value;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(EnumType.STRING)
    private RowStatus status;

    @OneToOne(cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    @JoinColumn(name = "userAppId",nullable = false)
    @JsonIgnore
    private UserApp userApp;


    public UserExtraInfo(String key, String value, Date createdDate, Date modifiedDate, RowStatus status, UserApp userApp) {
        this.key = key;
        this.value = value;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.status = status;
        this.userApp = userApp;
    }

    public UserExtraInfo() {}

    public Long getUserExtraInfoId() {return userExtraInfoId;}

    public void setUserExtraInfoId(Long userExtraInfoId) {this.userExtraInfoId = userExtraInfoId;}

    public String getKey() {return key;}

    public void setKey(String key) {this.key = key;}

    public String getValue() {return value;}

    public void setValue(String value) {this.value = value;}

    public Date getCreatedDate() {return createdDate;}

    public void setCreatedDate(Date createdDate) {this.createdDate = createdDate;}

    public Date getModifiedDate() {return modifiedDate;}

    public void setModifiedDate(Date modifiedDate) {this.modifiedDate = modifiedDate;}

    public RowStatus getStatus() {return status;}

    public void setStatus(RowStatus status) {this.status = status;}

    public UserApp getUserApp() {return userApp;}

    public void setUserApp(UserApp userApp) {this.userApp = userApp;}

    @Override
    public String toString() {
        return "UserExtraInfo{" +
                "userExtraInfoId=" + userExtraInfoId +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", status=" + status +
                '}';
    }
}
