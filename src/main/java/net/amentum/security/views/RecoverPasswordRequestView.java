package net.amentum.security.views;

import org.hibernate.validator.constraints.NotEmpty;

public class RecoverPasswordRequestView {

    @NotEmpty(message="Debe proporcionar el nombre de usuario")
    private String username;
    @NotEmpty(message="Ingrese la contraseña")
    private String password;
    @NotEmpty(message="Falta ingresar la contraseña")
    private String hash;

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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "RecoverPasswordRequestView{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }
}
