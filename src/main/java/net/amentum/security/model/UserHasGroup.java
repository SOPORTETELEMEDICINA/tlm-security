package net.amentum.security.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by dev06 on 16/03/17.
 */
@Entity
@Table(name="user_has_group")
@AssociationOverrides({
        @AssociationOverride(name = "pk.userApp",joinColumns =  @JoinColumn(name="id_user_app")),
        @AssociationOverride(name= "pk.group",joinColumns = @JoinColumn(name = "id_group"))
})
public class UserHasGroup implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 5718944504686168607L;

	@EmbeddedId
    private UserHasGroupId pk = new UserHasGroupId();

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    public UserHasGroupId getPk() {
        return pk;
    }

    public void setPk(UserHasGroupId pk) {
        this.pk = pk;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Transient
    public UserApp getUserApp(){
        return getPk().getUserApp();
    }

    public void setUserApp(UserApp userApp){
        getPk().setUserApp(userApp);
    }

    @Transient
    public Group getGroup(){
        return getPk().getGroup();
    }

    public void setGroup(Group group){
        getPk().setGroup(group);
    }

}
