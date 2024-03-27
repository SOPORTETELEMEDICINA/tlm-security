package net.amentum.security.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dev06 on 15/03/17.
 */
@Entity
@Table(name = "module_")
public class Module implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7546693372836582740L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_module")
    private Long moduleId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "module_name", unique = true)
    private String moduleName;

    private Long parentalId;

    @OneToMany(mappedBy = "module", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<ModulePermission> modulePermissions = new ArrayList<>();

    /**
     * Constructor using parameters
     */
    public Module(Date createdDate, String moduleName, Long parentalId) {
        this.createdDate = createdDate;
        this.moduleName = moduleName;
        this.parentalId = parentalId;
    }

    public Module() {
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Long getParentalId() {
        return parentalId;
    }

    public void setParentalId(Long parentalId) {
        this.parentalId = parentalId;
    }

    public List<ModulePermission> getModulePermissions() {
        return modulePermissions;
    }

    public void setModulePermissions(List<ModulePermission> modulePermissions) {
        this.modulePermissions = modulePermissions;
    }

    @Override
    public String toString() {
        return "Module{" +
                "moduleId=" + moduleId +
                ", createdDate=" + createdDate +
                ", moduleName='" + moduleName + '\'' +
                ", parentalId=" + parentalId +
                ", modulePermissions=" + modulePermissions +
                '}';
    }
}
