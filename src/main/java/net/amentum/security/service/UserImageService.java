package net.amentum.security.service;


import net.amentum.security.exception.UserImageException;
import net.amentum.security.views.UserImageView;

/**
 * @author Victor
 * */
public interface UserImageService {

    /**
     * Método para cargar una nueva imagen de perfil para el usuario
     * @param imageView imagen a guardar en base de datos
     * @param userId ID único de usuario
     * @throws UserImageException si no es posible guardar la imagen
     * **/
    void addNewImageProfile(UserImageView imageView,Long userId) throws UserImageException;

    /**
     * Método para cargar una nueva imagen de perfil para el usuario
     * @param imageView imagen a editar en base de datos
     * @param userId ID único de usuario
     * @throws UserImageException si no es posible guardar la imagen
     * **/
    void updateImageProfile(UserImageView imageView,Long userId) throws UserImageException;

    /**
     * Método para cargar una nueva imagen de perfil para el usuario
     * @param imageId ID de imagen a eliminar
     * @throws UserImageException si no es posible eliminar la imagen
     * **/
    void deleteImageProfile(Long imageId) throws UserImageException;

    /**
     * Método para obtener la imagen de perfil se usuario
     * @param username  nombre de usuario
     * @throws UserImageException si no es posible obtener la imagen
     * **/
    UserImageView getImageProfileByUsername(String username, Boolean small) throws UserImageException;


}
