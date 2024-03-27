package net.amentum.security.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="user_has_boss")
@AssociationOverrides({
        @AssociationOverride(name = "pk.boss",joinColumns =  @JoinColumn(name="id_user_boss")),
        @AssociationOverride(name= "pk.userId",joinColumns = @JoinColumn(name = "id_user"))
})
public class UserHasBoss implements Serializable {

    @EmbeddedId
    private UserHasBossId pk = new UserHasBossId();

    public UserHasBossId getPk() {
        return pk;
    }

    public void setPk(UserHasBossId pk) {
        this.pk = pk;
    }

    @Transient
    public UserApp getUserAppBoss(){
        return getPk().getBoss();
    }

    public void setUserAppBoss(UserApp userApp){
        getPk().setBoss(userApp);
    }

    @Transient
    public UserApp getUserApp(){
        return getPk().getUserApp();
    }

    public void setUserApp(UserApp userApp){
        getPk().setUserApp(userApp);
    }
}
