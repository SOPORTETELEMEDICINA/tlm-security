package net.amentum.security.utils.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailService {

    private JavaMailSender javaMailSender;

    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(Email email){
        if(email.isHtml()){
            try{
                sendHtmlMail(email);
            }catch (MessagingException me){
                logger.error("Error al enviar correo",me);
            }
        }else{
            try{
                sendPlainTextMail(email);
            }catch (MessagingException me){
                logger.error("Error al enviar correo",me);
            }
        }

    }

    private void sendHtmlMail(Email eParams) throws MessagingException {
        boolean isHtml = true;
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(eParams.getTo().toArray(new String[eParams.getTo().size()]));
        helper.setReplyTo(eParams.getFrom());
        helper.setFrom(eParams.getFrom());
        helper.setSubject(eParams.getSubject());
        helper.setText(eParams.getMessage(), isHtml);
        if (eParams.getCc().size() > 0) {
            helper.setCc(eParams.getCc().toArray(new String[eParams.getCc().size()]));
        }
        javaMailSender.send(message);
    }

    private void sendPlainTextMail(Email eParams) throws MessagingException {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        eParams.getTo().toArray(new String[eParams.getTo().size()]);
        mailMessage.setTo(eParams.getTo().toArray(new String[eParams.getTo().size()]));
        mailMessage.setReplyTo(eParams.getFrom());
        mailMessage.setFrom(eParams.getFrom());
        mailMessage.setSubject(eParams.getSubject());
        mailMessage.setText(eParams.getMessage());
        if (eParams.getCc().size() > 0) {
            mailMessage.setCc(eParams.getCc().toArray(new String[eParams.getCc().size()]));
        }
        javaMailSender.send(mailMessage);
    }
}
