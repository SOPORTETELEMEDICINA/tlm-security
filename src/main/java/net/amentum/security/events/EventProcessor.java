package net.amentum.security.events;


import net.amentum.common.GenericException;
import net.amentum.security.service.SocketConnectionService;
import net.amentum.security.views.SocketSessionView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EventProcessor {

    private final Logger logger = LoggerFactory.getLogger(EventProcessor.class);

    private SocketConnectionService socketConnectionService;


    @Autowired
    public void setSocketConnectionService(SocketConnectionService socketConnectionService) {
        this.socketConnectionService = socketConnectionService;
    }

    // Sre10022020 Quito JMS:
    //@StreamListener(Processor.INPUT)
    public void eventListener(Map<String,Object> messagePayload){
        String code = (String)messagePayload.get(EventMessageType.CODE);

        switch (code){
            case EventMessageType.CONNECTED:
                logger.info("Guardar sesion de usuario: {}",messagePayload);
                Map<String,Object> objectMap = (Map<String, Object>) messagePayload.get(EventMessageType.CONTENT);
                SocketSessionView sessionView = new SocketSessionView();
                sessionView.setDeviceId((String)objectMap.get("deviceId"));
                sessionView.setSessionId((String)objectMap.get("sessionId"));
                sessionView.setUsername((String)objectMap.get("username"));
                try {
                    this.socketConnectionService.addSocketSession(sessionView);
                }catch (GenericException ge){

                }
                break;
            case EventMessageType.DISCONNECTED:
                logger.info("Eliminar sesion de usuario: {}",messagePayload);
                try {
                    this.socketConnectionService.removeSocketSession((String)messagePayload.get(EventMessageType.CONTENT));
                }catch (GenericException ge){

                }
                break;
            case EventMessageType.MESSAGES:
                logger.info("Recuperar mensajes de usuario: {}",messagePayload);
                logger.info("Guardar sesion de usuario: {}",messagePayload);
                objectMap = (Map<String, Object>) messagePayload.get(EventMessageType.CONTENT);
                sessionView = new SocketSessionView();
                //sessionView.setDeviceId((String)objectMap.get("deviceId"));
                sessionView.setSessionId((String)objectMap.get("sessionId"));
                sessionView.setUsername((String)objectMap.get("username"));
                try {
                    this.socketConnectionService.recoverMessages(sessionView);
                }catch (GenericException ge){

                }
                break;
        }

    }
}
