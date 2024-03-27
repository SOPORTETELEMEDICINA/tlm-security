package net.amentum.security.service;

import net.amentum.security.exception.ProfileException;
import net.amentum.security.model.Profile;
import net.amentum.security.views.NodeTree;
import net.amentum.security.views.ProfileView;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Victor de la Cruz
 */
public interface ProfileService {


    /**
     * Método para agregar un nuevo perfil a la base de datos, la vista se convierte a la entidad antes de guardarla
     * @param profileView vista a ser guardada en la base de datos, debe tener el nombre de perfil y al menos un permiso
     * @throws ProfileException
     * <br/>.1.- Si ocurre algún error  guardar en base de datos
     * <br/>2.- Si algún permiso viene con información nula
     * **/
    void addProfile(ProfileView profileView) throws ProfileException;

    /**
     * Método para editar los detalles y permisos de un perfil
     * @param profileView vista a ser editada, debe tener el nombre del perfil y al menos un permiso
     * @throws  ProfileException
     * <br/> 1.- Si el perfil no se encuentra en base de datos
     * <br/> 2.- Si ocurre algún error en base de datos
     * **/
    void editProfile(ProfileView profileView) throws ProfileException;

    /**
     * Método para eliminar un perfil a través del ID
     * @param profileId ID del perfil a eliminar, no debe ser nulo
     * @throws ProfileException
     * <br/> 1.- Si el perfil no se encuentra en base de datos
     * <br/> 2.- Si no  es posible eliminar el perfil de la base de datos
     * */
    void deleteProfile(@NotNull Long profileId) throws  ProfileException;

    /**
     * Método para obtener el arbol de permisos que se tiene en base de datos
     * @param profileId si el parametro es nulo, no busca los permisos que tenga el perfil, solo obtiene el arbol general<br/>
     * si el parametro es no nulo, busca los permisos del perfil para revisar cuales tiene asignados
     * @throws ProfileException si no es posible obtener la lista de permisos en la base de datos
     * @return lista de permisos
     * */
    List<NodeTree> getProfileTree(Long profileId) throws ProfileException;

    /**
     * Método para obtener los detalles de un perfil junto con el arbol de permisos que tiene
     * @param profileId parametro no nulo para buscar el perfil en base de datos
     * @throws ProfileException
     * <br/> 1.- Si el perfil no se encuentra en base de datos
     * <br/> 2.- Si no es posible obtener el arbol de permisos del perfil
     * <br/> 3.- Si ocurre algún error con la conexion a la base de datos
     * */
    ProfileView getProfile(@NotNull Long profileId) throws ProfileException;

    /**
     * Método para obtener de forma paginada la lista de servicios
     * @param name valor para filtrar por nombre de perfil
     * @param page numero de pagina que se desea obtener desde 0 -> n , <strong>0</strong> pagina default
     * @param size número de resultados que se desean por pagina, <strong>10</strong> número de resultados por default
     * @param columnOrder columna por la cual realizar ordenamiento, <strong>profileId</strong> columna default
     * @param orderType tipo de ordenamiento ('asc','desc') <strong>asc</strong> ordenamiento defautl
     * @throws ProfileException si ocurre un error a la hora de realizar la consulta en la base de datos
     * @return Pagina con los resultados obtenidos
     * */
    Page<Profile> findProfiles(@NotNull String name,@NotNull Integer page,@NotNull Integer size,String columnOrder,String orderType) throws ProfileException;

    /**
     * Método para obtener lista completa de perfiles sin paginar
     * @return lista de perfiles
     * @throws ProfileException si ocurre algún error al seleccionar lista de perfiles
     * */
    List<ProfileView> findAll() throws ProfileException;
}
