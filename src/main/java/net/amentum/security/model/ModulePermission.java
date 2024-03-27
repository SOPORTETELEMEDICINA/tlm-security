package net.amentum.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dev06 on 15/03/17.
 */
@Entity
public class ModulePermission implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -8063901372425105583L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_module_permission_id")
    private Long modulePermissionId;

    @NotEmpty(message = "Agregue una descripción al permiso")
    private String namePermission;

    @Column(unique = true)
    @NotEmpty(message = "Agregue el código para el permiso")
    private String codeModulePermission;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
    @JoinColumn(name = "moduleId",nullable = false)
    @JsonIgnore
    private Module module;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "permissionId.modulePermission",cascade = {CascadeType.ALL})
    @JsonIgnore
    private List<ProfilePermission> permissionList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "userHasPermissionId.modulePermission",cascade = {CascadeType.ALL})
    @JsonIgnore
    private List<UserHasPermission> userHasPermissionList = new ArrayList<>();

    public Long getModulePermissionId() {return modulePermissionId;}

    public void setModulePermissionId(Long modulePermissionId) {this.modulePermissionId = modulePermissionId;}

    public String getNamePermission() {return namePermission;}

    public void setNamePermission(String namePermission) {this.namePermission = namePermission;}

    public String getCodeModulePermission() {return codeModulePermission;}

    public void setCodeModulePermission(String codeModulePermission) {this.codeModulePermission = codeModulePermission;}

    public Module getModule() {return module;}

    public void setModule(Module module) {this.module = module;}

    public List<ProfilePermission> getPermissionList() {return permissionList;}

    public void setPermissionList(List<ProfilePermission> permissionList) {this.permissionList = permissionList;}

    @Override
    public String toString() {
        return "ModulePermission{" +
                "modulePermissionId=" + modulePermissionId +
                ", namePermission='" + namePermission + '\'' +
                ", codeModulePermission='" + codeModulePermission + '\'' +
                '}';
    }
}
