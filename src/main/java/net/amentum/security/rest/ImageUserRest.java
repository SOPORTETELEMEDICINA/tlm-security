package net.amentum.security.rest;

import io.swagger.annotations.Api;
import net.amentum.common.BaseController;
import net.amentum.common.GenericException;
import net.amentum.security.exception.ExceptionServiceCode;
import net.amentum.security.service.UserImageService;
import net.amentum.security.views.InfoBasicResponseView;
import net.amentum.security.views.UserImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@Api(description = "Manejo de imagen de usuario")
public class ImageUserRest extends BaseController{

    private final Logger logger = LoggerFactory.getLogger(ImageUserRest.class);

    private UserImageService imageService;

    @Autowired
    public void setImageService(UserImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * Método para cargar una nueva imagen de perfil para el usuario
     * @param imageView imagen a guardar en base de datos
     * @param userId ID único de usuario
     * @throws GenericException si no es posible guardar la imagen
     * **/
    @RequestMapping(value = "{userId}/imageProfile",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewImageProfile(@RequestBody @Validated UserImageView imageView,@PathVariable Long userId) throws GenericException{
        logger.info(ExceptionServiceCode.IMG+"- Agregar imagen de perfil - {} - {}",userId,imageView);
        imageService.addNewImageProfile(imageView,userId);
    }

    /**
     * Método para cargar una nueva imagen de perfil para el usuario
     * @param imageView imagen a editar en base de datos
     * @param userId ID único de usuario
     * @throws GenericException si no es posible guardar la imagen
     * **/
    @RequestMapping(value = "{userId}/imageProfile/{imageId}",method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateImageProfile(@RequestBody @Validated UserImageView imageView,@PathVariable Long userId,@PathVariable Long imageId) throws GenericException{
        logger.info(ExceptionServiceCode.IMG+"- Editar imagen de perfil -{} - {} - {}",imageId,userId,imageView);
        imageView.setUserImageId(imageId);
        imageService.updateImageProfile(imageView,userId);
    }


    /**
     * Método para cargar una nueva imagen de perfil para el usuario
     * @param imageId ID de imagen a eliminar
     * @throws GenericException si no es posible eliminar la imagen
     * **/
    @RequestMapping(value = "{userId}/imageProfile/{imageId}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteImageProfile(@PathVariable Long userId,@PathVariable Long imageId) throws GenericException{
        logger.info(ExceptionServiceCode.IMG+"- Eliminar imagen de perfil -{} - {}",imageId,userId);
        imageService.deleteImageProfile(imageId);
    }

    /**
     * Método para obtener la imagen del perfil de usuario
     * @param username nombre de usuario
     * @throws GenericException si no es posible obtener la imagen
     * **/
    @GetMapping("findImageByUsername")
    @ResponseStatus(HttpStatus.OK)
    public UserImageView getImageByUsername(@RequestParam(required = true) String username, @RequestParam(required = false,defaultValue = "false")Boolean small) throws GenericException {
        logger.info(ExceptionServiceCode.IMG+" - Obtener imagen de perfil del usuario: "+username);
        logger.debug(ExceptionServiceCode.IMG+" - Obtener imagen de perfil del usuario: "+username);
        return  imageService.getImageProfileByUsername(username, small);
    }


}
