package net.amentum.security.service.impl;

import net.amentum.common.GenericException;
import net.amentum.security.Utils;
import net.amentum.security.converter.GroupConverter;
import net.amentum.security.exception.ExceptionServiceCode;
import net.amentum.security.exception.GroupException;
import net.amentum.security.model.Group;
import net.amentum.security.model.GroupHasCategory;
import net.amentum.security.persistence.GroupHasCategoryRepository;
import net.amentum.security.persistence.GroupHasTypeTicketRepository;
import net.amentum.security.persistence.GroupRepository;
import net.amentum.security.service.GroupService;
import net.amentum.security.views.GroupView;
import org.apache.http.annotation.Obsolete;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 * */
@Service
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService {


    private final Logger logger = LoggerFactory.getLogger(GroupService.class);

    private final Map<String,Object> colOrderNames = new HashMap<>();
    {
        colOrderNames.put("groupId","groupId");
        colOrderNames.put("groupName","groupName");
        colOrderNames.put("active","active");
        colOrderNames.put("createdDate","createdDate");
    }

    private GroupRepository groupRepository;

    private GroupConverter groupConverter;

    private GroupHasCategoryRepository groupHasCategoryRepository;

    private GroupHasTypeTicketRepository groupHasTypeTicketRepository;

    @Autowired
    public void setGroupHasTypeTicketRepository(GroupHasTypeTicketRepository groupHasTypeTicketRepository) {
        this.groupHasTypeTicketRepository = groupHasTypeTicketRepository;
    }

    @Autowired
    public void setGroupHasCategoryRepository(GroupHasCategoryRepository groupHasCategoryRepository) {
        this.groupHasCategoryRepository = groupHasCategoryRepository;
    }

    @Autowired
    public void setGroupConverter(GroupConverter groupConverter) {
        this.groupConverter = groupConverter;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    @Transactional(readOnly = false,rollbackFor = {GroupException.class})
    public void addGroup(GroupView group) throws GroupException {
        try{
            Group g = groupConverter.convertToEntity(group, null);
            groupRepository.save(g);
        }catch (DataIntegrityViolationException e){
            GroupException groupException = new GroupException("Error al insertar grupo, el nombre del grupo ya esta en la base de datos",GroupException.LAYER_DAO,GroupException.ACTION_INSERT);
            groupException.addError("El nombre: "+group.getGroupName()+" ya se encuentra en uso");
            logger.error(ExceptionServiceCode.GROUP+"- Error al insertar grupo - grupo: {} - CODE: {}",group,groupException.getExceptionCode(),e);
            throw groupException;
        }catch (Exception ex){
            GroupException groupException = new GroupException(ex,"Error al insertar grupo",GroupException.LAYER_SERVICE,GroupException.ACTION_INSERT);
            logger.error(ExceptionServiceCode.GROUP+"- Error al insertar grupo - grupo: {} - CODE: {}",group,groupException.getExceptionCode(),ex);
            throw groupException;
        }
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    @Transactional(readOnly = false,rollbackFor = {GroupException.class})
    public void editGroup(GroupView group) throws GroupException {
        try{
            if(!groupRepository.exists(group.getGroupId())){
                GroupException groupException = new GroupException("El grupo que trata de editar no existe",GroupException.LAYER_SERVICE,GroupException.ACTION_VALIDATE);
                groupException.addError("Grupo no encontrado");
                throw  groupException;
            }
            Group groupEntity = groupRepository.findOne(group.getGroupId());
            logger.info("Group: {}", groupEntity);

            groupEntity.getCategoryList().forEach(groupHasCategory -> {
                groupHasCategoryRepository.delete(groupHasCategory);
            });

            groupEntity.getTypeTicketList().forEach(groupHasTypeTicket -> {
                groupHasTypeTicketRepository.delete(groupHasTypeTicket);
            });

            groupEntity.setCategoryList(new ArrayList<>());
            groupEntity.setTypeTicketList(new ArrayList<>());
            groupRepository.flush();
            groupRepository.save(groupConverter.convertToEntity(group, groupEntity));
        }catch (GroupException e){
            throw  e;
        }catch (Exception ex){
            GroupException groupException = new GroupException("Ocurrio un error al tratar de editar el grupo",GroupException.LAYER_DAO,GroupException.ACTION_UPDATE);
            logger.error(ExceptionServiceCode.GROUP+"- Error al editar grupo - grupo: {} - CODE: {}",group,groupException.getExceptionCode(),ex);
            throw  groupException;
        }
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    @Transactional(readOnly = false,rollbackFor = {GroupException.class})
    public void deleteGroup(Long groupId) throws GroupException {
        try{
            if(!groupRepository.exists(groupId)){
                throw  new GroupException("EL grupo que desea eliminar no se encuentra en base de datos",GroupException.LAYER_DAO, GenericException.ACTION_VALIDATE);
            }
            groupRepository.delete(groupId);
        }catch (GroupException gr){
            throw  gr;
        }catch (DataIntegrityViolationException dae){
            GroupException groupException = new GroupException("No es posible eliminar el Grupo seleccionado",GenericException.LAYER_DAO,GenericException.ACTION_DELETE);
            groupException.addError("Error al tratar de eliminaar el grupo: "+groupId);
            logger.error(ExceptionServiceCode.GROUP+" - No es posible eliminar el grupo: {} - CODE: {} - Se tienen referecnias a otras tablas: {}",groupId,groupException.getExceptionCode(),dae.getCause().getMessage());
            throw  groupException;
        }catch (Exception ex){
            GroupException groupException = new GroupException("Ocurrio un error al elimianr grupo",GenericException.LAYER_SERVICE,GenericException.ACTION_DELETE);
            logger.error(ExceptionServiceCode.GROUP+"- Error al tratar de eliminar el grupo: {} - CODE: {}",groupId,groupException.getExceptionCode(),ex);
            throw  groupException;
        }
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public GroupView findGroup(Long groupId) throws GroupException {
        try{
            if(!groupRepository.exists(groupId)){
                throw  new GroupException("EL grupo no se encuentra en base de datos",GroupException.LAYER_DAO, GenericException.ACTION_VALIDATE);
            }
            //return groupRepository.findOne(groupId);
            return groupConverter.convertToView(groupRepository.findOne(groupId));
        }catch (GroupException gr){
            throw  gr;
        }catch (Exception ex){
            GroupException groupException = new GroupException("Ocurrio un error al seleccionar grupo",GenericException.LAYER_SERVICE,GenericException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP+"- Error al tratar de seleccionar el grupo: {} - CODE: {}",groupId,groupException.getExceptionCode(),ex);
            throw  groupException;
        }
    }


    /**
     * {@inheritDoc}
     * */
    @Override
    public Page<GroupView> findGroups(Boolean active, String name, Integer page, Integer size, String columnOrder, String orderType) throws GroupException {
        try{
            List<GroupView> groupViewList = new ArrayList<>();
            Page<Group> groupList = null;
            Sort sort = new Sort(Sort.Direction.ASC,(String)colOrderNames.get("groupId"));

            if(columnOrder!=null && orderType!=null){
                if(orderType.equalsIgnoreCase("asc"))
                    sort = new Sort(Sort.Direction.ASC,(String)colOrderNames.get(columnOrder));
                else
                    sort = new Sort(Sort.Direction.DESC,(String)colOrderNames.get(columnOrder));
            }
            PageRequest request = new PageRequest(page,size,sort);
            if(null!=active)
                groupList = groupRepository.findAllGroupActiveByNameLike(Utils.getPatternLike(name),active,request);
            else
                groupList = groupRepository.findAllGroupByNameLike(Utils.getPatternLike(name),request);

            groupList.getContent().forEach(group -> {
                groupViewList.add(groupConverter.convertToView(group));
            });
            PageImpl<GroupView> pageGroup = new PageImpl<GroupView>(groupViewList, request, groupList.getTotalElements());
            return pageGroup;
        }catch (Exception ex){
            GroupException groupException = new GroupException("Ocurrio un error al seleccionar lista de grupos",GenericException.LAYER_SERVICE,GenericException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP+"- Error al tratar de seleccionar lista de grupos - CODE: {}",groupException.getExceptionCode(),ex);
            throw  groupException;
        }
    }

    @Override
    public List<GroupView> findAll(Boolean active) throws GroupException {
        try{
            List<GroupView> groupViewList = new ArrayList<>();
            List<Group> groupList = new ArrayList<>();
            if(active==null){
                groupList = groupRepository.findAll();
            }else{
                groupList = groupRepository.findAll().stream().filter(group -> group.getActive().compareTo(active)==0).collect(Collectors.toList());
            }
            for (Group g:groupList) {
                groupViewList.add(groupConverter.convertToView(g));
            }
            return groupViewList;
        }catch (Exception ex){
            GroupException groupException = new GroupException("Ocurrio un error al seleccionar lista de grupos",GenericException.LAYER_SERVICE,GenericException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP+"- Error al tratar de seleccionar lista de grupos - CODE: {}",groupException.getExceptionCode(),ex);
            throw  groupException;
        }
    }

    /*
    // GGR20200610 Obtener imagen del grupo principal (Deberian ser uno mismo)
    @Override
    public String findImageGroup(Long idUserApp) throws GroupException {
        try {
            return groupRepository.findImageGroup(idUserApp);
        } catch (Exception ex) {
            GroupException groupException = new GroupException("Ocurrio un error al seleccionar grupo findImageGroup",GenericException.LAYER_SERVICE,GenericException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP+"- Error en findImageGroup principal {} - CODE: {}",idUserApp,groupException.getExceptionCode(),ex);
            ex.printStackTrace();
            throw  groupException;
        }
    }

    // GGR20200612 Obtener el nombre del grupo principal (Deberian ser uno mismo)
    @Override
    public String findNameMainGroup(Long idUserApp) throws GroupException {
        try {
            return groupRepository.findNameMainGroup(idUserApp);
        } catch (Exception ex) {
            GroupException groupException = new GroupException("Ocurrio un error al seleccionar grupo findImageGroup",GenericException.LAYER_SERVICE,GenericException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP+"- Error en findImageGroup principal {} - CODE: {}",idUserApp,groupException.getExceptionCode(),ex);
            ex.printStackTrace();
            throw  groupException;
        }
    }

    // GGR20200612 Obtener Id del grupo principal (Deberian ser uno mismo)
    @Override
    public Long findIdMainGroup(Long idUserApp) throws GroupException {
        try {
            return groupRepository.findIdMainGroup(idUserApp);
        } catch (Exception ex) {
            GroupException groupException = new GroupException("Ocurrio un error al seleccionar grupo findImageGroup",GenericException.LAYER_SERVICE,GenericException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP+"- Error en findImageGroup principal {} - CODE: {}",idUserApp,groupException.getExceptionCode(),ex);
            ex.printStackTrace();
            throw  groupException;
        }
    }

     */

}
