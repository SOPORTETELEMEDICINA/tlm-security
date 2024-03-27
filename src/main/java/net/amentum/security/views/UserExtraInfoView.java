package net.amentum.security.views;


import org.hibernate.validator.constraints.NotEmpty;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by marellano on 25/04/17.
 */
public class UserExtraInfoView implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4359963927786622782L;
	private Long userExtraInfoId;
    @NotEmpty(message = "Agregue el key")
    private String key;
    @NotEmpty(message = "Agregue el value")
    private String value;
    private Date createdDate;
    private Date modifiedDate;
    private String status;


    public Long getUserExtraInfoId() {
        return userExtraInfoId;
    }

    public void setUserExtraInfoId(Long userExtraInfoId) {
        this.userExtraInfoId = userExtraInfoId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserExtraInfoView{" +
                "userExtraInfoId=" + userExtraInfoId +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", status=" + status +
                '}';
    }
}
