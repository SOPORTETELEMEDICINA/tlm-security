package net.amentum.security.views;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

public class UserAlentaView implements Serializable {

    private static final long serialVersionUID = 1502536465883625299L;

    private Long idRelation;
    @NotEmpty(message = "Debes agregar un id de usuario")
    private String idUser;
    @NotEmpty(message = "Debe agregar un id de usuario alenta")
    private String idAlenta;

    public Long getIdRelation() {
        return idRelation;
    }

    public void setIdRelation(Long idRelation) {
        this.idRelation = idRelation;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdAlenta() {
        return idAlenta;
    }

    public void setIdAlenta(String idAlenta) {
        this.idAlenta = idAlenta;
    }

    @Override
    public String toString() {
        return "UserAlentaView{" +
                "idRelation=" + idRelation +
                ", idUser='" + idUser + '\'' +
                ", idAlenta='" + idAlenta + '\'' +
                '}';
    }
}
