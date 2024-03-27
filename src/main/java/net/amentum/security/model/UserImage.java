package net.amentum.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

@Entity
@Table(name="user_image")
public class UserImage implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_image_id")
    private Long userImageId;

    @Column(name = "content_type")
    private String contentType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate = new Date();

    @Column(name = "image_content")
    private byte[] imageContent;

    @Column(name = "image_content_small")
    private byte[] imageContentSmall;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "user_app_id")
    private Long userAppId;

    @OneToOne(mappedBy = "userImage")
    @JsonIgnore
    private UserApp userApp;

    public Long getUserImageId() {
        return userImageId;
    }

    public void setUserImageId(Long userImageId) {
        this.userImageId = userImageId;
    }

    public byte[] getImageContent() {
        return imageContent;
    }

    public void setImageContent(byte[] imageContent) {
        this.imageContent = imageContent;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public UserApp getUserApp() {
        return userApp;
    }

    public void setUserApp(UserApp userApp) {
        this.userApp = userApp;
    }

    public byte[] getImageContentSmall() {
        return imageContentSmall;
    }

    public void setImageContentSmall(byte[] imageContentSmall) {
        this.imageContentSmall = imageContentSmall;
    }

    public Long getUserAppId() {
        return userAppId;
    }

    public void setUserAppId(Long userAppId) {
        this.userAppId = userAppId;
    }

    @Override
    public String toString() {
        return "UserImage{" +
                "userImageId=" + userImageId +
                ", imageContent=" + Arrays.toString(imageContent) +
                ", contentType='" + contentType + '\'' +
                ", imageName='" + imageName + '\'' +
                ", createdDate=" + createdDate +
                ", imageContentSmall=" + Arrays.toString(imageContentSmall) +
                ", userAppId=" + userAppId +
                '}';
    }
}
