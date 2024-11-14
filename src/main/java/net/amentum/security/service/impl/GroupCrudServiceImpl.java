package net.amentum.security.service.impl;

import net.amentum.common.GenericException;
import net.amentum.security.converter.GroupCrudConverter;
import net.amentum.security.exception.ExceptionServiceCode;
import net.amentum.security.exception.GroupCrudException;
import net.amentum.security.model.GroupCrud;
import net.amentum.security.persistence.GroupCrudRepository;
import net.amentum.security.service.GroupCrudService;
import net.amentum.security.views.GroupCrudView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class GroupCrudServiceImpl implements GroupCrudService {

    private final Logger logger = LoggerFactory.getLogger(GroupCrudService.class);

    private final Map<String,Object> colOrderNames = new HashMap<>();
    {
        colOrderNames.put("id","id");
        colOrderNames.put("groupId","groupId");
        colOrderNames.put("color","color");
    }

    private GroupCrudRepository groupCrudRepository;

    private GroupCrudConverter groupCrudConverter;

    @Autowired
    public void setGroupCrudConverter(GroupCrudConverter groupCrudConverter) {
        this.groupCrudConverter = groupCrudConverter;
    }

    @Autowired
    public void setGroupCrudRepository(GroupCrudRepository groupCrudRepository) {
        this.groupCrudRepository = groupCrudRepository;
    }

    @Override
    @Transactional(readOnly = false,rollbackFor = {GroupCrudException.class})
    public void addGroupCrud(GroupCrudView groupCrud) throws GroupCrudException {
        try{
            GroupCrud g = groupCrudConverter.toEntity(groupCrud);
            groupCrudRepository.save(g);
        }catch (Exception ex){
            GroupCrudException groupCrudException = new GroupCrudException(ex,"Error al insertar grupo",GroupCrudException.LAYER_SERVICE,GroupCrudException.ACTION_INSERT);
            logger.error("{}- Error al insertar grupo - grupo: {} - CODE: {}", ExceptionServiceCode.GROUP, groupCrud, groupCrudException.getExceptionCode(), ex);
            throw groupCrudException;
        }
    }

    @Override
    @Transactional(readOnly = false,rollbackFor = {GroupCrudException.class})
    public void editGroupCrud(GroupCrudView groupCrud) throws GroupCrudException {
        try{
            if(!groupCrudRepository.exists(groupCrud.getIdGroupCrud())){
                GroupCrudException groupCrudException = new GroupCrudException("El grupo que trata de editar no existe",GroupCrudException.LAYER_SERVICE,GroupCrudException.ACTION_VALIDATE);
                groupCrudException.addError("Grupo no encontrado");
                throw  groupCrudException;
            }
            GroupCrud groupCrudEntity = groupCrudRepository.findOne(groupCrud.getIdGroupCrud());
            logger.info("Group: {}", groupCrudEntity);

            groupCrudRepository.flush();
            groupCrudRepository.save(groupCrudConverter.toEntity(groupCrud));
        }catch (GroupCrudException e){
            throw  e;
        }catch (Exception ex){
            GroupCrudException groupCrudException = new GroupCrudException("Ocurrio un error al tratar de editar el grupo",GroupCrudException.LAYER_DAO,GroupCrudException.ACTION_UPDATE);
            logger.error("{}- Error al editar grupo - grupo: {} - CODE: {}", ExceptionServiceCode.GROUP, groupCrud, groupCrudException.getExceptionCode(), ex);
            throw  groupCrudException;
        }
    }

    @Override
    @Transactional(readOnly = false,rollbackFor = {GroupCrudException.class})
    public void deleteGroupCrud(Long groupCrudId) throws GroupCrudException {
        try{
            if(!groupCrudRepository.exists(groupCrudId)){
                throw  new GroupCrudException("EL grupo que desea eliminar no se encuentra en base de datos",GroupCrudException.LAYER_DAO, GenericException.ACTION_VALIDATE);
            }
            groupCrudRepository.delete(groupCrudId);
        }catch (GroupCrudException gr){
            throw  gr;
        }catch (DataIntegrityViolationException dae){
            GroupCrudException groupCrudException = new GroupCrudException("No es posible eliminar el Grupo seleccionado",GenericException.LAYER_DAO,GenericException.ACTION_DELETE);
            groupCrudException.addError("Error al tratar de eliminaar el grupo: "+groupCrudId);
            logger.error("{} - No es posible eliminar el grupo: {} - CODE: {} - Se tienen referecnias a otras tablas: {}", ExceptionServiceCode.GROUP, groupCrudId, groupCrudException.getExceptionCode(), dae.getCause().getMessage());
            throw  groupCrudException;
        }catch (Exception ex){
            GroupCrudException groupCrudException = new GroupCrudException("Ocurrio un error al elimianr grupo",GenericException.LAYER_SERVICE,GenericException.ACTION_DELETE);
            logger.error("{}- Error al tratar de eliminar el grupo: {} - CODE: {}", ExceptionServiceCode.GROUP, groupCrudId, groupCrudException.getExceptionCode(), ex);
            throw  groupCrudException;
        }
    }

    @Override
    public Page<GroupCrudView> findGroupCruds(Long gid, Integer page, Integer size, String columnOrder, String orderType) throws GroupCrudException {
        try{
            List<GroupCrudView> groupCrudViewList = new ArrayList<>();
            Page<GroupCrud> groupCrudList = null;
            Sort sort = new Sort(Sort.Direction.ASC,(String)colOrderNames.get("id"));

            if(columnOrder!=null && orderType!=null){
                if(orderType.equalsIgnoreCase("asc"))
                    sort = new Sort(Sort.Direction.ASC,(String)colOrderNames.get(columnOrder));
                else
                    sort = new Sort(Sort.Direction.DESC,(String)colOrderNames.get(columnOrder));
            }
            PageRequest request = new PageRequest(page,size,sort);

            groupCrudList = groupCrudRepository.findAllGroupCrudByGroup(gid,request);

            groupCrudList.getContent().forEach(groupCrud -> groupCrudViewList.add(groupCrudConverter.toView(groupCrud)));

            return new PageImpl<GroupCrudView>(groupCrudViewList, request, groupCrudList.getTotalElements());
        }catch (Exception ex){
            GroupCrudException groupCrudException = new GroupCrudException("Ocurrio un error al seleccionar lista de grupos",GenericException.LAYER_SERVICE,GenericException.ACTION_SELECT);
            logger.error("{}- Error al tratar de seleccionar lista de grupos - CODE: {}", ExceptionServiceCode.GROUP, groupCrudException.getExceptionCode(), ex);
            throw  groupCrudException;
        }
    }

    @Override
    public List<GroupCrudView> findAll() throws GroupCrudException {
        try{
            List<GroupCrudView> groupViewList = new ArrayList<>();
            List<GroupCrud> groupCrudList = new ArrayList<>();
                groupCrudList = groupCrudRepository.findAll();
            for (GroupCrud g:groupCrudList) {
                groupViewList.add(groupCrudConverter.toView(g));
            }
            return groupViewList;
        }catch (Exception ex){
            GroupCrudException groupCrudException = new GroupCrudException("Ocurrio un error al seleccionar lista de grupos",GenericException.LAYER_SERVICE,GenericException.ACTION_SELECT);
            logger.error("{}- Error al tratar de seleccionar lista de grupos - CODE: {}", ExceptionServiceCode.GROUP, groupCrudException.getExceptionCode(), ex);
            throw  groupCrudException;
        }
    }

    @Override
    public String findImageGroupCrud(Long idGroup, String color) throws GroupCrudException {
        try {
            return groupCrudRepository.findImageGroup(idGroup, color);
        } catch (Exception ex) {
            GroupCrudException groupCrudException = new GroupCrudException("Ocurrio un error al seleccionar grupo findImageGroup",GenericException.LAYER_SERVICE,GenericException.ACTION_SELECT);
            logger.error("{}- Error en findImageGroup principal {} - CODE: {}", ExceptionServiceCode.GROUP, idGroup, groupCrudException.getExceptionCode(), ex);
            throw  groupCrudException;
        }
    }


    @Override
    public GroupCrudView findGroupCrud(Long groupCrudId) throws GroupCrudException {
        try{
            if(!groupCrudRepository.exists(groupCrudId)){
                throw  new GroupCrudException("EL grupo no se encuentra en base de datos",GroupCrudException.LAYER_DAO, GenericException.ACTION_VALIDATE);
            }
            return groupCrudConverter.toView(groupCrudRepository.findOne(groupCrudId));
        }catch (GroupCrudException gr){
            throw  gr;
        }catch (Exception ex){
            GroupCrudException groupCrudException = new GroupCrudException("Ocurrio un error al seleccionar grupo",GenericException.LAYER_SERVICE,GenericException.ACTION_SELECT);
            logger.error("{}- Error al tratar de seleccionar el grupo: {} - CODE: {}", ExceptionServiceCode.GROUP, groupCrudId, groupCrudException.getExceptionCode(), ex);
            throw  groupCrudException;
        }
    }
}
