package net.amentum.security.service.impl;

import net.amentum.security.Utils;
import net.amentum.security.exception.ExceptionServiceCode;
import net.amentum.security.exception.ProfileException;
import net.amentum.security.model.Module;
import net.amentum.security.model.ModulePermission;
import net.amentum.security.model.Profile;
import net.amentum.security.model.ProfilePermission;
import net.amentum.security.persistence.ModuleRepository;
import net.amentum.security.persistence.ProfilePermissionRepository;
import net.amentum.security.persistence.ProfileRepository;
import net.amentum.security.service.ProfileService;
import net.amentum.security.views.NodeTree;
import net.amentum.security.views.PermissionView;
import net.amentum.security.views.ProfileView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dev06
 */
@Service
@Transactional(readOnly = true)
public class ProfileServiceImpl implements ProfileService {

    private final Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);


    private ProfileRepository profileRepository;

    

    private ProfilePermissionRepository profilePermissionRepository;

    private ModuleRepository moduleRepository;

    @Autowired
    public void setProfileRepository(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Autowired
    public void setProfilePermissionRepository(ProfilePermissionRepository profilePermissionRepository) {
        this.profilePermissionRepository = profilePermissionRepository;
    }

    @Autowired
    public void setModuleRepository(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    private final Map<String,Object> colOrderNames = new HashMap<>();
    {
        colOrderNames.put("profileId","profileId");
        colOrderNames.put("profileName","profileName");
        colOrderNames.put("active","active");
        colOrderNames.put("createdDate","createdDate");
    }


    @Transactional(readOnly = false,rollbackFor = {ProfileException.class})
    @Override
    /**
     * {@inheritDoc}
     * */
    public void addProfile(ProfileView profileView) throws ProfileException {
        try{
            Profile profile = convertViewToEntity(profileView, null,Boolean.FALSE);
            logger.debug("Insertar nuevo perfil: {}",profile);
            profileRepository.save(profile);
        }catch (DataIntegrityViolationException dte){
            ProfileException pe = new ProfileException("No fue posible agregar el perfil",ProfileException.LAYER_DAO,ProfileException.ACTION_INSERT);
            pe.addError("Ocurrio un error al agregar perfil");
            logger.error("Error al insertar nuevo perfil - CODE: {} - {}",pe.getExceptionCode(),profileView,dte);
            throw  pe;
        }catch (Exception ex){
            ProfileException pe = new ProfileException("Error inesperado al agregar el perfil",ProfileException.LAYER_DAO,ProfileException.ACTION_INSERT);
            pe.addError("Ocurrio un error al agregar perfil");
            logger.error("Error al insertar nuevo perfil - CODE: {} - {}",pe.getExceptionCode(),profileView,ex);
            throw  pe;
        }
    }

    @Transactional(readOnly = false,rollbackFor = {ProfileException.class})
    @Override
    /**
     * {@inheritDoc}
     * */
    public void editProfile(ProfileView profileView) throws ProfileException {
        try{
            Profile profile = profileRepository.findOne(profileView.getProfileId());
            List<ProfilePermission> permissions = profile.getPermissionList().stream().collect(Collectors.toList());//obtener permisos y tenerlos en memoria
            profile.setPermissionList(new ArrayList<>());//resetear referencias
            profile = convertViewToEntity(profileView,profile,Boolean.TRUE);
            for (ProfilePermission pf: permissions) {
                //eliminar el permiso si ya no vienen en el request
                if(!existPermission(pf.getProfile().getProfileId(),pf.getModulePermission().getModulePermissionId(),profile.getPermissionList())){
                    profilePermissionRepository.delete(pf);
                }
            }
            profileRepository.save(profile);
        }catch (DataIntegrityViolationException dae){
            ProfileException pe = new ProfileException("No fue posible editar el perfil",ProfileException.LAYER_DAO,ProfileException.ACTION_UPDATE);
            pe.addError("Ocurrio un error al editar perfil");
            logger.error("Error al editar perfil - CODE: {} - {}",pe.getExceptionCode(),profileView,dae);
            throw  pe;
        }catch (Exception ex){
            ProfileException pe = new ProfileException("Ocurrio un error al editar perfil",ProfileException.LAYER_DAO,ProfileException.ACTION_UPDATE);
            pe.addError("Ocurrio un error al editar perfil");
            logger.error("Error al editar perfil - CODE: {} - {}",pe.getExceptionCode(),profileView,ex);
            throw  pe;
        }
    }

    @Transactional(readOnly = false,rollbackFor = {ProfileException.class})
    @Override
    /**
     * {@inheritDoc}
     * */
    public void deleteProfile(Long profileId) throws ProfileException {
        try {
            if(!profileRepository.exists(profileId))
                throw new ProfileException("El perfil no existe en la base de datos",ProfileException.LAYER_SERVICE,ProfileException.ACTION_VALIDATE);
            profileRepository.delete(profileId);
        }catch (DataIntegrityViolationException dta){
            ProfileException pe = new ProfileException("No fue posible editar el perfil",ProfileException.LAYER_DAO,ProfileException.ACTION_DELETE);
            pe.addError("El perfil seleccionado no se puede eliminar");
            logger.error("Error al eliminar perfil - CODE: {} - {}",pe.getExceptionCode(),profileId,dta);
            throw  pe;
        }
    }

    @Override
    /**
     * {@inheritDoc}
     * */
    public List<NodeTree> getProfileTree(Long profileId) throws ProfileException {
        try{
            //obtiene la lista de módulos en el sistema
            List<Module> modules = moduleRepository.findAll();
            //lista de nodos a regresar
            List<NodeTree> nodes = new ArrayList<>();

            //estructura para no regresar nodos repetidos
            Map<Long, Object> temporal = new HashMap<>();

            modules.forEach(module -> {
                NodeTree node = new NodeTree();
                node.setActive(false);
                node.setChildren(new ArrayList<>());//se inicia arraylist
                node.setParentId(module.getParentalId());
                if(!module.getModulePermissions().isEmpty()){//agregar los permisos al nodo del arbol
                    node.setChildCount(module.getModulePermissions().size());
                    module.getModulePermissions().forEach(modulePermission -> {
                        NodeTree nodePermission = new NodeTree();
                        nodePermission.setActive(false);
                        if(profileId != null ){
                            //obtener los permisos del perfil
                            Profile p = profileRepository.findOne(profileId);
                            if(!p.getPermissionList().isEmpty()){
                                //busca el permiso dentro de los permisos que tiene el perfil
                                List<ProfilePermission> permission = p.getPermissionList().stream().filter(profilePermission->profilePermission.getModulePermission().getModulePermissionId().compareTo(modulePermission.getModulePermissionId())==0)
                                        .collect(Collectors.toList());
                                if(permission.size()!=0){
                                    nodePermission.setActive(true);
                                }
                            }
                        }
                        nodePermission.setContent(modulePermission.getNamePermission());
                        nodePermission.setId(modulePermission.getModulePermissionId());
                        nodePermission.setModule(false);
                        node.getChildren().add(nodePermission);
                    });
                }
                node.setContent(module.getModuleName());
                node.setId(module.getModuleId());
                node.setModule(true);
                //comparacion de la cantidad tortal de permisos contra los permisos asignados en el perfil
                if(node.getChildCount().compareTo(node.getChildSelected()) == 0 && node.getChildCount() != 0){
                    node.setExpanded(true);
                    node.setActive(true);
                }

                temporal.put(module.getModuleId(), node);// agrego nodos padre

                // pertenecen a un padre
                // buscar el nodo padre dentro del mapa temporal
                NodeTree nodeParent = (NodeTree) temporal.get(module.getParentalId());
                if (nodeParent != null) {
                    logger.info("Parent node: "+nodeParent.getContent());
                    nodeParent.getChildren().add(node);
                    nodeParent.setChildCount(nodeParent.getChildren().size());//numero de hijos
                    nodeParent.setChildSelected(countActiveNodes(nodeParent));//hijos seleccionados
                    if(nodeParent.getChildCount().compareTo(nodeParent.getChildSelected()) == 0 && nodeParent.getChildCount() != 0){
                        nodeParent.setExpanded(true);//expande el nodo
                        nodeParent.setActive(true);//activa el nodo
                    }
                }
            });

            //agregar los nodos obtenidos a la lista
            temporal.keySet().stream().forEach(key->{
                NodeTree node = (NodeTree)temporal.get(key);
                if(node.getParentId()==null || node.getParentId()==0){
                    nodes.add(node);
                    if(!node.getExpanded()){
                        node.setChildCount(node.getChildren().size());//numero de hijos
                        node.setChildSelected(countActiveNodes(node));//hijos seleccionados
                        if(node.getChildCount().compareTo(node.getChildSelected()) == 0 && node.getChildCount() != 0){
                            node.setExpanded(true);//expande el nodo
                            node.setActive(true);//activa el nodo
                        }
                    }
                }
            });
            return nodes;
        }catch (Exception ex){
            ProfileException profileException = new ProfileException("Error al obtener arbol de permisos",ProfileException.LAYER_SERVICE,ProfileException.ACTION_LISTS);
            logger.error("Error al obtener arbol de permisos",ex);
            throw  profileException;
        }
    }

    /**
     * Método para contar el numero de elementos activos de un nodo
     * @param nodeTree
     * @return numero de nodos activos
     * */
    private Integer countActiveNodes(@NotNull NodeTree nodeTree){
        Integer count = 0;
        for(int i= 0; i< nodeTree.getChildren().size();i++){
            if(nodeTree.getChildren().get(i).getActive().booleanValue())
                count++;
        }
        return count;
    }


    @Override
    /**
     * {@inheritDoc}
     * */
    public ProfileView getProfile(Long profileId) throws ProfileException {
        try{
            if(!profileRepository.exists(profileId))
                throw new ProfileException("El perfil no existe en la base de datos",ProfileException.LAYER_DAO,ProfileException.ACTION_SELECT);
            Profile profile = profileRepository.findOne(profileId);
            return convertEntityToView(profile,false);
        }catch (ProfileException pe){
            throw  pe;
        }catch (Exception ex){
            ProfileException profileException = new ProfileException("Error al obtener detalles de perfil",ProfileException.LAYER_SERVICE,ProfileException.ACTION_SELECT);
            logger.error("Error al obtener detalles de perfil: {}",profileId,ex);
            throw  profileException;
        }
    }

    @Override
    public Page<Profile> findProfiles(String name, Integer page, Integer size, String columnOrder, String orderType) throws ProfileException {
        try{
            Sort sort = new Sort(Sort.Direction.ASC,(String)colOrderNames.get("profileId"));

            if(columnOrder!=null && orderType!=null){
                if(orderType.equalsIgnoreCase("asc"))
                    sort = new Sort(Sort.Direction.ASC,(String)colOrderNames.get(columnOrder));
                else
                    sort = new Sort(Sort.Direction.DESC,(String)colOrderNames.get(columnOrder));
            }
            PageRequest request = new PageRequest(page,size,sort);
            return profileRepository.findByName(Utils.getPatternLike(name),request);
        }catch (Exception ex){
            ProfileException profileException = new ProfileException("Ocurrio un error al seleccionar lista de perfiles", ProfileException.LAYER_SERVICE,ProfileException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP+"- Error al tratar de seleccionar lista de perfiles - CODE: {}",profileException.getExceptionCode(),ex);
            throw  profileException;
        }
    }


    @Override
    public List<ProfileView> findAll() throws ProfileException {
        try{
            List<ProfileView> profileViews = new ArrayList<>();
            profileRepository.findAll().stream().forEach(profile -> {
                try {
                    profileViews.add(convertEntityToView(profile, true));
                } catch (ProfileException e) {

                }
            });
            return profileViews;
        }catch (Exception ex){
            ProfileException profileException = new ProfileException("Ocurrio un error al seleccionar lista de perfiles", ProfileException.LAYER_SERVICE,ProfileException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP+"- Error al tratar de seleccionar lista de perfiles - CODE: {}",profileException.getExceptionCode(),ex);
            throw  profileException;
        }
    }

    private Profile convertViewToEntity(ProfileView profileView, Profile profile, Boolean update){
        if(profile==null)
            profile = new Profile();
        if(update) {
            profile.setProfileId(profileView.getProfileId());
            profile.setActive(profileView.getActive());
            profile.setAllowEdition(profileView.getAllowEdition());
        }
        profile.setProfileName(profileView.getProfileName());

        if(profileView.getProfilePermissions()!= null && !profileView.getProfilePermissions().isEmpty()){
            for (PermissionView p: profileView.getProfilePermissions()) {
                    ProfilePermission profilePermission = new ProfilePermission();
                    ModulePermission mp = new ModulePermission();
                    mp.setModulePermissionId(p.getPermissionId());
                    profilePermission.setProfile(profile);
                    profilePermission.setCreatedDate(new Date());
                    profilePermission.setModulePermission(mp);
                    profile.getPermissionList().add(profilePermission);
            }
        }
        return profile;
    }


    private ProfileView convertEntityToView(Profile profile,Boolean shortConvert) throws ProfileException{
        ProfileView profileView = new ProfileView();
        profileView.setProfileId(profile.getProfileId());
        profileView.setActive(profile.getActive());
        profileView.setProfileName(profile.getProfileName());
        profileView.setAllowEdition(profile.getAllowEdition());
        profileView.setCreatedDate(profile.getCreatedDate());
        if(!shortConvert){
            profileView.setTree(getProfileTree(profile.getProfileId()));
        }
        return profileView;
    }


    private Boolean existPermission(Long profileId,Long permissionId,List<ProfilePermission> pf){
        List<ProfilePermission> list= pf.stream().filter(profilePermission -> profilePermission.getProfile().getProfileId().compareTo(profileId)==0 && profilePermission.getModulePermission().getModulePermissionId().compareTo(permissionId)==0 ).collect(Collectors.toList());
        return !list.isEmpty();
    }

}
