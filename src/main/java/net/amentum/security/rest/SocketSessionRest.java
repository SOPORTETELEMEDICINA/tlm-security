package net.amentum.security.rest;


import io.swagger.annotations.Api;
import net.amentum.common.BaseController;
import net.amentum.common.GenericException;
import net.amentum.security.service.SocketConnectionService;
import net.amentum.security.views.SocketSessionView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("sockets")
@Api(description = "Implementacion de sesiones para socket")
public class SocketSessionRest extends BaseController{

    private final Logger logger = LoggerFactory.getLogger(SocketSessionRest.class);

    private SocketConnectionService socketConnectionService;

    @Autowired
    public SocketSessionRest(SocketConnectionService socketConnectionService){
        this.socketConnectionService = socketConnectionService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public void addSession(@RequestBody()SocketSessionView socketSessionView) throws GenericException{
        logger.info("Agregar nueva sesion al socket: {}",socketSessionView);
        this.socketConnectionService.addSocketSession(socketSessionView);
    }

    @DeleteMapping(value = "{sessionId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSession(@PathVariable()String sessionId) throws GenericException{
        logger.info("Eliminar sesion: {}",sessionId);
        this.socketConnectionService.removeSocketSession(sessionId);
    }

    @GetMapping(value = "{username}")
    public Boolean isSessionActive(@PathVariable()String username) throws GenericException{
        logger.info("Buscar sesiones activas del usuario: {}",username);
        return this.socketConnectionService.isSessionActive(username);
    }
}
