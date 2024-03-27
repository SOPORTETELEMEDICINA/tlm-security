package net.amentum.security.views;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dev06 on 27/04/17.
 */
public class UserAppPageView implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3103560388405536704L;
	private Long idUserApp;
    private String username;
    private String email;
    private String name;
    private String profileName;
    private ArrayList<GroupView> groups;

    public UserAppPageView(){

    }

    public UserAppPageView(Long idUserApp, String username, String email, String name, String profileName) {
        this.idUserApp = idUserApp;
        this.username = username;
        this.email = email;
        this.name = name;
        this.profileName = profileName;
        this.groups = null;
    }

    public UserAppPageView(Long idUserApp, String username, String email, String name, String profileName, ArrayList<GroupView> groups) {
        this.idUserApp = idUserApp;
        this.username = username;
        this.email = email;
        this.name = name;
        this.profileName = profileName;
        this.groups = groups;
    }

    public Long getIdUserApp() {
        return idUserApp;
    }

    public void setIdUserApp(Long idUserApp) {
        this.idUserApp = idUserApp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public ArrayList<GroupView> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<GroupView> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "UserAppPageView{" +
                "idUserApp=" + idUserApp +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", profileName='" + profileName + '\'' +
                ", groups=" + groups +
                '}';
    }
}
