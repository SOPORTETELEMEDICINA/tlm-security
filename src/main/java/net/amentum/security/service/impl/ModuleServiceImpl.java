package net.amentum.security.service.impl;

import net.amentum.security.Utils;
import net.amentum.security.exception.ExceptionServiceCode;
import net.amentum.security.exception.ModuleException;
import net.amentum.security.model.Module;
import net.amentum.security.model.ModulePermission;
import net.amentum.security.persistence.ModuleRepository;
import net.amentum.security.service.ModuleService;
import net.amentum.security.views.ModuleView;
import net.amentum.security.views.PermissionView;
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
import java.util.stream.Collectors;

/**
 * Created by dev06 on 11/04/17.
 */
@Service
@Transactional(readOnly = true)
public class ModuleServiceImpl implements ModuleService{

    private final Logger logger = LoggerFactory.getLogger(ModuleServiceImpl.class);

    @Autowired
    private ModuleRepository moduleRepository;

    private final Map<String,Object> colOrderNames = new HashMap<String,Object>();
    {
        colOrderNames.put("moduleId","moduleId");
        colOrderNames.put("moduleName","createdDate");
        colOrderNames.put("createdDate","moduleName");
    }

    @Override
    @Transactional(readOnly = false,rollbackFor = {ModuleException.class})
    /**
     * {@inheritDoc}
     * */
    public void addModule(ModuleView moduleView) throws ModuleException {
        try{
            if(moduleView.getModulePermissions().isEmpty())
                throw new ModuleException("La lista de permisos no debe estar vacia",ModuleException.LAYER_SERVICE,ModuleException.ACTION_VALIDATE);
            Module module = convertViewToEntity(moduleView,Boolean.FALSE);
            logger.debug("Insertando módulo a base  de datos: {}",module);
            moduleRepository.save(module);
        }catch (DataIntegrityViolationException dta){
            ModuleException moduleException = new ModuleException("Error al agregar el módulo a la base de datos",ModuleException.LAYER_DAO,ModuleException.ACTION_VALIDATE);
            moduleException.addError("Ya existe un módulo o código con el mismo nombre en base de datos");
            logger.error(ExceptionServiceCode.MODULE+" - No fue posible insertar el módulo:{}", moduleView,dta);
            throw  moduleException;
        }catch (ModuleException me){
            throw  me;
        }
    }

    @Override
    @Transactional(readOnly = false,rollbackFor = {ModuleException.class})
    /**
     * {@inheritDoc}
     * */
    public void editModule(ModuleView moduleView) throws ModuleException {
        try{
            if(!moduleRepository.exists(moduleView.getModuleId())){
                throw new ModuleException("El módulo no existe en base de datos",ModuleException.LAYER_SERVICE,ModuleException.ACTION_VALIDATE);
            }

            Module module = moduleRepository.findOne(moduleView.getModuleId());
            Date createdDate = module.getCreatedDate();
            List<ModulePermission> permissions = module.getModulePermissions().stream().collect(Collectors.toList());
            module = convertViewToEntity(moduleView,true);//replace in memoty
            module.setCreatedDate(createdDate);

            //eliminar si no vienen en el request
            for(ModulePermission p : permissions){
                if(indexOfPermission(p.getModulePermissionId(),module.getModulePermissions())==-1){//eliminar el permiso en BD
                    try{
                        moduleRepository.deletePermission(p.getModulePermissionId());
                    }catch (DataIntegrityViolationException dta){
                        ModuleException moduleException= new ModuleException("Error al eliminar permiso: "+p.getCodeModulePermission(),ModuleException.LAYER_DAO,ModuleException.ACTION_DELETE);
                        moduleException.addError("No es posible eliminar permiso: "+p.getCodeModulePermission()+", ya que se encuentra en uso");
                        throw  moduleException;
                    }catch (Exception ex){
                        ModuleException moduleException= new ModuleException("Error al eliminar permiso: "+p.getCodeModulePermission(),ModuleException.LAYER_DAO,ModuleException.ACTION_DELETE);
                        moduleException.addError("No es posible eliminar permiso: "+p.getCodeModulePermission()+", intente nuevamente");
                        logger.error(ExceptionServiceCode.MODULE+"- Ocurrio un error al eliminar permiso: {} -  del modulo: {}",p.getCodeModulePermission(),module.getModuleName(),ex);
                        throw  moduleException;
                    }
                }
            }
            moduleRepository.save(module);
        }catch (DataIntegrityViolationException dta){
            ModuleException moduleException= new ModuleException("Error al editar módulo",ModuleException.LAYER_DAO,ModuleException.ACTION_UPDATE);
            moduleException.addError("No es posible editar el permiso");
            throw  moduleException;
        }catch (ModuleException me){
            throw  me;
        }
    }

    @Override
    @Transactional(readOnly = false,rollbackFor = {ModuleException.class})
    /**
     * {@inheritDoc}
     * */
    public void deleteModule(Long moduleId) throws ModuleException {
        try{
            if(!moduleRepository.exists(moduleId)){
                throw new ModuleException("El módulo no existe en base de datos",ModuleException.LAYER_SERVICE,ModuleException.ACTION_VALIDATE);
            }
            Module m = moduleRepository.findOne(moduleId);
            moduleRepository.delete(m);
        }catch (DataIntegrityViolationException dta){
            ModuleException moduleException = new ModuleException("Error al eliminar el módulo de la base de datos",ModuleException.LAYER_DAO,ModuleException.ACTION_DELETE);
            moduleException.addError("No es posible eliminar el módulo o sus permisos ya que se encuentran en uso");
            logger.error(ExceptionServiceCode.MODULE+" - No fue posible eliminar el módulo:{}", moduleId,dta);
            throw  moduleException;
        }catch (ModuleException me){
            throw me;
        }catch (Exception e){
            ModuleException moduleException = new ModuleException("Error al eliminar el módulo de la base de datos",ModuleException.LAYER_DAO,ModuleException.ACTION_DELETE);
            logger.error(ExceptionServiceCode.MODULE+" - No fue posible eliminar el módulo:{}", moduleId,e);
            throw  moduleException;
        }
    }

    @Override
    /**
     * {@inheritDoc}
     * */
    public ModuleView getModule(Long moduleId) throws ModuleException {
        try{
            if(!moduleRepository.exists(moduleId))
                throw new ModuleException("El módulo no existe en base de datos",ModuleException.LAYER_SERVICE,ModuleException.ACTION_VALIDATE);
            Module m = moduleRepository.findOne(moduleId);
            return convertEntityToView(m,false);
        }catch (ModuleException me){
            throw  me;
        }
        catch (Exception ex){
            ModuleException moduleException = new ModuleException("Error al agregar seleccionar detalles de módulo",ModuleException.LAYER_DAO,ModuleException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.MODULE+" - No fue posible seleccionar módulo: {}",moduleId,ex);
            throw  moduleException;
        }
    }

    @Override
    /**
     * {@inheritDoc}
     * */
    public Page<ModuleView> getModules(String name, Integer page, Integer size, String columnOrder, String orderType) throws ModuleException {
        try{
            Sort sort = new Sort(Sort.Direction.ASC,(String)colOrderNames.get("moduleId"));

            if(columnOrder!=null && orderType!=null){
                if(null == orderType || orderType.equalsIgnoreCase("asc"))
                    sort = new Sort(Sort.Direction.ASC,(String)colOrderNames.get(columnOrder));
                else
                    sort = new Sort(Sort.Direction.DESC,(String)colOrderNames.get(columnOrder));
            }
            PageRequest request = new PageRequest(page,size,sort);
            Page<Module> modulePage = moduleRepository.findByNameLike(Utils.getPatternLike(name),request);
            ArrayList<ModuleView> modules = new ArrayList<>();
            //modulePage.getContent().stream().forEach(module -> {modules.add(convertEntityToView(module,true));});
            for (Module m: modulePage.getContent()) {
                modules.add(convertEntityToView(m,true));
            }

            PageImpl<ModuleView> moduleViews = new PageImpl<ModuleView>(modules,request,modulePage.getTotalElements());


            return moduleViews;
        }catch (Exception ex){
            ModuleException moduleException = new ModuleException("Error al agregar seleccionar lista de módulos",ModuleException.LAYER_DAO,ModuleException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.MODULE+" - No fue posible seleccionar módulos");
            throw  moduleException;
        }
    }

    /**
     * Método para convertir una vista a su equivalente Entity para ser guardado o editado en la base de datos
     * @param moduleView la vista a ser convertida
     * @param update bandera pra saber si se esta convirtiendo desde un método para guardar un nuevo objeto o para editarlo<br/>
     * necesario para agregar la fecha de creación del objeto
     * @return Nueva entidad obtenida desde la vista
     * */
    private Module convertViewToEntity(ModuleView moduleView,Boolean update){
        Module module = new Module();
        if(!update)
        module.setCreatedDate(new Date());
        module.setModuleId(moduleView.getModuleId());
        module.setModuleName(moduleView.getModuleName());
        module.setParentalId(moduleView.getParentalId());

        if(!moduleView.getModulePermissions().isEmpty()){
            for(PermissionView p: moduleView.getModulePermissions()) {
                if(p!=null) {
                    ModulePermission permission = new ModulePermission();
                    permission.setModule(module);
                    permission.setCodeModulePermission(p.getCodePermission());
                    permission.setModulePermissionId(p.getPermissionId());
                    permission.setNamePermission(p.getNamePermission());
                    module.getModulePermissions().add(permission);
                }
            }
        }

        return module;
    }

    /**
     * Método para convertir una entidad a una vista
     * @param module  la entidad a ser convertida
     * @param shortConvert bandera boleana para saber el tipo de conversión que se esta realizando
     * <br/>Cuando el parametro <strong>shortConvert</strong> es verdadero, se incluye la lista de permisos del módulo
     * @return la vista obtenida de la conversión con o sin lista de permisos dependiendo de <strong>shortConvert</strong>
     * **/
    private ModuleView convertEntityToView(Module module,Boolean shortConvert){
        ModuleView moduleView = new ModuleView();
        moduleView.setParentalId(module.getParentalId());
        moduleView.setModuleName(module.getModuleName());
        moduleView.setCreatedDate(module.getCreatedDate());
        moduleView.setModuleId(module.getModuleId());

        if(!shortConvert){
            //agregar permisos del módulo
            for(ModulePermission p : module.getModulePermissions()){
                PermissionView permissionView = new PermissionView();
                permissionView.setNamePermission(p.getNamePermission());
                permissionView.setCodePermission(p.getCodeModulePermission());
                permissionView.setPermissionId(p.getModulePermissionId());
                moduleView.getModulePermissions().add(permissionView);
            }
        }

        return moduleView;
    }

    /**
     * Método para obtener el indice en el que se encuentra un ID en la lista de permisos
     * @param permissionId ID único a buscar
     * @param permissions  lista de permisos
     * @return el indice encontrado en el arreglo, si el valor es igual a <strong>-1</strong>, significa que no se encontro el elemento
     * **/
    private Integer indexOfPermission(Long permissionId,List<ModulePermission> permissions){
        for(int i=0;i<permissions.size();i++){
            if(permissions.get(i).getModulePermissionId().compareTo(permissionId)==0){
                return i;
            }
        }
        return -1;
    }
}
