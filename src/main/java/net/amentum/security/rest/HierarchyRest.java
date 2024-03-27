package net.amentum.security.rest;

import net.amentum.common.BaseController;
import net.amentum.security.exception.HierarchyException;
import net.amentum.security.exception.UserAppException;
import net.amentum.security.service.HierarchyService;
import net.amentum.security.views.HierarchyRequestView;
import net.amentum.security.views.ProfileHierarchyView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("hierarchies")
public class HierarchyRest extends BaseController {

    private Logger logger = LoggerFactory.getLogger(HierarchyRest.class);

    private HierarchyService hierarchyService;

    @Autowired
    public void setHierarchyService(HierarchyService hierarchyService) {
        this.hierarchyService = hierarchyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void addOrUpdateHierarchy(@RequestBody @Valid HierarchyRequestView hierarchyView) throws HierarchyException {
        try{
            logger.info("- Guardar nueva jerarquía: {} ",hierarchyView);
            hierarchyService.addOrUpdateHierarchy(hierarchyView);
        }catch (HierarchyException he) {
            throw he;
        }catch (Exception ex) {
            HierarchyException uae = new HierarchyException("No fue posible insertar la jerarquía", HierarchyException.LAYER_DAO, HierarchyException.ACTION_SELECT);
            logger.error("Error al insertar la jerarquía - CODE: {} - ",uae.getExceptionCode(),ex);
            throw uae;
        }
    }

    @GetMapping(value = "{idPerfilBoss}")
    @ResponseStatus(HttpStatus.OK)
    public List<ProfileHierarchyView> getHierarchy(@PathVariable Long idPerfilBoss) throws HierarchyException {
        logger.info(" - HIC - Obtener lista de perfiles asignados al perfil: {} ",idPerfilBoss);
        logger.debug(" - HIC - Obtener lista de perfiles asignados al perfil: {}",idPerfilBoss);
        return  hierarchyService.getHierarchy(idPerfilBoss);
    }
}
