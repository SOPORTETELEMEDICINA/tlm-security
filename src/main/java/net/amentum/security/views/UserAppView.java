package net.amentum.security.views;


import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author marellano on 25/04/17.
 */
public class UserAppView implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1502536465883625299L;
	private Long idUserApp;
    @NotEmpty(message = "Debe agregar un username")
    private String username;
    private String email;
    private String curp;
    private String password;
    private String password2;
    private String password3;
    private String telefono;
    private Date createdDate;
    @NotEmpty(message = "Debe ingresar el nombre del usuario")
    private String name;
    private String status;
    private Long customerId;
    @Valid
    private List<UserExtraInfoView>  extraList =  new ArrayList<>();
   // @NotNull(message = "Debe ingresar el id del perfil")
    private Long profileId;
    private Long idWorkShift;
    private String nameWorkShift;

    @Valid
   // @NotEmpty(message = "Debe ingresar al menos un grupo")
    private List<Long> groupList = new ArrayList<>();

    @Valid
    //@NotEmpty(message = "Debe ingresar los perfiles")
    private List<Long> permissionsList = new ArrayList<>();

    private UserImageView imageProfile;

    private String connectionStatus;
    private String profileName;
    private List<GroupView> groups = new ArrayList<>();

    private List<Long> idUsersAssigned = new ArrayList<>();

    private List<UsersAssignedView> infoUsers = new ArrayList<>();

    ////////////////////////////////////////////// catalogo tipo usuario
    private Integer idTipoUsuario;
    private String descripcion;
   ////////////////////////////////////////////// catalogo tipo usuario

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

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<UserExtraInfoView> getExtraList() {
        return extraList;
    }

    public void setExtraList(List<UserExtraInfoView> extraList) {
        this.extraList = extraList;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public List<Long> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Long> groupList) {
        this.groupList = groupList;
    }

    public List<Long> getPermissionsList() {
        return permissionsList;
    }

    public void setPermissionsList(List<Long> permissionsList) {
        this.permissionsList = permissionsList;
    }

    public UserImageView getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(UserImageView imageProfile) {
        this.imageProfile = imageProfile;
    }

    public Long getIdWorkShift() {
        return idWorkShift;
    }

    public void setIdWorkShift(Long idWorkShift) {
        this.idWorkShift = idWorkShift;
    }

    public String getNameWorkShift() {
        return nameWorkShift;
    }

    public void setNameWorkShift(String nameWorkShift) {
        this.nameWorkShift = nameWorkShift;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public List<GroupView> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupView> groups) {
        this.groups = groups;
    }

    public List<Long> getIdUsersAssigned() {
        return idUsersAssigned;
    }

    public void setIdUsersAssigned(List<Long> idUsersAssigned) {
        this.idUsersAssigned = idUsersAssigned;
    }

    public List<UsersAssignedView> getInfoUsers() {
        return infoUsers;
    }

    public void setInfoUsers(List<UsersAssignedView> infoUsers) {
        this.infoUsers = infoUsers;
    }

   ////////////////////////////////////////////// catalogo tipo usuario
   public Integer getIdTipoUsuario() {
      return idTipoUsuario;
   }

   public void setIdTipoUsuario(Integer idTipoUsuario) {
      this.idTipoUsuario = idTipoUsuario;
   }

   public String getDescripcion() {
      return descripcion;
   }

   public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
   }
   ////////////////////////////////////////////// catalogo tipo usuario


    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getPassword3() {
        return password3;
    }

    public void setPassword3(String password3) {
        this.password3 = password3;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "UserAppView{" +
                "idUserApp=" + idUserApp +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", password2='" + password2 + '\'' +
                ", password3='" + password3 + '\'' +
                ", telefono='" + telefono + '\'' +
                ", createdDate=" + createdDate +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", customerId=" + customerId +
                ", extraList=" + extraList +
                ", profileId=" + profileId +
                ", idWorkShift=" + idWorkShift +
                ", nameWorkShift='" + nameWorkShift + '\'' +
                ", groupList=" + groupList +
                ", permissionsList=" + permissionsList +
                ", imageProfile=" + imageProfile +
                ", connectionStatus='" + connectionStatus + '\'' +
                ", profileName='" + profileName + '\'' +
                ", groups=" + groups +
                ", idUsersAssigned=" + idUsersAssigned +
                ", infoUsers=" + infoUsers +
                ", idTipoUsuario=" + idTipoUsuario +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
