package net.amentum.security.rest;

import net.amentum.common.BaseController;
import net.amentum.security.exception.ExceptionServiceCode;
import net.amentum.security.exception.ModuleException;
import net.amentum.security.service.ModuleService;
import net.amentum.security.views.ModuleView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by dev06 on 11/04/17.
 */
@RestController
@RequestMapping("modules")
public class ModuleRest extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(ModuleRest.class);

    @Autowired
    private ModuleService moduleService;


    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addModule(@Validated @RequestBody ModuleView moduleView) throws ModuleException{
        logger.info(ExceptionServiceCode.MODULE+"- Crear nuevo módulo: {}",moduleView);
        moduleService.addModule(moduleView);
    }



    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<ModuleView> getModulePage(@RequestParam(required = false)String name, @RequestParam(required = false)Integer page, @RequestParam(required=false)Integer size,@RequestParam(required = false)
            String orderColumn,@RequestParam(required = false)String orderType) throws ModuleException{
        logger.info(ExceptionServiceCode.MODULE+"- Obtener listado de modulos: {} - page {} - size: {} - orderColumn: {} - orderType: {}",name,page,size,orderColumn,orderType);
        if(page==null)
            page = 0;
        if(size==null)
            size = 10;
        return moduleService.getModules(name != null ? name : "",page,size,orderColumn,orderType);
    }


    @RequestMapping(value = "{moduleId}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModuleView getModuleDetail(@PathVariable()Long moduleId) throws ModuleException{
        logger.info(ExceptionServiceCode.MODULE+"- Obtener detalles de módulo: "+moduleId);
        return moduleService.getModule(moduleId);
    }


    @RequestMapping(value = "{moduleId}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteModule(@PathVariable()Long moduleId) throws ModuleException{
        logger.info(ExceptionServiceCode.MODULE+"- Eliminar módulo de base de datos: {}",moduleId);
        moduleService.deleteModule(moduleId);
    }


    @RequestMapping(value = "{moduleId}",method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public void updateModule(@Validated @RequestBody ModuleView moduleView,@PathVariable()Long moduleId) throws ModuleException{
        moduleView.setModuleId(moduleId);
        logger.info(ExceptionServiceCode.MODULE+"- Actualizar módulo: {}",moduleView);
        moduleService.editModule(moduleView);
    }
}
