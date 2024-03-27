package net.amentum.security.model;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by dev06 on 16/03/17.
 */
@Embeddable
public class UserHasGroupId implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 5217278815472382727L;

	@ManyToOne
    private Group group;

    @ManyToOne
    private UserApp userApp;


    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public UserApp getUserApp() {
        return userApp;
    }

    public void setUserApp(UserApp userApp) {
        this.userApp = userApp;
    }

}
