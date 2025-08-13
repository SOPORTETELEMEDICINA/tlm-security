package net.amentum.security.service;

import net.amentum.security.exception.UserAppException;
import net.amentum.security.model.UserApp;
import net.amentum.security.views.*;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author marellano on 25/04/17.
 */
public interface UserAppService {

    /**
     * Método para agregar un nuevo usuario a la base de datos, la vista se convierte a la entidad antes de guardarla
     * @param userAppView vista a ser guardada en la base de datos, debe tener el username, la contraseña,
     *                    el nombre, el id del perfil y al menos un grupo y permiso.
     * @throws UserAppException
     * <br/>.1.- Si ocurre algún error  guardar en base de datos
     * <br/>2.- Si algún permiso viene con información nula
     * **/
    UserAppView createdUserApp(UserAppView userAppView) throws UserAppException;

    /**
     * Método para modificar un usuario en la base de datos, la vista se convierte a la entidad antes de modificarla
     * @param userAppView vista para modificar la informacion del usuario en la base de datos,
     *                    debe tener el username, la contraseña,
     *                    el nombre, el id del perfil y al menos un grupo y permiso.
     * @throws UserAppException
     * <br/>.1.- Si ocurre algún error  al modificar en base de datos
     * <br/>2.- Si algún permiso viene con información nula
     * **/
    void updateUserApp(UserAppView userAppView) throws UserAppException;

    /**
     * Método para modificar un usuario en la base de datos desde el movic, la vista se convierte a la entidad antes de modificarla
     * @param userAppView vista para modificar la informacion del usuario en la base de datos,
     *                    debe tener el username, la contraseña,
     *                    el nombre, el id del perfil y al menos un grupo y permiso.
     * @throws UserAppException
     * <br/>.1.- Si ocurre algún error  al modificar en base de datos
     * <br/>2.- Si algún permiso viene con información nula
     * **/
    void updateUserAppMovil(UserAppView userAppView) throws UserAppException;

    /**
     * Método para eliminar un usuario en la base de datos (modifica el estatus del usuario asi como
     * su información extra)
     * @param idUserApp id para modificar el estatus del usuario es requerido
     * @throws UserAppException
     * <br/>.1.- Si ocurre algún error  al eliminar en base de datos
     * <br/>2.- Si no se encuentra el usuario en la bd.
     * **/
    void deleteUserApp (Long idUserApp, String motivo) throws UserAppException;

    /**
     * Método para obtener los detalles de un usuario
     * @param idUserApp id para obtener los detalles del usuario es requerido
     * @throws UserAppException
     * <br/>.1.- Si ocurre algún error  al obtener usuario
     * <br/>2.- Si no se encuentra el usuario en la bd.
     * **/
    UserAppView getDetailsUserApp(Long idUserApp, Boolean image) throws UserAppException;

    /**
     * Método para buscar usuarios.
     * @param name nombre del usuario  requerido
     * @param page paginado de la lista requerido
     * @param size tamaño de la lista requerido
     * @param columnOrder ordenar columna
     * @param orderType tipo de orden
     * @throws UserAppException
     * <br/>.1.- Si ocurre algún error  al obtener usuarios
     * <br/>2.- Si no se encuentran el usuarios en la bd.
     * **/
    Page<UserAppPageView> findUsers(@NotNull String name, @NotNull Integer page, @NotNull  Integer size, @NotNull Long idGroup, String columnOrder, String orderType, List<Long> idUsersList) throws UserAppException;

    /**
     * Método para buscar usuarios cuando se es admin.
     * @param name nombre del usuario  requerido realmente es el search term
     * @param page paginado de la lista requerido
     * @param size tamaño de la lista requerido
     * @param columnOrder ordenar columna
     * @param orderType tipo de orden
     * @throws UserAppException
     * <br/>.1.- Si ocurre algún error  al obtener usuarios
     * <br/>2.- Si no se encuentran el usuarios en la bd.
     * Sre22052020 Agregado
     * **/
    Page<UserAppPageView> findUsersAdmin(@NotNull String name, @NotNull Integer page, @NotNull  Integer size, @NotNull Long idGroup,String columnOrder, String orderType, List<Long> idUsersList) throws UserAppException;
    
    Page<UserAppPageView> searchUsersByGroups(String name, Integer page, Integer size, String columnOrder, String orderType, List<Long> idGroups, List<Long> idUsers) throws UserAppException;

    /**
     * Método para obtener los detalles de un usuario utilizando el 'username' como filtro
     * @param username necesario para obtener los detalles de usuario
     * @return detalles de usuario seleccionado
     * @throws UserAppException si no es posible obtener usuario
     * */
    UserApp findByUsername(@NotNull String username) throws UserAppException;

    /**
     * Metodo para actualizar el estado de un usuario, saber si esta online u offline
     * @param status el estado a establecer Online,Offline
     * @param username Identificador de usuario
     * */
    void updateUsersConnectionStatus(String username,String status) throws UserAppException;

    List<UserAppView> findByGroups(List<Long> groups, List<Long> idUsers) throws UserAppException;

    /**
     * Metodo para para obtener todos los usuarios asignados al perfil
     * @param idProfile  informacion del perfil
     * @param groups  informacion de los grupos
     **/
    List<UsersAssignedView> getUsersAssignedToBoss(Long idProfile, List<Long> groups) throws UserAppException;


    List<InfoBasicResponseView> getInfoBasicUsers(InfoBasicRequestView view) throws UserAppException;

    void deleteRollBack(Long idUserApp) throws UserAppException;

    List<Long> obtenerIdUserAppDeGrupos(List<Integer> grupos) throws UserAppException;

    /**
     * Método para obtener los detalles en view de un usuario utilizando el 'username' como filtro
     * @param username necesario para obtener los detalles de usuario
     * @return detalles de usuario seleccionado as UserAppView
     * @throws UserAppException si no es posible obtener usuario
     * */
    UserAppView findViewByUsername(@NotNull String username) throws UserAppException;

    // GGR20200709 Agrego método para buscar horario por idUser
    Map<String, Object> findHorarioByIdUser(Long idUserApp) throws UserAppException;

    void resetUserPassword(ResetPasswordView resetPasswordView) throws UserAppException;
}
