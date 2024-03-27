package net.amentum.security.rest;


import net.amentum.common.BaseController;
import net.amentum.security.exception.ExceptionServiceCode;
import net.amentum.security.exception.UserAppException;
import net.amentum.security.exception.UsuariosCanalizadosException;
import net.amentum.security.model.UsuariosCanalizados;
import net.amentum.security.service.UsuariosCanalizadosService;
import net.amentum.security.views.UserAppView;
import net.amentum.security.views.UsuariosCanalizadosPageView;
import net.amentum.security.views.UsuariosCanalizadosView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Ggarcia GGR20200626
 */

@RestController
@RequestMapping("canalizar")
public class UsuariosCanalizadosRest extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(UsuariosCanalizadosRest.class);

    private UsuariosCanalizadosService serviceUsuariosCanalizados;

    @Autowired
    public void setUsuariosCanalizadosService(UsuariosCanalizadosService serviceUsuariosCanalizados) {
        this.serviceUsuariosCanalizados = serviceUsuariosCanalizados;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    UsuariosCanalizadosPageView createUsuariosCanalizados(@RequestBody @Valid UsuariosCanalizadosView usuariosCanalizadosView) throws UsuariosCanalizadosException {
        try {
            logger.info(ExceptionServiceCode.USER + " - Crear una nueva relacion de usuarios canalizados: {}", usuariosCanalizadosView);
            serviceUsuariosCanalizados.createUsuariosCanalizados(usuariosCanalizadosView);
            /*serviceUsuariosCanalizados.insertaUsuariosCanalizados(usuariosCanalizadosView.getFechaInicial(), usuariosCanalizadosView.getFechaFinal()
                    ,usuariosCanalizadosView.getIdUsuarioEmisor(), usuariosCanalizadosView.getIdUsuarioReceptor(),
                    usuariosCanalizadosView.getIdUsuarioPaciente());*/
            UsuariosCanalizadosPageView usuariosCanalizadosPageView = new UsuariosCanalizadosPageView(
                    usuariosCanalizadosView.getUsuariosCanalizadosId(),
                    usuariosCanalizadosView.getIdUsuarioEmisor(), usuariosCanalizadosView.getIdUsuarioReceptor(),
                    usuariosCanalizadosView.getIdUsuarioPaciente(),
                    usuariosCanalizadosView.getFechaFinal(), usuariosCanalizadosView.getFechaFinal());
            return (usuariosCanalizadosPageView);
        } catch (UsuariosCanalizadosException uce) {
            throw uce;
        } catch (Exception ex) {
            UsuariosCanalizadosException uce = new UsuariosCanalizadosException("No fue posible insertar el usuario", UsuariosCanalizadosException.LAYER_DAO, UsuariosCanalizadosException.ACTION_SELECT);
            logger.error("Error al insertar el usuario canalizado - CODE: {} - ", uce.getExceptionCode(), ex);
            throw uce;
        }

    }

    @RequestMapping(value="page", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<UsuariosCanalizadosView> findUsuariosCanalizados(@RequestParam(required = false)Integer page, @RequestParam(required=false)Integer size, @RequestParam(required = false)
            String orderColumn, @RequestParam(required = false)String orderType, @RequestParam(required = false)Long idMedicoEmisor) throws UsuariosCanalizadosException {
        logger.info(ExceptionServiceCode.USER +"- Obtener listado de uusarios_canalizados: page {} - size: {} - orderColumn: {} - orderType: {} - idMedicoEmisor: {}",page,size,orderColumn,orderType, idMedicoEmisor);
        if(page==null)
            page = 0;
        if(size==null)
            size = 10;
        return serviceUsuariosCanalizados.findUsuariosCanalizados(page, size, orderColumn, orderType, idMedicoEmisor);

    }

    @RequestMapping(value = "{usuariosCanalizadosId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteUsuariosCanalizados(@PathVariable() Long usuariosCanalizadosId) throws UsuariosCanalizadosException {
        logger.info("Eliminar usuariosCanalizadosId: {}", usuariosCanalizadosId);
        serviceUsuariosCanalizados.deleteUsuariosCanalizados(usuariosCanalizadosId);
    }

    // Obtener la lista de canalizados al medico idUserApp
    @RequestMapping(value = "lista/{idUserApp}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Long> getListByUser(@PathVariable("idUserApp") Long idIUserApp) throws UsuariosCanalizadosException {
        logger.info("Obtener la lista de usuarios canalizados para el usuario: " + idIUserApp);
        List<Long> listaUsuarios = serviceUsuariosCanalizados.findListByUser(idIUserApp);
        return listaUsuarios;
    }

    // Obtener la lista de canalizados al medico idUserApp
    @RequestMapping(value = "pacienteCanalizado/{idPaciente}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Long> getStatusByPaciente(@PathVariable("idPaciente") Long idPaciente) throws UsuariosCanalizadosException {
        logger.info("Obtener la lista de usuarios canalizados para el usuario: " + idPaciente);
        return serviceUsuariosCanalizados.findByPaciente(idPaciente);
    }

}
