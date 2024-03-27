package net.amentum.security.views;

import org.hibernate.validator.constraints.NotEmpty;

public class ChangePasswordRequestView {

    @NotEmpty(message="Debe proporcionar el nombre de usuario")
    private String username;
    @NotEmpty(message="Ingrese la contraseña anterior")
    private String password;
    @NotEmpty(message="Ingrese la contraseña anterior")
    private String oldPassword;
    @NotEmpty(message="Ingresa la nueva contraseña")
    private String newPassword;
    @NotEmpty(message="Ingresa la nueva contraseña.")
    private String newPassword2;
    @NotEmpty(message="Ingresa la nueva contraseña,")
    private String newPassword3;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

    public String getNewPassword3() {
        return newPassword3;
    }

    public void setNewPassword3(String newPassword3) {
        this.newPassword3 = newPassword3;
    }

    @Override
    public String toString() {
        return "ChangePasswordRequestView{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", newPassword2='" + newPassword2 + '\'' +
                ", newPassword3='" + newPassword3 + '\'' +
                '}';
    }
}
