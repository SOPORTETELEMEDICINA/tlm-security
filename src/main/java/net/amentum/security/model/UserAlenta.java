package net.amentum.security.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users_alenta")
public class UserAlenta implements Serializable {

    private static final long serialVersionUID = 8012024157105489411L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_relation")
    private Long idRelation;
    private String idUser;
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
        return "UserAlenta{" +
                "idRelation=" + idRelation +
                ", idUser='" + idUser + '\'' +
                ", idAlenta='" + idAlenta + '\'' +
                '}';
    }
}
