package net.amentum.security.rest;

import io.swagger.annotations.Api;
import net.amentum.common.BaseController;
import net.amentum.security.exception.ExceptionServiceCode;
import net.amentum.security.exception.GroupException;
import net.amentum.security.model.Group;
import net.amentum.security.service.GroupService;
import net.amentum.security.views.GroupView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author dev06
 */
@RestController
@Api(value = "Grupos",description = "Servicio para crear grupos de usuarios")
@RequestMapping("groups")
public class GroupRest extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(GroupRest.class);

    private GroupService groupService;

    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }


    //@PreAuthorize(value = "hasRole('ROLE_CREATE_GROUP')")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addGroup(@RequestBody @Validated GroupView group) throws GroupException{
        logger.info(ExceptionServiceCode.GROUP+"- Crear nuevo grupo: {}",group.toStringResume());
        logger.debug(ExceptionServiceCode.GROUP+" - Crear nuevo grupo: {}",group);
        groupService.addGroup(group);
    }


    @RequestMapping(value = "{groupId}",method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void editGroup(@RequestBody @Validated GroupView group,@PathVariable()Long groupId) throws GroupException{
        logger.info(ExceptionServiceCode.GROUP+"- Editar grupo - ID: {} - grupo: {}",groupId,group);
        group.setGroupId(groupId);
        groupService.editGroup(group);
    }


    @RequestMapping(value = "{groupId}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteGroup(@PathVariable()Long groupId) throws GroupException{
        logger.info(ExceptionServiceCode.GROUP+"- Eliminar grupo, ID: {}",groupId);
        groupService.deleteGroup(groupId);
    }


    @RequestMapping(value = "{groupId}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public GroupView findGroup(@PathVariable()Long groupId) throws GroupException{
        logger.info(ExceptionServiceCode.GROUP+"- Obtener grupo, ID: {}",groupId);
        return groupService.findGroup(groupId);
    }


    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<GroupView> findGroups(@RequestParam(required = false)Boolean active,@RequestParam(required = false)String name, @RequestParam(required = false)Integer page, @RequestParam(required=false)Integer size,@RequestParam(required = false)
            String orderColumn,@RequestParam(required = false)String orderType) throws GroupException{
        logger.info(ExceptionServiceCode.GROUP+"- Obtener listado de modulos: {} - page {} - size: {} - orderColumn: {} - orderType: {} - active: {}",name,page,size,orderColumn,orderType,active);
        if(page==null)
            page = 0;
        if(size==null)
            size = 10;
        return groupService.findGroups(active,name != null ? name : "",page,size,orderColumn,orderType);
    }


    @RequestMapping(value = "all",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<GroupView> findAll(@RequestParam(required = false)Boolean active) throws GroupException{
        return groupService.findAll(active);
    }
}
