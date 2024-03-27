package net.amentum.security.rest;

import net.amentum.common.BaseController;
import net.amentum.security.exception.UserAlentaException;
import net.amentum.security.model.UserAlenta;
import net.amentum.security.service.UserAlentaService;
import net.amentum.security.views.UserAlentaView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("users-alenta")
public class UserAlentaRest extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(UserAppRest.class);

    @Autowired
    private UserAlentaService service;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createUserAlenta(@RequestBody @Valid UserAlentaView view) throws UserAlentaException {
        try {
            if(view.getIdUser() == null || view.getIdUser().isEmpty())
                throw new UserAlentaException("Usuario vacio", UserAlentaException.LAYER_SERVICE, UserAlentaException.ACTION_INSERT);
            if(view.getIdAlenta() == null || view.getIdAlenta().isEmpty())
                throw new UserAlentaException("Usuario alenta vacio", UserAlentaException.LAYER_SERVICE, UserAlentaException.ACTION_INSERT);
            service.insertUserAlenta(view);
        } catch (UserAlentaException uae) {
            logger.error("===>>>Error validando datos - " + uae.getMessage());
            throw uae;
        } catch (Exception ex) {
            UserAlentaException uae = new UserAlentaException("No fue posible obtener el usuario", UserAlentaException.LAYER_SERVICE, UserAlentaException.ACTION_INSERT);
            uae.addError("Ocurrio un error al insertar el usuario: {" + view + "}");
            logger.error("Error al insertar usuario - CODE: {} - {}", uae.getExceptionCode(), view, ex);
            throw uae;
        }
    }

    @RequestMapping(value = "findUser", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public UserAlentaView getUserById(@RequestParam(required = false) String idUser) throws UserAlentaException {
        try {
            if(idUser == null || idUser.isEmpty())
                throw new UserAlentaException("Usuario vacio", UserAlentaException.LAYER_SERVICE, UserAlentaException.ACTION_INSERT);
            return service.getUserByIdUser(idUser);
        } catch (UserAlentaException uae) {
            logger.error("===>>>Error validando datos - " + uae.getMessage());
            throw uae;
        } catch (Exception ex) {
            UserAlentaException uae = new UserAlentaException("No fue posible obtener el usuario", UserAlentaException.LAYER_SERVICE, UserAlentaException.ACTION_INSERT);
            uae.addError("Ocurrio un error al obtener el usuario: {" + idUser + "}");
            logger.error("Error al obtener usuario - CODE: {} - {}", uae.getExceptionCode(), idUser, ex);
            throw uae;
        }
    }

    @RequestMapping(value = "findUserAlenta", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public UserAlentaView getUserByIdAlenta(@RequestParam(required = false) String idUserAlenta) throws UserAlentaException {
        try {
            if(idUserAlenta == null || idUserAlenta.isEmpty())
                throw new UserAlentaException("Usuario vacio", UserAlentaException.LAYER_SERVICE, UserAlentaException.ACTION_INSERT);
            return service.getUserByIdAlenta(idUserAlenta);
        } catch (UserAlentaException uae) {
            logger.error("===>>>Error validando datos - " + uae.getMessage());
            throw uae;
        } catch (Exception ex) {
            UserAlentaException uae = new UserAlentaException("No fue posible obtener el usuario", UserAlentaException.LAYER_SERVICE, UserAlentaException.ACTION_INSERT);
            uae.addError("Ocurrio un error al obtener el usuario: {" + idUserAlenta + "}");
            logger.error("Error al obtener usuario - CODE: {} - {}", uae.getExceptionCode(), idUserAlenta, ex);
            throw uae;
        }
    }
}
