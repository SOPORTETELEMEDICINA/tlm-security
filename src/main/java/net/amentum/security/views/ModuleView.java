package net.amentum.security.views;

import net.amentum.security.views.PermissionView;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dev06 on 11/04/17.
 */
public class ModuleView implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8664058450514272123L;
	private Long moduleId;
    private Long parentalId;
    @NotEmpty(message = "Agregue el nombre del módulo")
    private String moduleName;
    private Date createdDate;

    @NotEmpty(message = "Agregue al menos un permiso al módulo")
    private List<PermissionView> modulePermissions = new ArrayList<>();

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getParentalId() {
        return parentalId;
    }

    public void setParentalId(Long parentalId) {
        this.parentalId = parentalId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<PermissionView> getModulePermissions() {
        return modulePermissions;
    }

    public void setModulePermissions(List<PermissionView> modulePermissions) {
        this.modulePermissions = modulePermissions;
    }

    @Override
    public String toString() {
        return "ModuleView{" +
                "moduleId=" + moduleId +
                ", parentalId=" + parentalId +
                ", moduleName='" + moduleName + '\'' +
                ", createdDate=" + createdDate +
                ", modulePermissions=" + modulePermissions +
                '}';
    }
}
