package net.amentum.security.model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="profile_has_boss")
@AssociationOverrides({
        @AssociationOverride(name = "pk.boss",joinColumns =  @JoinColumn(name="id_profile_boss")),
        @AssociationOverride(name= "pk.profileId",joinColumns = @JoinColumn(name = "id_profile"))
})
public class ProfileHasBoss implements Serializable {


    @EmbeddedId
    private ProfileHasBossId pk = new ProfileHasBossId();


    public ProfileHasBossId getPk() {
        return pk;
    }

    public void setPk(ProfileHasBossId pk) {
        this.pk = pk;
    }


    @Transient
    public Profile getProfileBoss(){
        return getPk().getBoss();
    }

    public void setProfileBoss(Profile profile){
        getPk().setBoss(profile);
    }

    @Transient
    public Profile getProfile(){
        return getPk().getProfile();
    }

    public void setProfile(Profile profile){
        getPk().setProfile(profile);
    }

}
