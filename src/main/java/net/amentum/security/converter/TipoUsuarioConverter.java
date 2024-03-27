package net.amentum.security.converter;

import net.amentum.security.model.TipoUsuario;
import net.amentum.security.views.TipoUsuarioView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TipoUsuarioConverter {

   private Logger logger = LoggerFactory.getLogger(TipoUsuarioConverter.class);

   public TipoUsuarioView toView(TipoUsuario tipoUsuario, Boolean completeConversion) {
      TipoUsuarioView tipoUsuarioView = new TipoUsuarioView();
      tipoUsuarioView.setIdTipoUsuario(tipoUsuario.getIdTipoUsuario());
      tipoUsuarioView.setDescripcion(tipoUsuario.getDescripcion());
      tipoUsuarioView.setDisponible(tipoUsuario.getDisponible());
      tipoUsuarioView.setVisible(tipoUsuario.getVisible());

      logger.debug("convertir tipoUsuario to View: {}", tipoUsuarioView);
      return tipoUsuarioView;
   }
}
