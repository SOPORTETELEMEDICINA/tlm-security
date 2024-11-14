package net.amentum.security.rest;

import io.swagger.annotations.Api;
import net.amentum.security.exception.ExceptionServiceCode;
import net.amentum.security.exception.GroupCrudException;
import net.amentum.security.service.GroupCrudService;
import net.amentum.security.views.GroupCrudView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "Grupos Crud")
@RequestMapping("groups-crud")

public class GroupCrudRest {
    private final Logger logger = LoggerFactory.getLogger(GroupRest.class);

    private GroupCrudService groupCrudService;

    @Autowired
    public void setGroupCrudService(GroupCrudService groupCrudService) {
        this.groupCrudService = groupCrudService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addGroup(@RequestBody GroupCrudView group) throws GroupCrudException {
        logger.info("{}- Crear nuevo grupo: {}", ExceptionServiceCode.GROUP, group.toString());
        logger.debug("{} - Crear nuevo grupo: {}", ExceptionServiceCode.GROUP, group);
        groupCrudService.addGroupCrud(group);
    }

    @RequestMapping(value = "{groupCrudId}",method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void editGroup(@RequestBody @Validated GroupCrudView group,@PathVariable()Long groupCrudId) throws GroupCrudException{
        logger.info("{}- Editar grupo - ID: {} - grupo: {}", ExceptionServiceCode.GROUP, groupCrudId, group);
        group.setIdGroupCrud(groupCrudId);
        groupCrudService.editGroupCrud(group);
    }


    @RequestMapping(value = "{groupCrudId}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteGroup(@PathVariable()Long groupCrudId) throws GroupCrudException{
        logger.info("{}- Eliminar grupo, ID: {}", ExceptionServiceCode.GROUP, groupCrudId);
        groupCrudService.deleteGroupCrud(groupCrudId);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<GroupCrudView> findGroups(@RequestParam(required = false) Long gid, @RequestParam(required = false)Integer page, @RequestParam(required=false)Integer size, @RequestParam(required = false)
    String orderColumn, @RequestParam(required = false)String orderType) throws GroupCrudException{
        logger.info("{}- Obtener listado de modulos: {} - page {} - size: {} - orderColumn: {} - orderType: {}", ExceptionServiceCode.GROUP, gid, page, size, orderColumn, orderType);
        if(page==null)
            page = 0;
        if(size==null)
            size = 10;
        return groupCrudService.findGroupCruds(gid ,page,size,orderColumn,orderType);
    }


    @RequestMapping(value = "all",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<GroupCrudView> findAll() throws GroupCrudException{
        return groupCrudService.findAll();
    }

    @RequestMapping(value = "image",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String findImageGroupCrud(@RequestParam(required = true)Long gid,@RequestParam(required = true)String color) throws GroupCrudException{
        return groupCrudService.findImageGroupCrud(gid,color);
    }

    @RequestMapping(value = "{groupCrudId}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public GroupCrudView findGroup(@PathVariable()Long groupCrudId) throws GroupCrudException{
        logger.info(ExceptionServiceCode.GROUP+"- Obtener grupo, ID: {}",groupCrudId);
        return groupCrudService.findGroupCrud(groupCrudId);
    }
}
