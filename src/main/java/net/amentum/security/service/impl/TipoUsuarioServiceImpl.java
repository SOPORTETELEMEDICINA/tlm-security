package net.amentum.security.service.impl;

import net.amentum.security.converter.TipoUsuarioConverter;
import net.amentum.security.exception.TipoUsuarioException;
import net.amentum.security.model.TipoUsuario;
import net.amentum.security.persistence.TipoUsuarioRepository;
import net.amentum.security.service.TipoUsuarioService;
import net.amentum.security.views.TipoUsuarioView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TipoUsuarioServiceImpl implements TipoUsuarioService {

   private final Logger logger = LoggerFactory.getLogger(TipoUsuarioServiceImpl.class);

   private TipoUsuarioRepository tipoUsuarioRepository;

   private TipoUsuarioConverter tipoUsuarioConverter;

   @Autowired
   public void setTipoUsuarioConverter(TipoUsuarioConverter tipoUsuarioConverter) {
      this.tipoUsuarioConverter = tipoUsuarioConverter;
   }

   @Autowired
   public void setTipoUsuarioRepository(TipoUsuarioRepository tipoUsuarioRepository) {
      this.tipoUsuarioRepository = tipoUsuarioRepository;
   }

   @Override
   public List<TipoUsuarioView> findAll(Boolean visible) throws TipoUsuarioException {
      try {
         List<TipoUsuario> tipoUsuarioList = new ArrayList<>();
         List<TipoUsuarioView> tipoUsuarioViewList = new ArrayList<>();
         if (visible == null) {
            tipoUsuarioList = tipoUsuarioRepository.findAll();
         } else {
            tipoUsuarioList = tipoUsuarioRepository.findAll().stream().filter(tipoUsuario -> tipoUsuario.getVisible().compareTo(visible) == 0).collect(Collectors.toList());
         }
         for (TipoUsuario tpoUsu : tipoUsuarioList) {
            tipoUsuarioViewList.add(tipoUsuarioConverter.toView(tpoUsu, Boolean.TRUE));
         }
         return tipoUsuarioViewList;
      }catch (Exception ex) {
         TipoUsuarioException tpoUE = new TipoUsuarioException("No fue posible obtener TipoUsuario", TipoUsuarioException.LAYER_DAO, TipoUsuarioException.ACTION_SELECT);
         tpoUE.addError("Ocurrio un error al obtener TipoUsuario");
         logger.error("Error al  obtener TipoUsuario - CODE: {} - {}",tpoUE.getExceptionCode(),ex);
         throw ex;
      }
   }

}
