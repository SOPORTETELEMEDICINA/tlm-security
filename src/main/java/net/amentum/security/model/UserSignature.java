package net.amentum.security.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="user_signature")
public class UserSignature implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_signature_id")
    private Long userSignatureId;

    @Column(name = "user_app_id")
    private Long userAppId;

    @Column(name = "signature_name")
    private String signatureName;

    @Column(name = "image_content")
    private byte[] imageContent;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate = new Date();

    public Long getUserAppId() { return this.getUserAppId(); }

    public String getSignatureName() { return this.signatureName; }

    public String getImageContentType() {
        return imageContentType;
    }

    public byte[] getImageContent() { return this.imageContent; }
}
