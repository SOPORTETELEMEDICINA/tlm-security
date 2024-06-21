package net.amentum.security.views;

import java.io.Serializable;

public class UserSignatureView implements Serializable {
    private String signatureName;
    private String imageContent;
    private String imageContentType;

    public String getSignatureName() {
        return signatureName;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public String getImageContent() {
        return imageContent;
    }
}
