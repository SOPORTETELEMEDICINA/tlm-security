package net.amentum.security.service.impl;

import net.amentum.common.GenericException;
import net.amentum.security.exception.RecoverPasswordException;
import net.amentum.security.model.UserApp;
import net.amentum.security.persistence.RecoverPasswordRepository;
import net.amentum.security.persistence.UserAppRepository;
import net.amentum.security.service.PasswordRequestService;
import net.amentum.security.utils.email.EmailService;
import net.amentum.security.views.ChangePasswordRequestView;
import net.amentum.security.views.RecoverPasswordRequestView;
import net.amentum.security.views.UserAppPageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.*;

@Service
@Transactional(readOnly = false)
public class PasswordRequestServiceImpl implements PasswordRequestService {

    private final Logger logger = LoggerFactory.getLogger(PasswordRequestServiceImpl.class);

    private UserAppRepository userAppRepository;

    private RecoverPasswordRepository passwordRepository;

    private String emailTemplateHtml = "recover-password.html";

    private EmailService emailService;

    @Value("${email.from}")
    private String emailFrom;

    @Value("${service.recover.url}")
    private String link;

    @Autowired
    public void setUserAppRepository(UserAppRepository userAppRepository) {
        this.userAppRepository = userAppRepository;
    }

    @Autowired
    public void setPasswordRepository(RecoverPasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    @Transactional(rollbackFor = RecoverPasswordException.class)
    public UserAppPageView sendPasswordRequest(String email) throws RecoverPasswordException {
        try {
            UserApp user = userAppRepository.findByEmailUpper(email.toUpperCase());
            logger.info("Usuario para restablecer contraseña: " + user.getUsername());
            UserAppPageView userAppView = new UserAppPageView();
            userAppView.setIdUserApp(user.getUserAppId());
            userAppView.setUsername(user.getUsername());
            logger.info("Response {}", userAppView);
            return userAppView;
        } catch (NullPointerException npe) {
            RecoverPasswordException exception = new RecoverPasswordException("Error al solicitar recuperación de contraseña", GenericException.LAYER_SERVICE,GenericException.ACTION_SELECT);
            exception.addError("Usuario no encontrado");
            throw exception;
        } catch (Exception ex) {
            RecoverPasswordException exception = new RecoverPasswordException("Error al solicitar recuperación de contraseña", GenericException.LAYER_SERVICE,GenericException.ACTION_SELECT);
            exception.addError(ex.getMessage());
            logger.error("Error al recuperar contraseña - {}",ex.getMessage());
            throw exception;
        }
    }

    @Override
    public String hashPass(String user, String pass) throws RecoverPasswordException {
        logger.info("Usuario: {}", user);
        logger.info("Contraseña: {}", pass);

        MessageDigestPasswordEncoder passwordEncoder = new MessageDigestPasswordEncoder("SHA-256");
        passwordEncoder.setIterations(1000);
        String passha = pass+"{"+user+"}";
        logger.info("Hash: {}", passha.toString());

        String hashpass = passwordEncoder.encodePassword(passha,null);
        logger.info("Hash: {}", hashpass.toString());
        return hashpass.toString();
    }

    @Override
    @Transactional(rollbackFor = RecoverPasswordException.class)
    public void validatePasswordRequest(RecoverPasswordRequestView requestView) throws RecoverPasswordException {
        try{
            if(requestView.getUsername() == null || requestView.getUsername().trim().isEmpty())
                throw new RecoverPasswordException("El campo de usuario viene vacío",GenericException.LAYER_SERVICE,GenericException.ACTION_VALIDATE);
            if(requestView.getPassword() == null || requestView.getPassword().trim().isEmpty())
                throw new RecoverPasswordException("El campo de contraseña viene vacío",GenericException.LAYER_SERVICE,GenericException.ACTION_VALIDATE);
            if(requestView.getHash() == null || requestView.getHash().trim().isEmpty())
                throw new RecoverPasswordException("El campo de hash viene vacío",GenericException.LAYER_SERVICE,GenericException.ACTION_VALIDATE);
            MessageDigestPasswordEncoder passwordEncoder = new MessageDigestPasswordEncoder("SHA-256");
            passwordEncoder.setIterations(1000);
            UserApp user = userAppRepository.findByUsername(requestView.getUsername());
            if(user == null) {
                logger.error("Usuario no encontrado");
                throw new RecoverPasswordException("Usuario no encontrado", GenericException.LAYER_SERVICE, GenericException.ACTION_VALIDATE);
            }
            user.setPassword(requestView.getHash());
            user.setPassword2(passwordEncoder.encodePassword(String.format("%s{%s}", requestView.getPassword(), user.getEmail()),null));
            user.setPassword3(passwordEncoder.encodePassword(String.format("%s{%s}", requestView.getPassword(), user.getTelefono()),null));
            logger.info("Usuario para contraseña nueva {}", user.getUsername());
            userAppRepository.save(user);
            logger.info("Contraseña actualizada");

            Map<String,String> replacements = new HashMap<>();
            replacements.put("recoverEmail",user.getEmail().toUpperCase());
            replacements.put("userName", user.getUsername().toUpperCase());
            replacements.put("recoverHash",requestView.getPassword());
            sendEmail("Cambio de solicitud de contraseña", user.getEmail().toUpperCase(), replacements);
        }catch (RecoverPasswordException re){
            throw re;
        }catch (NullPointerException npe){
            RecoverPasswordException exception = new RecoverPasswordException("Error al solicitar recuperación de contraseña", GenericException.LAYER_SERVICE,GenericException.ACTION_UPDATE);
            throw exception;
        }
        catch (Exception ex){
            RecoverPasswordException exception = new RecoverPasswordException("Error al solicitar recuperación de contraseña", GenericException.LAYER_SERVICE,GenericException.ACTION_UPDATE);
            logger.error("Error al recuperar contraseña",ex);
            throw exception;
        }
    }

    @Override
    @Transactional(rollbackFor = RecoverPasswordException.class)
    public void changePasswordRequest(ChangePasswordRequestView requestView) throws RecoverPasswordException {
        try{
            if(requestView.getUsername() == null || requestView.getUsername().trim().isEmpty()) {
                logger.error("El campo de usuario viene vacío");
                throw new RecoverPasswordException("El campo de usuario viene vacío", GenericException.LAYER_SERVICE, GenericException.ACTION_VALIDATE);
            }
            if(requestView.getOldPassword() == null || requestView.getOldPassword().trim().isEmpty()) {
                logger.error("El campo de contraseña anterior viene vacío");
                throw new RecoverPasswordException("El campo de contraseña anterior viene vacío", GenericException.LAYER_SERVICE, GenericException.ACTION_VALIDATE);
            }
            if(requestView.getNewPassword() == null || requestView.getNewPassword().trim().isEmpty()) {
                logger.error("El campo de nueva contraseña viene vacío");
                throw new RecoverPasswordException("El campo de nueva contraseña viene vacío", GenericException.LAYER_SERVICE, GenericException.ACTION_VALIDATE);
            }
            if(requestView.getPassword() == null || requestView.getPassword().trim().isEmpty()) {
                logger.error("El campo de contraseña viene vacío");
                throw new RecoverPasswordException("El campo de contraseña viene vacío", GenericException.LAYER_SERVICE, GenericException.ACTION_VALIDATE);
            }
            if(requestView.getNewPassword2() == null || requestView.getNewPassword2().trim().isEmpty()) {
                logger.error("El campo de contraseña2 viene vacío");
                throw new RecoverPasswordException("El campo de contraseña viene vacío", GenericException.LAYER_SERVICE, GenericException.ACTION_VALIDATE);
            }
            if(requestView.getNewPassword3() == null || requestView.getNewPassword3().trim().isEmpty()) {
                logger.error("El campo de contraseña3 viene vacío");
                throw new RecoverPasswordException("El campo de contraseña viene vacío", GenericException.LAYER_SERVICE, GenericException.ACTION_VALIDATE);
            }
            UserApp user = userAppRepository.findByUsername(requestView.getUsername());
            if(user == null) {
                logger.error("Usuario no encontrado");
                throw new RecoverPasswordException("Usuario no encontrado", GenericException.LAYER_SERVICE, GenericException.ACTION_VALIDATE);
            }
            if(!user.getPassword().equals(requestView.getOldPassword())) {
                logger.error("La contraseña ingresada no coincide con la anterior");
                throw new RecoverPasswordException("La contraseña ingresada no coincide con la anterior", GenericException.LAYER_SERVICE, GenericException.ACTION_VALIDATE);
            }
            user.setPassword(requestView.getNewPassword());
            user.setPassword2(requestView.getNewPassword2());
            user.setPassword3(requestView.getNewPassword3());
            logger.info("Usuario para contraseña nueva {}", user.getUsername());
            userAppRepository.save(user);
            logger.info("Contraseña actualizada");

            Map<String,String> replacements = new HashMap<>();
            replacements.put("recoverEmail",user.getEmail().toUpperCase());
            replacements.put("userName", user.getUsername().toUpperCase());
            replacements.put("recoverHash",requestView.getPassword());
            sendEmail("Generación de nueva contraseña", user.getEmail().toUpperCase(), replacements);
        } catch (RecoverPasswordException re) {
            logger.error(re.getLocalizedMessage());
            throw re;
        } catch (NullPointerException npe) {
            logger.error(npe.getLocalizedMessage());
            RecoverPasswordException exception = new RecoverPasswordException("Error al solicitar recuperación de contraseña", GenericException.LAYER_SERVICE,GenericException.ACTION_UPDATE);
            exception.addError(npe.getLocalizedMessage());
            throw exception;
        } catch (Exception ex){
            RecoverPasswordException exception = new RecoverPasswordException("Error al solicitar recuperación de contraseña", GenericException.LAYER_SERVICE,GenericException.ACTION_UPDATE);
            exception.addError(ex.getLocalizedMessage());
            logger.error("Error al recuperar contraseña",ex);
            throw exception;
        }
    }

    public void sendEmail(String title, String recipient, Map<String, String> replacements) {
        String message = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "\t<head>\n" +
                "\t\t<title>Generación de nueva contraseña</title>\n" +
                "\t\t<meta charset=\"utf-8\">\n" +
                "\t\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "\t\t<header style=\"background-color: #7CB1C2;\">\n" +
                "\t\t\t<h1 style=\"font-size: 20px; letter-spacing: 1px; text-transform: uppercase; text-align: center; \">Generaci&#243;n de nueva contrase&#241;a</h1>\n" +
                "\t\t</header>\n" +
                "\t\t<section>\n" +
                "\t\t\t<h3 style=\"font-size: 17px; letter-spacing: 1px; color: #25ad9c;\">  EL PRESENTE CORREO ES PARA NOTIFICAR LA GENERACI&#211;N DE UNA NUEVA CONTRASE&#209;A</h3><br></br>\n" +
                "\t\t\t<h3 style=\"font-size: 15px; letter-spacing: 1px; color: #25ad9c;\">USUARIO:  {{userName}}</h3>\n" +
                "\t\t\t<h3 style=\"font-size: 15px; letter-spacing: 1px; color: #25ad9c;\">CORREO:  {{recoverEmail}}</h3>\n" +
                "\t\t\t<h3 style=\"font-size: 15px; letter-spacing: 1px; color: #25ad9c;\">CONTRASE&#209;A NUEVA:  {{recoverHash}}</h3>\n" +
                "<br></br><br></br><br></br><br></br>\n" +
                "\t\t\t<h1 style=\"font-size: 25px; letter-spacing: 1px; text-transform: uppercase;  color: #25ad9c;\">   <STRONG>Descarga nuestra aplicaci&#243;n </STRONG></h1>\n" +
                "<br></br><br></br>\n" +
                "\t\t\t<h1 style=\"font-size: 25px; letter-spacing: 1px; text-transform: uppercase;  color: #25ad9c;\">   <STRONG>Android:</STRONG></h1>\n" +
                "\t\t\t<a href=\"https://play.google.com/store/apps/details?id=com.tmesalud.telemetria\" style=\"font-size: 40px; text-align: center;\">Play Store</a>\n" +
                "<br></br><br></br><br></br>\n" +
                "\t\t\t<h1 style=\"font-size: 25px; letter-spacing: 1px; text-transform: uppercase;  color: #25ad9c;\">   <STRONG>iPhone:</STRONG></h1>\n" +
                "\t\t\t<a href=\"https://apps.apple.com/mx/app/cm-monitoreo-pacientes/id1585180778\" style=\"font-size: 40px; text-align: center;\">App Store</a>\n" +
                "\t\t</section>\t  \n" +
                "\t</body>\n" +
                "</html>";
        for (Map.Entry<String, String> entry : replacements.entrySet())
            message = message.replace("{{" + entry.getKey() + "}}", entry.getValue());
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.host", "mail.telemedicina.lat");
            props.put("mail.smtp.port", "465");

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("noresponder@telemedicina.lat", "Z{hcvq1x}T9.");
                }
            });
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("noresponder@telemedicina.lat", false));

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            msg.setSubject(title);
            msg.setContent(message, "text/html");
            msg.setSentDate(new Date());

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(message, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            Transport.send(msg);
            logger.info("Correo enviado");
        }catch (Exception ex) {
            logger.error("Error al enviar correo - {}",ex.getMessage());
        }
    }
}
