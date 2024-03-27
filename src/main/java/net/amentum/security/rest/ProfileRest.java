package net.amentum.security.rest;

import net.amentum.common.BaseController;
import net.amentum.security.exception.ExceptionServiceCode;
import net.amentum.security.exception.ProfileException;
import net.amentum.security.model.Profile;
import net.amentum.security.service.ProfileService;
import net.amentum.security.views.NodeTree;
import net.amentum.security.views.ProfileView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Victor de la Cruz
 */
@RestController
@RequestMapping("profiles")
public class ProfileRest extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(ProfileRest.class);


    private ProfileService profileService;

    @Autowired
    public void setProfileService(ProfileService profileService) {
        this.profileService = profileService;
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addProfile(@RequestBody @Valid ProfileView profileView) throws ProfileException{
        logger.info("Guardar nuevo perfil: {}  ",profileView);
        profileService.addProfile(profileView);
    }


    @RequestMapping(value = "{profileId}",method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void editProfile(@PathVariable()Long profileId,@RequestBody @Valid ProfileView profileView) throws ProfileException{
        logger.info("Editar perfil: {} - ID: {}",profileView,profileId);
        profileView.setProfileId(profileId);
        profileService.editProfile(profileView);
    }


    @RequestMapping(value = "{profileId}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteProfile(@PathVariable Long profileId) throws ProfileException{
        logger.info("Eliminar perfil: {}",profileId);
        profileService.deleteProfile(profileId);
    }


    @RequestMapping(value = "{profileId}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ProfileView getProfile(@PathVariable Long profileId) throws ProfileException{
        logger.info("Obtener perfil: {}",profileId);
        return profileService.getProfile(profileId);
    }


    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<Profile> getProfiles(@RequestParam(required = false)String name, @RequestParam(required = false)Integer page, @RequestParam(required=false)Integer size,@RequestParam(required = false)
            String orderColumn,@RequestParam(required = false)String orderType) throws ProfileException{
        logger.info(ExceptionServiceCode.PROFILE+"- Obtener listado de perfiles: {} - page {} - size: {} - orderColumn: {} - orderType: {}",name,page,size,orderColumn,orderType);
        if(page==null)
            page = 0;
        if(size==null)
            size = 10;
        if(name==null)
            name = "";
        return profileService.findProfiles(name,page,size,orderColumn,orderType);
    }


    @RequestMapping(value = "/tree",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<NodeTree> findNodes() throws ProfileException{
        logger.info("Obtener arbol de permisos");
        return profileService.getProfileTree(null);
    }


    @RequestMapping(value = "all",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<ProfileView> findAll() throws ProfileException{
        logger.info("Obtener lista de perfiles");
        return profileService.findAll();
    }

}
