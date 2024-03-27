package net.amentum.security.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by dev06 on 16/03/17.
 */
@Entity
@AssociationOverrides({
        @AssociationOverride(name = "permissionId.profile",joinColumns =  @JoinColumn(name="id_profile")),
        @AssociationOverride(name= "permissionId.modulePermission",joinColumns = @JoinColumn(name = "id_module_permission"))
})
public class ProfilePermission implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 2582696529429795135L;

	@EmbeddedId
    private ProfilePermissionId permissionId = new ProfilePermissionId();

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    public ProfilePermissionId getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(ProfilePermissionId permissionId) {
        this.permissionId = permissionId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Transient
    public Profile getProfile(){
        return getPermissionId().getProfile();
    }

    public void setProfile(Profile profile){
        getPermissionId().setProfile(profile);
    }

    @Transient
    public ModulePermission getModulePermission(){
        return getPermissionId().getModulePermission();
    }

    public void setModulePermission(ModulePermission modulePermission){
        getPermissionId().setModulePermission(modulePermission);
    }
}
