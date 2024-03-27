package net.amentum.security.service;

import net.amentum.security.exception.ExtraFieldException;
import net.amentum.security.views.ExtraFieldView;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *  @author Victor de la Cruz
 * */
public interface ExtraFieldService {


    /**
     * Método para agregar un nuevo campo extra a un perfil de usuario, se realizan validaciones, no pueden haber dos campos con el mismo nombre y  perfil
     * @param view vista que sera parseada a una entidad y guardada en la base de datos
     * @throws ExtraFieldException si existen errores de validacion o acceso a datos
     * */
    void addNewField(@NotNull ExtraFieldView view) throws ExtraFieldException;
    /**
     * Método para editar campo extra, se realizan las siguientes validaciones: el campo debe existir en la base de datos y no puede haber dos campos con el mismo nombre y perfil
     * @param view vista que sera parseada a una entidad y sera editada en la base de datos
     * @throws ExtraFieldException si existe errores de validación o aaceso a datos
     * **/
    void updateField(@NotNull ExtraFieldView view) throws ExtraFieldException;

    /**
     * Método para eliminar un campo extra, se realizan las sig validaciones: El campo debe existir en BD
     * @param fieldId el ID del campo a eliminar
     * @throws ExtraFieldException si no es posible eliminar el campo debido a que tiene referencia en otras tablas o no existe en base de datos
     * */
    void deleteField(@NotNull Long fieldId) throws ExtraFieldException;

    /**
     * Método para obtener un campo en base al ID, validaciones: El campo debe existir en  base de datos
     * @param fieldId el ID del campo a seleccionar
     * @throws ExtraFieldException si no se encuentra el ID en base de datos o no es posible realizar la selección
     * */
    ExtraFieldView findField(@NotNull Long fieldId) throws ExtraFieldException;

    /**
     * Método para obtener la lista de campos, se puede filtrar por perfil y si estan o no activos, son opcionales
     * @param active si se desea se pueden obtener solo los campos activos o inactivos
     * @param profileId si se especifica se realiza un filtro por categoría
     * @throws ExtraFieldException si ocurre algún error al realizar la query en base de datos
     * */
    List<ExtraFieldView> findFields(Long profileId, Boolean active) throws ExtraFieldException;


    /**
     * Método para obtener la lista de campos de forma paginada
     * @param search opcional - debe especificarse en caso de que sea busqueda general
     * @param profileId opcional - Es posible filtrar por categoría en una busqueda avanzada
     * @param legend opcional - Es posible filtrar por legenda en caso de una busqueda avanzada
     * @param date1 opcional - Es posible filtrar por fecha de creacion - formato: dd-MM-yyyy
     * @param date2 opcional - Es posible filtrar por fecha de creacion - formato: dd-MM-yyyy -- para filtrar por fechas las dos deben venir no nulas
     * @param general requerido - Si es TRUE se realiza busqueda general en otro caso se realiza busqueda avanzada
     * @param columnOrder opcional - especificar la columna por la que se ordenarán los resultados
     * @param order opcional - Tipo de ordenamiento - asc o desc
     * @param page requerido - Pagina a obetner
     * @param size requerido - tamaño de resultados
     * */
    Page<ExtraFieldView> findPage(String search, Long profileId, String legend, String date1, String date2, Boolean general,
                                  String columnOrder, String order, @NotNull Integer page, @NotNull Integer size) throws ExtraFieldException;
}
