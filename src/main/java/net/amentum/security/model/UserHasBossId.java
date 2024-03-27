package net.amentum.security.model;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class UserHasBossId implements Serializable {

    @ManyToOne
    private UserApp boss;

    @ManyToOne
    private UserApp userId;


    public UserApp getBoss() {
        return boss;
    }

    public void setBoss(UserApp boss) {
        this.boss = boss;
    }

    public UserApp getUserApp() {
        return userId;
    }

    public void setUserApp(UserApp userApp) {
        this.userId = userApp;
    }
}
