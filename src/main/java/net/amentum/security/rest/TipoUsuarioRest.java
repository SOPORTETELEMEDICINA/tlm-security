package net.amentum.security.rest;

import net.amentum.common.BaseController;
import net.amentum.security.exception.TipoUsuarioException;
import net.amentum.security.service.TipoUsuarioService;
import net.amentum.security.views.TipoUsuarioView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("tipo-usuario")
public class TipoUsuarioRest extends BaseController {
   private final Logger logger = LoggerFactory.getLogger(TipoUsuarioRest.class);

   private TipoUsuarioService tipoUsuarioService;

   @Autowired
   public void setTipoUsuarioService(TipoUsuarioService tipoUsuarioService) {
      this.tipoUsuarioService = tipoUsuarioService;
   }

   @RequestMapping(value = "findAll", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<TipoUsuarioView> findAll(@RequestParam(required = false) Boolean visible) throws TipoUsuarioException {
      return tipoUsuarioService.findAll(visible);
   }

}
