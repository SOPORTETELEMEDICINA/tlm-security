package net.amentum.security.service.impl;

import net.amentum.common.GenericException;
import net.amentum.security.exception.NewUserException;
import net.amentum.security.model.NewUser;
import net.amentum.security.model.UserApp;
import net.amentum.security.persistence.NewUserRepository;
import net.amentum.security.persistence.UserAppRepository;
import net.amentum.security.service.NewUsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class NewUsersServiceImpl implements NewUsersService {

    private final Logger logger = LoggerFactory.getLogger(NewUsersServiceImpl.class);

    @Autowired
    private NewUserRepository repository;

    @Autowired
    private UserAppRepository userAppRepository;

    @Value("${service.baseUrl}")
    private String link;

    @Override
    @Transactional(rollbackFor = NewUserException.class)
    public String createUserLink(String username, String idUsuario, Long idGroup) throws NewUserException {
        try {
            NewUser newUser = null;
            UserApp userApp = userAppRepository.findByUsername(username);
            if(userApp == null)
                throw new Exception("Usuario no encontrado con el username: " + username);

            Specifications<NewUser> newUserSpec = Specifications.where((root, query, cb) ->
                    cb.and(cb.equal(root.get("username"),username), cb.equal(root.get("active"),Boolean.TRUE),
                            cb.equal(root.get("idGroup"),idGroup))
            );

            if(repository.count(newUserSpec) > 0) {
                newUser = repository.findByUsernameAndIdGroup(username, idGroup);
                newUser.setTimeBeforeExpire(addTimeToDate(new Date(), 7, Calendar.DAY_OF_YEAR));
                logger.info("Hash actualizado. Resultado {}", newUser);
                repository.save(newUser);
            } else {
                newUser = new NewUser();
                newUser.setActive(true);
                Date date = new Date();
                newUser.setCreatedDate(date);
                newUser.setTimeBeforeExpire(addTimeToDate(new Date(), 7, Calendar.DAY_OF_YEAR));
                newUser.setUsername(username);
                newUser.setidUsuario(idUsuario);
                newUser.setIdGroup(idGroup);
                newUser.setName(userApp.getName());

                ShaPasswordEncoder encoder = new ShaPasswordEncoder();
                encoder.setIterations(1500);
                newUser.setHash(encoder.encodePassword(UUID.randomUUID().toString().toUpperCase(), username));
                repository.save(newUser);
                logger.info("Resultado {}", newUser);
            }
            logger.info(String.format("%s?hash=%s", link, newUser.getHash()));
            return String.format("%s?hash=%s", link, newUser.getHash());
        } catch (Exception ex) {
            NewUserException exception = new NewUserException("Error al solicitar el enlace", GenericException.LAYER_SERVICE, GenericException.ACTION_INSERT);
            exception.addError(ex.getMessage());
            logger.error("Error al solicitar el enlace - {}", ex.getMessage());
            throw exception;
        }
    }

    @Override
    public Boolean verifyLink(String hash) throws NewUserException {
        try {
            NewUser user = repository.findByHash(hash);
            if(user == null)
                throw new Exception("No encontrado");
            logger.info(user.toString());
            return true;
        } catch (Exception ex) {
            NewUserException exception = new NewUserException("Error al verificar el enlace", GenericException.LAYER_SERVICE, GenericException.ACTION_SELECT);
            exception.addError(ex.getMessage());
            logger.error("Error al verificar el enlace - {}", ex.getMessage());
            throw exception;
        }
    }

    @Override
    public NewUser findByHash(String hash) throws NewUserException {
        try {
            NewUser user = repository.findByHash(hash);
            if(user == null)
                throw new Exception("No encontrado");
            logger.info(user.toString());
            return user;
        } catch (Exception ex) {
            NewUserException exception = new NewUserException("Error al verificar el enlace", GenericException.LAYER_SERVICE, GenericException.ACTION_SELECT);
            exception.addError(ex.getMessage());
            logger.error("Error al verificar el enlace - {}", ex.getMessage());
            throw exception;
        }
    }

    private Date addTimeToDate(Date date,Integer timeAdd,Integer addTo){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(addTo, timeAdd);
        return c.getTime();
    }

}
