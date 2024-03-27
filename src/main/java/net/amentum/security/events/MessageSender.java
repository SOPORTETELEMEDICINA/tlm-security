package net.amentum.security.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {


    private final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    // Sre10022020 Quito JMS
    //@Autowired
    //@Output(Processor.OUTPUT)
    private MessageChannel output;

    public void sendMessage(EventMessage sendMessage){
        logger.info("Enviar mensaje a gateway: {}",sendMessage);
        output.send(MessageBuilder.withPayload(sendMessage).build());
    }
}
