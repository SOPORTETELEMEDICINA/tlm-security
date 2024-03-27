package net.amentum.security.service.impl;

import net.amentum.security.converter.UserAlentaConverter;
import net.amentum.security.exception.UserAlentaException;
import net.amentum.security.exception.UserAppException;
import net.amentum.security.model.UserAlenta;
import net.amentum.security.persistence.UserAlentaRepository;
import net.amentum.security.service.UserAlentaService;
import net.amentum.security.views.UserAlentaView;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserAlentaServiceImpl implements UserAlentaService {

    private final Logger logger = LoggerFactory.getLogger(UserAlentaServiceImpl.class);

    @Autowired
    private UserAlentaRepository repository;

    @Autowired
    private UserAlentaConverter converter;

    @Override
    @Transactional(readOnly = false, rollbackFor = UserAlentaException.class)
    public void insertUserAlenta(UserAlentaView view) throws UserAlentaException {
        try {
            UserAlenta user = repository.findByIdUser(view.getIdUser());
            if(user != null)
                throw new UserAlentaException("Usuario ya registrado por id usuario", UserAlentaException.LAYER_DAO, UserAlentaException.ACTION_INSERT);
            user = repository.findByIdAlenta(view.getIdAlenta());
            if(user != null)
                throw new UserAlentaException("Usuario ya registrado por id Alenta", UserAlentaException.LAYER_DAO, UserAlentaException.ACTION_INSERT);
            repository.save(converter.toEntity(view));
        } catch (UserAlentaException uae) {
            logger.error("===>>>Error validando datos - " + uae.getMessage());
            throw uae;
        } catch (DataIntegrityViolationException dte) {
            UserAlentaException uae = new UserAlentaException("No fue posible agregar Usuario", UserAlentaException.LAYER_DAO, UserAlentaException.ACTION_INSERT);
            uae.addError("Ocurrio un error al agregar Usuario");
            logger.error("===>>>Error al insertar nuevo Usuario - CODE: {} - {}", uae.getExceptionCode(), view, dte);
            throw uae;
        } catch (ConstraintViolationException dte) {
            UserAlentaException uae = new UserAlentaException("No fue posible agregar  Usuario", UserAlentaException.LAYER_DAO, UserAlentaException.ACTION_INSERT);
            uae.addError("Ocurrio un error al agregar Usuario");
            logger.error("===>>>Error al insertar nuevo Usuario - CODE: {} - {}", uae.getExceptionCode(), view, dte);
            throw uae;
        } catch (Exception ex) {
            UserAlentaException uae = new UserAlentaException("No fue posible insertar el usuario", UserAlentaException.LAYER_DAO, UserAlentaException.ACTION_INSERT);
            uae.addError("Ocurrio un error al insertar el usuario: {}");
            logger.error("Error al insertar nuevo usuario - CODE: {} - {}", uae.getExceptionCode(), view, ex);
            throw uae;
        }
    }

    @Override
    public UserAlentaView getUserByIdUser(String idUser) throws UserAlentaException {
        try {
            UserAlenta user = repository.findByIdUser(idUser);
            if(user == null)
                throw new UserAlentaException("Usuario no encontrado: " + idUser, UserAlentaException.LAYER_DAO, UserAlentaException.ACTION_SELECT);
            return converter.toView(user);
        } catch (UserAlentaException uae) {
            logger.error("===>>>Error validando datos - " + uae.getMessage());
            throw uae;
        } catch (Exception ex) {
            UserAlentaException uae = new UserAlentaException("No fue posible obtener el usuario", UserAlentaException.LAYER_DAO, UserAlentaException.ACTION_SELECT);
            uae.addError("Ocurrio un error al obtener el usuario: {" + idUser + "}");
            logger.error("Error al obtener usuario - CODE: {} - {}", uae.getExceptionCode(), idUser, ex);
            throw uae;
        }
    }

    @Override
    public UserAlentaView getUserByIdAlenta(String idUserAlenta) throws UserAlentaException {
        try {
            UserAlenta user = repository.findByIdAlenta(idUserAlenta);
            if(user == null)
                throw new UserAlentaException("Usuario no encontrado: " + idUserAlenta, UserAlentaException.LAYER_DAO, UserAlentaException.ACTION_SELECT);
            return converter.toView(user);
        } catch (UserAlentaException uae) {
            logger.error("===>>>Error validando datos - " + uae.getMessage());
            throw uae;
        } catch (Exception ex) {
            UserAlentaException uae = new UserAlentaException("No fue posible obtener el usuario", UserAlentaException.LAYER_DAO, UserAlentaException.ACTION_SELECT);
            uae.addError("Ocurrio un error al obtener el usuario: {" + idUserAlenta + "}");
            logger.error("Error al obtener usuario - CODE: {} - {}", uae.getExceptionCode(), idUserAlenta, ex);
            throw uae;
        }
    }
}
