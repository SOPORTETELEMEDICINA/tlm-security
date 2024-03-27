package net.amentum.security.model;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by dev06 on 16/03/17.
 */
@Embeddable
public class ProfilePermissionId implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2518863750878700045L;

	@ManyToOne
    private Profile profile;

    @ManyToOne
    private ModulePermission modulePermission;

    public Profile getProfile() {return profile;}

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public ModulePermission getModulePermission() {
        return modulePermission;
    }

    public void setModulePermission(ModulePermission modulePermission) {
        this.modulePermission = modulePermission;
    }
}
