package net.amentum.security.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by dev06 on 16/03/17.
 */
@Entity
@AssociationOverrides({
        @AssociationOverride(name = "userHasPermissionId.userApp",joinColumns =  @JoinColumn(name="id_user_app")),
        @AssociationOverride(name= "userHasPermissionId.modulePermission",joinColumns = @JoinColumn(name = "id_module_permission"))
})
public class UserHasPermission implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 3886881163852847639L;
	@EmbeddedId
    private UserHasPermissionId userHasPermissionId = new UserHasPermissionId();

    public UserHasPermissionId getUserHasPermissionId() {return userHasPermissionId;}

    public void setUserHasPermissionId(UserHasPermissionId userHasPermissionId) {this.userHasPermissionId = userHasPermissionId;}

    @Transient
    public UserApp getUserApp(){return getUserHasPermissionId().getUserApp();}

    public void setUserApp(UserApp userApp){getUserHasPermissionId().setUserApp(userApp);}

    @Transient
    public ModulePermission getModulePermission(){ return getUserHasPermissionId().getModulePermission(); }

    public void setModulePermission(ModulePermission modulePermission){ getUserHasPermissionId().setModulePermission(modulePermission);}

}
