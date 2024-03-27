package net.amentum.security.views;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author dev06
 */
public class ProfileView implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8797605049349867462L;
	private Long profileId;
    @NotEmpty(message = "Agreggue el nombre del perfil")
    private String profileName;
    private Date createdDate = new Date();
    @NotEmpty(message = "Agregue al menos un permiso")
    private List<PermissionView> profilePermissions;
    private Boolean active = Boolean.TRUE;
    private Boolean allowEdition = Boolean.TRUE;

    private List<NodeTree> tree;

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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<PermissionView> getProfilePermissions() {
        return profilePermissions;
    }

    public void setProfilePermissions(List<PermissionView> profilePermissions) {
        this.profilePermissions = profilePermissions;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getAllowEdition() {
        return allowEdition;
    }

    public void setAllowEdition(Boolean allowEdition) {
        this.allowEdition = allowEdition;
    }

    public List<NodeTree> getTree() {
        return tree;
    }

    public void setTree(List<NodeTree> tree) {
        this.tree = tree;
    }

    @Override
    public String toString() {
        return "ProfileView{" +
                "profileId=" + profileId +
                ", profileName='" + profileName + '\'' +
                ", createdDate=" + createdDate +
                ", profilePermissions=" + profilePermissions +
                ", active=" + active +
                ", allowEdition=" + allowEdition +
                ", tree=" + tree +
                '}';
    }
}
