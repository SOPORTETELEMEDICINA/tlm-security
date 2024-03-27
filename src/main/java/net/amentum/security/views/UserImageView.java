package net.amentum.security.views;

import java.util.Date;

public class UserImageView {
    private Long userImageId;
    private String encodedImageContent;
    private String contentType;
    private String imageName;
    private Date createdDate = new Date();
    private String encodedImageContentSmall;

    public Long getUserImageId() {
        return userImageId;
    }

    public void setUserImageId(Long userImageId) {
        this.userImageId = userImageId;
    }

    public String getEncodedImageContent() {
        return encodedImageContent;
    }

    public void setEncodedImageContent(String encodedImageContent) {
        this.encodedImageContent = encodedImageContent;
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

    public String getEncodedImageContentSmall() {
        return encodedImageContentSmall;
    }

    public void setEncodedImageContentSmall(String encodedImageContentSmall) {
        this.encodedImageContentSmall = encodedImageContentSmall;
    }

    @Override
    public String toString() {
        return "UserImageView{" +
                "userImageId=" + userImageId +
               // ", encodedImageContent='" + encodedImageContent + '\'' +
                ", contentType='" + contentType + '\'' +
                ", imageName='" + imageName + '\'' +
                ", createdDate=" + createdDate +
               // ", encodedImageContentSmall=" + encodedImageContentSmall +
                '}';
    }
}
