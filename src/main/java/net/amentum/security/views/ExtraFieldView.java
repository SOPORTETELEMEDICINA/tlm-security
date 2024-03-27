package net.amentum.security.views;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

public class ExtraFieldView implements Serializable{

    private Long extraFieldId;
    @NotEmpty(message = "Debe ingresar un ideintificador al campo")
    private String key;
    private String legend;
    private Boolean active = Boolean.TRUE;
    @NotEmpty(message = "Debe agregar el tipo de campo que se va a crear")
    private String fieldType;
    private String fieldValidation;
    private Date createdDate = new Date();
    private String validationMessage;
    @NotNull(message = "Debe seleccionar a que perfil le pertenece el campo")
    private Long profileId;
    private String profileName;

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

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    @Override
    public String toString() {
        return "ExtraFieldView{" +
                "extraFieldId=" + extraFieldId +
                ", key='" + key + '\'' +
                ", legend='" + legend + '\'' +
                ", active=" + active +
                ", fieldType='" + fieldType + '\'' +
                ", fieldValidation='" + fieldValidation + '\'' +
                ", createdDate=" + createdDate +
                ", validationMessage='" + validationMessage + '\'' +
                ", profileId=" + profileId +
                ", profileName='" + profileName + '\'' +
                '}';
    }
}
