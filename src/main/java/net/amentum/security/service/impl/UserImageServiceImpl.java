package net.amentum.security.service.impl;


import net.amentum.common.GenericException;
import net.amentum.security.converter.UserImageConverter;
import net.amentum.security.exception.ExceptionServiceCode;
import net.amentum.security.exception.UserImageException;
import net.amentum.security.model.UserApp;
import net.amentum.security.model.UserImage;
import net.amentum.security.persistence.UserAppRepository;
import net.amentum.security.persistence.UserImageRepository;
import net.amentum.security.service.UserImageService;
import net.amentum.security.views.UserImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;


@Service
@Transactional(readOnly = false)
public class UserImageServiceImpl implements UserImageService{

    private final Logger logger = LoggerFactory.getLogger(UserImageServiceImpl.class);

    private UserImageRepository imageRepository;

    private UserAppRepository userAppRepository;

    private UserImageConverter userImageConverter;

    @Autowired
    public void setImageRepository(UserImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Autowired
    public void setUserAppRepository(UserAppRepository userAppRepository) {
        this.userAppRepository = userAppRepository;
    }

    @Autowired
    public void setUserImageConverter(UserImageConverter userImageConverter) {
        this.userImageConverter = userImageConverter;
    }

    @Override
    @Transactional(readOnly = false,rollbackFor = UserImageException.class)
    public void addNewImageProfile(UserImageView imageView, Long userId) throws UserImageException {
        try{
            UserApp userApp = userAppRepository.findOne(userId);
            if(userApp.getUserImage()==null){
                UserImage image = userImageConverter.fromView(imageView);
                image.setUserApp(userApp);
                imageRepository.save(image);
            }else{
                Long imageId = userApp.getUserImage().getUserImageId();
                Date createdDate = userApp.getUserImage().getCreatedDate();
                UserImage image = userImageConverter.fromView(imageView);
                image.setUserImageId(imageId);
                image.setCreatedDate(createdDate);
                userApp.setUserImage(image);
                imageRepository.save(image);
            }
        }catch (Exception ex){
            UserImageException userImageException = new UserImageException("Error al cargar imagen de perfil", GenericException.LAYER_DAO,GenericException.ACTION_INSERT);
            logger.error(ExceptionServiceCode.IMG+"- Error al agregar imagen de perfil - {} - {} - CODE: {}",userId,imageView,userImageException.getExceptionCode());
            throw userImageException;
        }
    }

    @Override
    @Transactional(readOnly = false,rollbackFor = UserImageException.class)
    public void updateImageProfile(UserImageView imageView, Long userId) throws UserImageException {
        try{
            UserApp userApp = userAppRepository.findOne(userId);
            if(userApp.getUserImage()==null){
                UserImage image = userImageConverter.fromView(imageView);
                image.setUserApp(userApp);
                imageRepository.save(image);
            }else{
                Long imageId = userApp.getUserImage().getUserImageId();
                Date createdDate = userApp.getUserImage().getCreatedDate();
                UserImage image = userImageConverter.fromView(imageView);
                image.setUserImageId(imageId);
                image.setCreatedDate(createdDate);
                userApp.setUserImage(image);
                imageRepository.save(image);
            }
        }catch (Exception ex){
            UserImageException userImageException = new UserImageException("Error al editar imagen de perfil", GenericException.LAYER_DAO,GenericException.ACTION_UPDATE);
            logger.error(ExceptionServiceCode.IMG+"- Error al editar imagen de perfil - {} - {} - CODE: {}",userId,imageView,userImageException.getExceptionCode());
            throw userImageException;
        }
    }

    @Override
    @Transactional(readOnly = false,rollbackFor = UserImageException.class)
    public void deleteImageProfile(Long imageId) throws UserImageException {
        try{
            imageRepository.delete(imageId);
        }catch (DataIntegrityViolationException dta){
            UserImageException userImageException = new UserImageException("Error al eliminar imagen de perfil", GenericException.LAYER_DAO,GenericException.ACTION_DELETE);
            logger.error(ExceptionServiceCode.IMG+"- Error al eliminar imagen de perfil - {} - {} - CODE: {}",imageId,userImageException.getExceptionCode());
            throw userImageException;
        }catch (Exception ex){
            UserImageException userImageException = new UserImageException("Error al editar imagen de perfil", GenericException.LAYER_DAO,GenericException.ACTION_DELETE);
            logger.error(ExceptionServiceCode.IMG+"- Error al eliminar imagen de perfil - {} - {} - CODE: {}",imageId,userImageException.getExceptionCode());
            throw userImageException;
        }
    }

    @Override
    public UserImageView getImageProfileByUsername(String username, Boolean small) throws UserImageException {
        try{
            UserImage userImage =  imageRepository.findByUserApp_Username(username);
            if (userImage!= null && userImage.getImageContent() != null){
                logger.info("El usuario: {} tiene imagen de perfil", username);
                logger.debug("El usuario: {} tiene imagen de perfil. - info: {}", username, userImage);
                return userImageConverter.fromEntity(userImage, small);
            }
            logger.info("El usuario: {} no tiene imagen de perfil", username);
            logger.debug("El usuario: {} no tiene imagen de perfil. - info: {}", username, userImage);
            return null;
        }catch (DataIntegrityViolationException dta){
            UserImageException userImageException = new UserImageException("Error al obtener imagen de perfil", GenericException.LAYER_DAO,GenericException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.IMG+"- Error al obtener imagen de perfil - {} - {} - CODE: {}",username,userImageException.getExceptionCode());
            throw userImageException;
        }catch (Exception ex){
            UserImageException userImageException = new UserImageException("Error al obtener imagen de perfil", GenericException.LAYER_DAO,GenericException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.IMG+"- Error al obtener imagen de perfil - {} - {} - CODE: {}",username,userImageException.getExceptionCode());
            throw userImageException;
        }
    }
}
