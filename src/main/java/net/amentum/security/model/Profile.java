package net.amentum.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dev06 on 15/03/17.
 */
@Entity
@Table(name="profile")
public class Profile implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7761808675506679787L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_profile")
    private Long profileId;

    private String profileName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();

    private Boolean active = Boolean.TRUE;

    private Boolean allowEdition = Boolean.FALSE;

    @OneToMany(cascade = {CascadeType.ALL},fetch = FetchType.LAZY,mappedBy = "permissionId.profile")
    @JsonIgnore
    private List<ProfilePermission> permissionList = new ArrayList<>();

    public Profile(String profileName, Date createdDate, Boolean active, Boolean allowEdition) {
        this.profileName = profileName;
        this.createdDate = createdDate;
        this.active = active;
        this.allowEdition = allowEdition;
    }

    public List<ProfilePermission> getPermissionList() {return permissionList;}

    public void setPermissionList(List<ProfilePermission> permissionList) {this.permissionList = permissionList;}

    public Profile() {}

    public Long getProfileId() {return profileId;}

    public void setProfileId(Long profileId) {this.profileId = profileId;}

    public String getProfileName() {return profileName;}

    public void setProfileName(String profileName) {this.profileName = profileName;}

    public Date getCreatedDate() {return createdDate;}

    public void setCreatedDate(Date createdDate) {this.createdDate = createdDate;}

    public Boolean getActive() {return active;}

    public void setActive(Boolean active) {this.active = active;}

    public Boolean getAllowEdition() {return allowEdition;}

    public void setAllowEdition(Boolean allowEdition) {this.allowEdition = allowEdition;}

    @Override
    public String toString() {
        return "Profile{" + "profileId=" + profileId +", profileName='" + profileName + '\'' +", createdDate=" + createdDate +", active=" + active +", allowEdition=" + allowEdition +'}';
    }
}
