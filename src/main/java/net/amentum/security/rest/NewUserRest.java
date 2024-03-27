package net.amentum.security.rest;

import net.amentum.common.BaseController;
import net.amentum.common.GenericException;
import net.amentum.security.exception.NewUserException;
import net.amentum.security.model.NewUser;
import net.amentum.security.service.NewUsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("new-user")
public class NewUserRest extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(NewUserRest.class);

    @Autowired
    private NewUsersService service;

    @RequestMapping(value = "get-link", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String createUserLink(@RequestParam()String username,
                                 @RequestParam() String idUsuario,
                                 @RequestParam() Long idGroup) throws NewUserException {
        try {
            if (username.isEmpty())
                throw new Exception("El username viene vacío");
            if (idUsuario.isEmpty())
                throw new Exception("El id viene vacío");
            if (idGroup == null || idGroup <= 0)
                throw new Exception("El grupo viene vacío");
            return service.createUserLink(username, idUsuario, idGroup);
        } catch (NewUserException ex) {
            throw ex;
        } catch (Exception ex) {
            NewUserException exception = new NewUserException("Error al solicitar el enlace", GenericException.LAYER_CONTROLLER, GenericException.ACTION_INSERT);
            exception.addError(ex.getMessage());
            logger.error("Error al solicitar el enlace - {}", ex.getMessage());
            throw exception;
        }
    }

    @RequestMapping(value = "link", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Boolean verifyLink(@RequestParam() String hash) throws NewUserException {
        try {
            if (hash.isEmpty())
                throw new Exception("Hash vacío");
            return service.verifyLink(hash);
        } catch (NewUserException ex) {
            throw ex;
        } catch (Exception ex) {
            NewUserException exception = new NewUserException("Error al verificar el enlace", GenericException.LAYER_CONTROLLER, GenericException.ACTION_SELECT);
            exception.addError(ex.getMessage());
            logger.error("Error al verificar el enlace - {}", ex.getMessage());
            throw exception;
        }
    }

    @RequestMapping(value = "find", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public NewUser findByHash(@RequestParam() String hash) throws NewUserException {
        try {
            if (hash.isEmpty())
                throw new Exception("Hash vacío");
            return service.findByHash(hash);
        } catch (NewUserException ex) {
            throw ex;
        } catch (Exception ex) {
            NewUserException exception = new NewUserException("Error al verificar el enlace", GenericException.LAYER_CONTROLLER, GenericException.ACTION_SELECT);
            exception.addError(ex.getMessage());
            logger.error("Error al verificar el enlace - {}", ex.getMessage());
            throw exception;
        }
    }

}
