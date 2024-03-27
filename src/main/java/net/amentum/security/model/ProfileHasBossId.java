package net.amentum.security.model;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class ProfileHasBossId implements Serializable {

    @ManyToOne
    private Profile boss;

    @ManyToOne
    private Profile profileId;

    public Profile getBoss() {
            return boss;
        }

    public void setBoss(Profile boss) {
            this.boss = boss;
        }

    public Profile getProfile() {
            return profileId;
        }

    public void setProfile(Profile profile) {
            this.profileId = profile;
        }
}
