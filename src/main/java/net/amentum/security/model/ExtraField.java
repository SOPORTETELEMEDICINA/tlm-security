package net.amentum.security.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table
public class ExtraField implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long extraFieldId;

    @Column(name = "key_")
    private String key;
    @Column(name = "legend_")
    private String legend;
    private Boolean active = Boolean.TRUE;
    private String fieldType;
    private String fieldValidation;
    private Date createdDate = new Date();
    private String validationMessage;
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_profile")
    private Profile profile;

    public Long getExtraFieldId() {
        return extraFieldId;
    }

    public void setExtraFieldId(Long extraFieldId) {
        this.extraFieldId = extraFieldId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldValidation() {
        return fieldValidation;
    }

    public void setFieldValidation(String fieldValidation) {
        this.fieldValidation = fieldValidation;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "ExtraField{" +
                "extraFieldId=" + extraFieldId +
                ", key='" + key + '\'' +
                ", legend='" + legend + '\'' +
                ", active=" + active +
                ", fieldType='" + fieldType + '\'' +
                ", fieldValidation='" + fieldValidation + '\'' +
                ", createdDate=" + createdDate +
                ", validationMessage='" + validationMessage + '\'' +
                ", profile=" + profile +
                '}';
    }
}
