package net.amentum.security.service;

import net.amentum.security.exception.ModuleException;
import net.amentum.security.views.ModuleView;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;

/**
 * Created by dev06 on 11/04/17.
 * Interface para administrar modulos y permisos de dichos modulos
 */
public interface ModuleService {

    /**
     * Método para agregar un nuevo modulo a la base de Datos
     * @param moduleView El módulo que se va a crear junto con sus permisos
     * @throws ModuleException <br/> 1.- Si la lista de permisos viene vacia <br/> 2.- Si ya existe un módulo con el mismo nombre
     * <br/> 3.- Si ocurre algún error en la base de datos y no es posible insertar el módulo
     * */
    void addModule(ModuleView moduleView) throws ModuleException;
    /**
     * Método para editar un módulo
     * @param moduleView El módulo que se va a editar
     * @throws ModuleException
     * <br/> 1.- Si la lista de permisos está vacia
     * <br/> 2.- Si no existe el módulo en la base de datos
     * <br/> 3.- Si no es posible eliminar el permiso de algún modulo
     * <br/> 4.- Si ocurre algún error en la base de datos
     * */
    void editModule(ModuleView moduleView) throws ModuleException;

    /**
     * Método para eliminar un módulo en base de datos utilizando el ID único
     * @param moduleId ID de módulo a eliminar
     * @throws ModuleException
     * <br/> 1.- Si el moulo que se desa eliminar no se encuentra en la base de datos
     * <br/> 2.- Si se trata de eliminar un módulo o permiso que tiene referencias con otras tablas
     * <br/> 3.- Si ocurre algún error con la conexión a la base de datos
     * */
    void deleteModule(Long moduleId) throws ModuleException;

    /**
     * Método para obtener el detalle de un módulo a través del ID
     * @param moduleId ID único a buscar en base de datos
     * @return detalles de módulo seleccionado junto con sus permisos
     * @throws ModuleException
     * <br/>1.- Si el módulo no se encuentra en base de datos
     * <br/>2.- Si ocurre algún error de conexión con la base de datos
     * **/
    ModuleView getModule(Long moduleId) throws ModuleException;

    /**
     * Metodo para obtener lista de módulos de forma pagianada, el filtrado se hace por ID de modulo, fecha de creación o nombre de módulo
     * @param name <strong>requerido</strong> valor para realizar filtrado, se utiliza un LIKE OR
     * @param page <strong>requerido</strong> pagina consultada
     * @param size <strong>requerido</strong> número máximo de resultados
     * @param columnOrder <strong>opcional</strong> columna por la cual se desea el ordenamiento, la columna por default es <strong>moduleId</strong>
     * @param orderType <strong>opcional</strong> tipo de ordenamiento ascendente (asc) o descendente (desc)- valor por default <strong>asc</strong>
     * @throws
     * */
    Page<ModuleView> getModules(@NotNull String name,@NotNull Integer page,@NotNull Integer size, String columnOrder, String orderType) throws ModuleException;
}
