package net.amentum.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author marellano
 */
@Entity
@Table(name = "user_app")
public class UserApp  implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8012024157105489411L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user_app")
    private Long userAppId;

    @Column(unique = true, nullable = false)
    @NotEmpty(message = "Debe agregar un nombre de usuario")
    private String username;

    private String email;

    private String password;

    @Column(name = "password2")
    private String password2;

    @Column(name = "password3")
    private String password3;

    @NotEmpty(message = "Debe agregar el número de teléfono")
    private String telefono;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();

    @NotEmpty(message = "Debe ingresar el nombre del usuario")
    private String name;

    @Column(name = "curp", unique = true)
    private String curp;

    @Enumerated(EnumType.STRING)
    private RowStatus status;

    private Long customerId;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "userApp", fetch = FetchType.LAZY)
    private List<UserExtraInfo> infoList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH})
    private Profile profile;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH})
    private WorkShift workShift;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "pk.userApp",cascade = {CascadeType.ALL})
    private List<UserHasGroup> groupList= new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "userHasPermissionId.userApp",cascade = {CascadeType.ALL})
    private List<UserHasPermission> permissionList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_user_app", referencedColumnName = "user_app_id")
    private UserImage userImage;

    private String connectionStatus;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "pk.userId")
    @JsonIgnore
    private List<UserHasBoss> userAppList=new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "pk.boss",cascade = {CascadeType.ALL} )
    @JsonIgnore
    private List<UserHasBoss> userAppBossList=new ArrayList<>();

    ////////////////////////////////////////////// catalogo tipo usuario
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH})
    @JoinColumn(name = "tipo_usuario_id", referencedColumnName = "id_tipo_usuario")
    private TipoUsuario tipoUsuario;
    ////////////////////////////////////////////// catalogo tipo usuario

    public UserApp(String username, String email, String password, Date createdDate, String name, RowStatus status, Long customerId, List<UserExtraInfo> infoList, Profile profile) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdDate = createdDate;
        this.name = name;
        this.status = status;
        this.customerId = customerId;
        this.infoList = infoList;
        this.profile = profile;
    }

    public UserApp() {}

    public Long getUserAppId() {return userAppId;}

    public void setUserAppId(Long userAppId) {this.userAppId = userAppId;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public Date getCreatedDate() {return createdDate;}

    public void setCreatedDate(Date createdDate) {this.createdDate = createdDate;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public RowStatus getStatus() {return status;}

    public void setStatus(RowStatus status) {this.status = status;}

    public Long getCustomerId() {return customerId;}

    public void setCustomerId(Long customerId) {this.customerId = customerId;}

    public List<UserExtraInfo> getInfoList() {return infoList;}

    public void setInfoList(List<UserExtraInfo> infoList) {this.infoList = infoList;}

    public Profile getProfile() {return profile;}

    public void setProfile(Profile profile) {this.profile = profile;}

    public List<UserHasGroup> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<UserHasGroup> groupList) {
        this.groupList = groupList;
    }

    public List<UserHasPermission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<UserHasPermission> permissionList) {
        this.permissionList = permissionList;
    }

    public UserImage getUserImage() {
        return userImage;
    }

    public void setUserImage(UserImage userImage) {
        this.userImage = userImage;
    }

    public WorkShift getWorkShift() {
        return workShift;
    }

    public void setWorkShift(WorkShift workShift) {
        this.workShift = workShift;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public List<UserHasBoss> getUserAppList() {
        return userAppList;
    }

    public void setUserAppList(List<UserHasBoss> userAppList) {
        this.userAppList = userAppList;
    }

    public List<UserHasBoss> getUserAppBossList() {
        return userAppBossList;
    }

    public void setUserAppBossList(List<UserHasBoss> userAppBossList) {
        this.userAppBossList = userAppBossList;
    }

    ////////////////////////////////////////////// catalogo tipo usuario
    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
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
        return "UserApp{" +
                "userAppId=" + userAppId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", password2='" + password2 + '\'' +
                ", password3='" + password3 + '\'' +
                ", telefono='" + telefono + '\'' +
                ", createdDate=" + createdDate +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", customerId=" + customerId +
                ", infoList=" + infoList +
                ", profile=" + profile +
                ", workShift=" + workShift +
                ", groupList=" + groupList +
                ", permissionList=" + permissionList +
                ", userImage=" + userImage +
                ", connectionStatus='" + connectionStatus + '\'' +
                ", userAppList=" + userAppList +
                ", userAppBossList=" + userAppBossList +
                ", tipoUsuario=" + tipoUsuario +
                '}';
    }
}