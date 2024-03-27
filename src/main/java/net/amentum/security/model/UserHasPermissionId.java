package net.amentum.security.model;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by dev06 on 16/03/17.
 */
@Embeddable
public class UserHasPermissionId implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 3497518890608589583L;

	@ManyToOne
    private UserApp userApp;

    @ManyToOne
    private ModulePermission modulePermission;

    public UserApp getUserApp() {return userApp;}

    public void setUserApp(UserApp userApp) {this.userApp = userApp;}

    public ModulePermission getModulePermission() {return modulePermission;}

    public void setModulePermission(ModulePermission modulePermission) {this.modulePermission = modulePermission;}
}
