package net.amentum.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "tipo_usuario")
public class TipoUsuario implements Serializable {

   private static final long serialVersionUID = -410327547092639984L;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id_tipo_usuario")
   private Integer idTipoUsuario;

   private String descripcion;

   private Boolean disponible;

   private Boolean visible;

   public TipoUsuario() {
   }

   public TipoUsuario(String descripcion, Boolean disponible, Boolean visible) {
      this.descripcion = descripcion;
      this.disponible = disponible;
      this.visible = visible;
   }

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

   public Boolean getDisponible() {
      return disponible;
   }

   public void setDisponible(Boolean disponible) {
      this.disponible = disponible;
   }

   public Boolean getVisible() {
      return visible;
   }

   public void setVisible(Boolean visible) {
      this.visible = visible;
   }

   @Override
   public String toString() {
      return "TipoUsuario{" +
         "idTipoUsuario=" + idTipoUsuario +
         ", descripcion='" + descripcion + '\'' +
         ", disponible=" + disponible +
         ", visible=" + visible +
         '}';
   }
}


