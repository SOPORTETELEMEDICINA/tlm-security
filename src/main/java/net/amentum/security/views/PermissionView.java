package net.amentum.security.views;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * Created by dev06 on 11/04/17.
 */
public class PermissionView implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5987094396907921890L;
	private Long permissionId;
    @NotEmpty(message = "Debe ingresar el nombre del permiso")
    private String namePermission;
    @NotEmpty(message = "Debe ingresar el codigo del permiso")
    private String codePermission;

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getNamePermission() {
        return namePermission;
    }

    public void setNamePermission(String namePermission) {
        this.namePermission = namePermission;
    }

    public String getCodePermission() {
        return codePermission;
    }

    public void setCodePermission(String codePermission) {
        this.codePermission = codePermission;
    }

    @Override
    public String toString() {
        return "PermissionView{" +
                "permissionId=" + permissionId +
                ", namePermission='" + namePermission + '\'' +
                ", codePermission='" + codePermission + '\'' +
                '}';
    }
}
