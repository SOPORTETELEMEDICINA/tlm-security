package net.amentum.security.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author dev06
 */
@Entity
@Table
public class OauthClientDetails implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "ID de cliente requerido")
    private String clientId;
    private String resourceIds;
    @NotEmpty(message = "Agregue scope")
    private String scope;
    @NotEmpty(message = "Agregue autorizaciones,  password, authorization, refresh_token ...")
    private String authorizedGrantTypes;
    private String webServerRedirectUri = "/web";
    private String authorities;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    @Column(name = "additional_information")
    private String additionaInformation;
    @Column(name = "autoapprove")
    private String autoaprove = "";
    private String clientSecret;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public String getWebServerRedirectUri() {
        return webServerRedirectUri;
    }

    public void setWebServerRedirectUri(String webServerRedirectUri) {
        this.webServerRedirectUri = webServerRedirectUri;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public Integer getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public void setAccessTokenValidity(Integer accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    public Integer getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public void setRefreshTokenValidity(Integer refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public String getAdditionaInformation() {
        return additionaInformation;
    }

    public void setAdditionaInformation(String additionaInformation) {
        this.additionaInformation = additionaInformation;
    }

    public String getAutoaprove() {
        return autoaprove;
    }

    public void setAutoaprove(String autoaprove) {
        this.autoaprove = autoaprove;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @Override
    public String toString() {
        return "OauthClientDetails{" +
                "id=" + id +
                ", clientId='" + clientId + '\'' +
                ", resourceIds='" + resourceIds + '\'' +
                ", scope='" + scope + '\'' +
                ", authorizedGrantTypes='" + authorizedGrantTypes + '\'' +
                ", webServerRedirectUri='" + webServerRedirectUri + '\'' +
                ", authorities='" + authorities + '\'' +
                ", accessTokenValidity=" + accessTokenValidity +
                ", refreshTokenValidity=" + refreshTokenValidity +
                ", additionaInformation='" + additionaInformation + '\'' +
                ", autoaprove=" + autoaprove +
                ", clientSecret='" + clientSecret + '\'' +
                '}';
    }
}
