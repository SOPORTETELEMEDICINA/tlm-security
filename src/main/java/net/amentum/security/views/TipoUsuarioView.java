package net.amentum.security.views;


import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;


public class TipoUsuarioView implements Serializable {

   private static final long serialVersionUID = 1996456088490727309L;

   private Integer idTipoUsuario;
   @NotEmpty(message = "Debe agregar una descripcion")
   private String descripcion;
   @NotEmpty(message = "Debe agregar un disponible")
   private Boolean disponible;
   @NotEmpty(message = "Debe agregar un visible")
   private Boolean visible;

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
      return "TipoUsuarioView{" +
         "idTipoUsuario=" + idTipoUsuario +
         ", descripcion='" + descripcion + '\'' +
         ", disponible=" + disponible +
         ", visible=" + visible +
         '}';
   }
}
