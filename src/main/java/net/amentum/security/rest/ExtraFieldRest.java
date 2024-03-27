package net.amentum.security.rest;

import io.swagger.annotations.Api;
import net.amentum.common.BaseController;
import net.amentum.security.exception.ExceptionServiceCode;
import net.amentum.security.exception.ExtraFieldException;
import net.amentum.security.service.ExtraFieldService;
import net.amentum.security.views.ExtraFieldView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("fields")
@Api(description = "Campos extra para usuarios",produces = "application/json",consumes = "application/json")
public class ExtraFieldRest extends BaseController{

    private final Logger logger = LoggerFactory.getLogger(ExtraFieldRest.class);

    private ExtraFieldService fieldService;

    @Autowired
    public void setFieldService(ExtraFieldService fieldService) {
        this.fieldService = fieldService;
    }

    /**
     * Método para agregar un nuevo campo extra a un perfil de usuario, se realizan validaciones, no pueden haber dos campos con el mismo nombre y  perfil
     * @param view vista que sera parseada a una entidad y guardada en la base de datos
     * @throws ExtraFieldException si existen errores de validacion o acceso a datos
     * */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewField(@RequestBody @Validated ExtraFieldView view) throws ExtraFieldException{
        logger.info(ExceptionServiceCode.FEX+"- Agregar campo extra - {}",view);
        fieldService.addNewField(view);
    }
    /**
     * Método para editar campo extra, se realizan las siguientes validaciones: el campo debe existir en la base de datos y no puede haber dos campos con el mismo nombre y perfil
     * @param view vista que sera parseada a una entidad y sera editada en la base de datos
     * @param fieldId ID único de campo a editar
     * @throws ExtraFieldException si existe errores de validación o aaceso a datos
     * **/
    @RequestMapping(method = RequestMethod.PUT,value = "{fieldId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateField(@RequestBody  @Validated ExtraFieldView view,@PathVariable Long fieldId) throws ExtraFieldException{
        logger.info(ExceptionServiceCode.FEX+"- Editar campo extra - {} - {}",fieldId,view);
        view.setExtraFieldId(fieldId);
        fieldService.updateField(view);
    }


    /**
     * Método para eliminar un campo extra, se realizan las sig validaciones: El campo debe existir en BD
     * @param fieldId el ID del campo a eliminar
     * @throws ExtraFieldException si no es posible eliminar el campo debido a que tiene referencia en otras tablas o no existe en base de datos
     * */
    @RequestMapping(method = RequestMethod.DELETE,value = "{fieldId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteField(@PathVariable Long fieldId) throws ExtraFieldException{
        logger.info(ExceptionServiceCode.FEX+"- Eliminar campo extra - {}",fieldId);
        fieldService.deleteField(fieldId);
    }

    /**
     * Método para obtener un campo en base al ID, validaciones: El campo debe existir en  base de datos
     * @param fieldId el ID del campo a seleccionar
     * @throws ExtraFieldException si no se encuentra el ID en base de datos o no es posible realizar la selección
     * */
    @RequestMapping(method = RequestMethod.GET,value = "{fieldId}")
    @ResponseStatus(HttpStatus.OK)
    public ExtraFieldView findField(@PathVariable Long fieldId) throws ExtraFieldException{
        logger.info(ExceptionServiceCode.FEX+"- Obtener campo extra - {}",fieldId);
        return fieldService.findField(fieldId);
    }

    /**
     * Método para obtener la lista de campos, se puede filtrar por perfil y si estan o no activos, son opcionales
     * @param active si se desea se pueden obtener solo los campos activos o inactivos
     * @param profileId si se especifica se realiza un filtro por categoría
     * @throws ExtraFieldException si ocurre algún error al realizar la query en base de datos
     * */
    @RequestMapping(method = RequestMethod.GET,value = "byProfileOrActive")
    @ResponseStatus(HttpStatus.OK)
    public List<ExtraFieldView> findFields(@RequestParam(required = false) Long profileId, @RequestParam(required = false) Boolean active) throws ExtraFieldException{
        logger.info(ExceptionServiceCode.FEX+"- Obtener lista de campos por perfil o activos - {} - {}",profileId,active);
        return fieldService.findFields(profileId,active);
    }


    /**
     * Método para obtener la lista de campos de forma paginada
     * @param search opcional - debe especificarse en caso de que sea busqueda general
     * @param profileId opcional - Es posible filtrar por categoría en una busqueda avanzada
     * @param legend opcional - Es posible filtrar por legenda en caso de una busqueda avanzada
     * @param date1 opcional - Es posible filtrar por fecha de creacion - formato: dd-MM-yyyy
     * @param date2 opcional - Es posible filtrar por fecha de creacion - formato: dd-MM-yyyy -- para filtrar por fechas las dos deben venir no nulas
     * @param general opcional - Si es TRUE se realiza busqueda general en otro caso se realiza busqueda avanzada - default TRUE
     * @param columnOrder opcional - especificar la columna por la que se ordenarán los resultados
     * @param order opcional - Tipo de ordenamiento - asc o desc
     * @param page requerido - Pagina a obtener
     * @param size requerido - tamaño de resultados
     * */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<ExtraFieldView> findPage(@RequestParam(required = false,defaultValue = "") String search,
                                         @RequestParam(required = false) Long profileId,
                                         @RequestParam(required = false,defaultValue = "") String legend,
                                         @RequestParam(required = false,defaultValue = "") String date1,
                                         @RequestParam(required = false,defaultValue = "") String date2,
                                         @RequestParam(required = false,defaultValue = "true") Boolean general,
                                         @RequestParam(required = false,defaultValue = "extraFieldId") String columnOrder,
                                         @RequestParam(required = false,defaultValue = "asc") String order, @RequestParam Integer page, @RequestParam Integer size) throws ExtraFieldException{

        logger.info(ExceptionServiceCode.FEX+"- Obtener campos de forma paginada - search: {} - profileId: {} - legend: {} - date1: {} - date2: {} - general: {} - columnOrder: {} - order: {} - page: {} - size:{} ",
                search,profileId,legend,date1,date2,general,columnOrder,order,page,size);
        return fieldService.findPage(search,profileId,legend,date1,date2,general,columnOrder,order,page,size);
    }
}
