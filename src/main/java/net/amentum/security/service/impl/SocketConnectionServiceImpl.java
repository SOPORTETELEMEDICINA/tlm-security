package net.amentum.security.service.impl;

import net.amentum.common.GenericException;
import net.amentum.security.events.EventMessage;
import net.amentum.security.events.EventMessageType;
import net.amentum.security.events.MessageSender;
import net.amentum.security.exception.SocketSessionException;
import net.amentum.security.model.SocketSession;
import net.amentum.security.persistence.SocketMessageRepository;
import net.amentum.security.persistence.SocketSessionRepository;
import net.amentum.security.service.SocketConnectionService;
import net.amentum.security.service.UserAppService;
import net.amentum.security.views.SocketSessionView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = false)
public class SocketConnectionServiceImpl implements SocketConnectionService {

    private final Logger logger = LoggerFactory.getLogger(SocketConnectionServiceImpl.class);

    private SocketSessionRepository sessionRepository;

    private SocketMessageRepository messageRepository;

    private UserAppService userAppService;

    private MessageSender messageSender;

    private final String ONLINE = "Online";
    private final String OFFLINE = "Offline";
    @Autowired
    public SocketConnectionServiceImpl(SocketMessageRepository messageRepository,
                                       SocketSessionRepository sessionRepository,
                                       UserAppService userAppService,
                                       MessageSender messageSender){
        this.sessionRepository = sessionRepository;
        this.messageRepository = messageRepository;
        this.userAppService = userAppService;
        this.messageSender = messageSender;
    }


    @Override
    @Transactional(rollbackFor = SocketSessionException.class)
    public void addSocketSession(SocketSessionView socketSessionView) throws SocketSessionException {
        try{
            SocketSession session = fromView(socketSessionView);
            this.sessionRepository.save(session);
            this.userAppService.updateUsersConnectionStatus(socketSessionView.getUsername(),ONLINE);
            EventMessage message = new EventMessage();
            message.setCode(EventMessageType.CONNECTED);
            message.setQueue(session.getUsername()+"/status");
            Map<String,Object> params = new HashMap<>();
            params.put(EventMessageType.CODE,EventMessageType.CONNECTED);
            params.put(EventMessageType.CONTENT,"Online");
            message.getMessages().add(params);
            messageSender.sendMessage(message);
        }catch (Exception ex){
            SocketSessionException socketException = new SocketSessionException(ex,"Error al agregar sesion de socket", GenericException.LAYER_SERVICE,GenericException.ACTION_INSERT);
            logger.error("Ocurrio un error al ingresar la sesion del socket: {}",socketSessionView,ex);
            throw  socketException;
        }
    }

    @Override
    public void recoverMessages(SocketSessionView socketSessionView) throws SocketSessionException {
        EventMessage message = new EventMessage();
        message.setCode(EventMessageType.MESSAGES);
        message.setQueue(socketSessionView.getUsername());
        Map<String,Object> params = new HashMap<>();
        params.put(EventMessageType.CODE,EventMessageType.MESSAGES);
        message.getMessages().add(params);
        messageSender.sendMessage(message);
    }

    @Override
    @Transactional(rollbackFor = SocketSessionException.class)
    public void removeSocketSession(String sessionId) throws SocketSessionException {
        try{
            SocketSession session = this.sessionRepository.findOne(sessionId);
            this.userAppService.updateUsersConnectionStatus(session.getUsername(),OFFLINE);
            this.sessionRepository.delete(sessionId);
            EventMessage message = new EventMessage();
            message.setCode(EventMessageType.CONNECTED);
            message.setQueue(session.getUsername()+"/status");
            Map<String,Object> params = new HashMap<>();
            params.put(EventMessageType.CODE,EventMessageType.CONNECTED);
            params.put(EventMessageType.CONTENT,"Offline");
            message.getMessages().add(params);
            messageSender.sendMessage(message);
        }catch (Exception ex){
            SocketSessionException socketException = new SocketSessionException(ex,"Error al eliminar sesion de socket", GenericException.LAYER_SERVICE,GenericException.ACTION_DELETE);
            logger.error("Ocurrio un error al eliminar la sesion del socket: {}",sessionId,ex);
            throw  socketException;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isSessionActive(String username) throws SocketSessionException {
        try{
            Specification<SocketSession> sessionSpec = Specifications.where((root, query, cb) ->
                cb.and(cb.equal(root.get("username"),username),cb.equal(root.get("active"),Boolean.TRUE))
            );
            List<SocketSession> sessions = sessionRepository.findAll(sessionSpec);
            logger.info("Numero de sesiones del usuario: {} - {} ",username,sessions.size());
            return sessions.isEmpty();
        }catch (Exception ex){
            SocketSessionException socketException = new SocketSessionException(ex,"Error al seleccionar sesion de socket", GenericException.LAYER_SERVICE,GenericException.ACTION_SELECT);
            logger.error("Ocurrio un error al seleccionar la sesion del usuario: {}",username,ex);
            throw  socketException;
        }
    }

    private SocketSession fromView(SocketSessionView view){
        SocketSession session = new SocketSession();
        session.setActive(Boolean.TRUE);
        session.setCreatedDate(new Date());
        session.setDeviceId(view.getDeviceId());
        session.setSessionId(view.getSessionId());
        session.setUsername(view.getUsername());
        return session;
    }
}
