package net.amentum.security.rest;

import net.amentum.common.BaseController;
import net.amentum.security.exception.ExceptionServiceCode;
import net.amentum.security.exception.UserAppException;
import net.amentum.security.model.UserApp;
import net.amentum.security.service.UserAppService;
import net.amentum.security.views.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author marellano on 25/04/17.
 */

@RestController
@RequestMapping("users")
public class UserAppRest extends BaseController {

   private final Logger logger = LoggerFactory.getLogger(UserAppRest.class);

   private UserAppService serviceUserApp;

   @Autowired
   public void setServiceUserApp(UserAppService serviceUserApp) {
      this.serviceUserApp = serviceUserApp;
   }

   //    @RequestMapping(method = RequestMethod.POST)
//    @ResponseStatus(HttpStatus.CREATED)
//    public void createdUserApp(@RequestBody @Valid UserAppView userAppView) throws UserAppException {
//        try{
//            if(userAppView.getPassword() == null  ||  userAppView.getPassword().isEmpty()){
//                UserAppException uae = new UserAppException("Es requerido el password.", UserAppException.LAYER_CONTROLLER, UserAppException.ACTION_VALIDATE);
//                uae.addError("Es requerido el password.");
//                throw  uae;
//            }
//            logger.info("Guardar nuevo usuario: {} ",userAppView);
//            serviceUserApp.createdUserApp(userAppView);
//        }catch (UserAppException uae) {
//            throw uae;
//        }catch (Exception ex) {
//            UserAppException uae = new UserAppException("No fue posible insertar el usuario", UserAppException.LAYER_DAO, UserAppException.ACTION_SELECT);
//            logger.error("Error al insertar el usuario - CODE: {} - ",uae.getExceptionCode(),ex);
//            throw uae;
//        }
//    }
   @RequestMapping(method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public UserAppPageView createdUserApp(@RequestBody @Valid UserAppView userAppView) throws UserAppException {
      try {
         if (userAppView.getPassword() == null || userAppView.getPassword().isEmpty()) {
            UserAppException uae = new UserAppException("Es requerido el password.", UserAppException.LAYER_REST, UserAppException.ACTION_VALIDATE);
            uae.addError("Es requerido el password.");
            throw uae;
         }
         if (userAppView.getIdTipoUsuario() == null) {
            logger.error("===>>>idTipoUsuario NULO/VACIO");
            UserAppException uae = new UserAppException("Existe un Error", UserAppException.LAYER_REST, UserAppException.ACTION_VALIDATE);
            uae.addError("idTipoUsuario NULO/VACIO");
            throw uae;
         }
         logger.info("Guardar nuevo usuario: {} ", userAppView);
         serviceUserApp.createdUserApp(userAppView);
         UserApp userApp = serviceUserApp.findByUsername(userAppView.getUsername());
         UserAppPageView userAppPageView = new UserAppPageView(
            userApp.getUserAppId(),
            userApp.getUsername(),
            userApp.getEmail(),
            userApp.getName(),
            userApp.getProfile().getProfileName());
         return (userAppPageView);
      } catch (UserAppException uae) {
         throw uae;
      } catch (Exception ex) {
         UserAppException uae = new UserAppException("No fue posible insertar el usuario", UserAppException.LAYER_REST, UserAppException.ACTION_SELECT);
         logger.error("Error al insertar el usuario - CODE: {} - ", uae.getExceptionCode(), ex);
         throw uae;
      }
   }

   @RequestMapping(value = "{idUserApp}", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public void updateUserApp(@PathVariable() Long idUserApp, @RequestBody @Valid UserAppView userAppView) throws UserAppException {
      userAppView.setIdUserApp(idUserApp);
      logger.info("Editar usuario: {}", userAppView);
      serviceUserApp.updateUserApp(userAppView);
   }

   @RequestMapping(value = "{idUserApp}/movil", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public void updateUserAppFromMovil(@PathVariable() Long idUserApp, @RequestBody @Valid UserAppView userAppView) throws UserAppException {
      userAppView.setIdUserApp(idUserApp);
      logger.info("Editar usuario: {}", userAppView);
      serviceUserApp.updateUserAppMovil(userAppView);
   }


   @RequestMapping(value = "{idUserApp}/{motivo}", method = RequestMethod.DELETE)
   @ResponseStatus(HttpStatus.OK)
   public void deleteUserApp(@PathVariable() Long idUserApp, @PathVariable() String motivo) throws UserAppException {
      logger.info("Eliminar usuario: {}", idUserApp);
      serviceUserApp.deleteUserApp(idUserApp, motivo);
   }


   @RequestMapping(method = RequestMethod.POST, value = "list")
   @ResponseStatus(HttpStatus.OK)
   public Page<UserAppPageView> findUsers(@RequestBody FindUsersView view) throws UserAppException {
      logger.info(ExceptionServiceCode.PROFILE + "- Obtener listado de usuarios: {} - page {} - size: {} - group {}", view.getIdUsersList(),
         view.getPage(), view.getSize(), view.getGroup());
      logger.debug(ExceptionServiceCode.PROFILE + "- Obtener listado de usuarios: {} - name: {} - page {} - size: {} - orderColumn: {} - orderType: {}", view.getIdUsersList(),
         view.getName(), view.getPage(), view.getSize(), view.getOrderColumn(), view.getOrderType());
      if(view.getGroup() == null || view.getGroup() < 0)
         throw new UserAppException("Grupo requerido para la búsqueda", UserAppException.LAYER_REST, UserAppException.ACTION_SELECT);
      if (view.getPage() == null)
         view.setPage(0);
      if (view.getSize() == null)
         view.setSize(10);
      if (view.getName() == null)
         view.setName("");
       // Sre22052020 Inicia si usuario es admin view.getIdUsersList() viene en null
       if (view.getIdUsersList() == null) {
           return serviceUserApp.findUsersAdmin(view.getName(), view.getPage(), view.getSize(), view.getGroup(), view.getOrderColumn(), view.getOrderType(), view.getIdUsersList());
       }
      return serviceUserApp.findUsers(view.getName(), view.getPage(), view.getSize(), view.getGroup(), view.getOrderColumn(), view.getOrderType(), view.getIdUsersList());
       // Sre22052020 Termina
   }

   @RequestMapping(value = "search", method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.OK)
   public Page<UserAppPageView> SearchUsersByGroups(@RequestBody @Valid UserRequestView view) throws UserAppException {
      logger.info(ExceptionServiceCode.PROFILE + " - Obtener listado de usuarios: {}", view);
      if (view.getPage() == null) {
         view.setPage(0);
      }
      if (view.getSize() == null) {
         view.setSize(10);
      }
      return serviceUserApp.searchUsersByGroups(view.getName() != null ? view.getName() : "", view.getPage(), view.getSize(),
         view.getOrderColumn(), view.getOrderType(), view.getIdGroups(), view.getUsers());
   }

   @RequestMapping(value = "{idUserApp}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public UserAppView getDetailsUserApp(@PathVariable() Long idUserApp, @RequestParam(required = false) Boolean image) throws UserAppException {
      logger.info("Obtener los detalles del usuario: " + idUserApp);
      return serviceUserApp.getDetailsUserApp(idUserApp, image);
   }

   @RequestMapping(value = "findByGroups", method = RequestMethod.POST)
   public List<UserAppView> findByGroups(@RequestBody FindByGroupsView view) throws UserAppException {
      logger.info("Obtener usuarios por grupo: {} - y usuarios: {}", view.getGroups(), view.getIdUsers());
      return serviceUserApp.findByGroups(view.getGroups(), view.getIdUsers());
   }

   @PostMapping(value = "assigned")
   @ResponseStatus(HttpStatus.OK)
   public List<UsersAssignedView> getUsersAssignedToBoss(@RequestBody UserAssignedRequestView view) throws UserAppException {
      logger.info("- Obtener usuarios asignados al perfil jefe: {}, con grupos: {}", view.getIdProfile(), view.getIdGroups());
      logger.debug("- Obtener usuarios asignados al perfil jefe: {}, con grupos: {}", view.getIdProfile(), view.getIdGroups());
      return serviceUserApp.getUsersAssignedToBoss(view.getIdProfile(), view.getIdGroups());
   }

   @PostMapping(value = "infoBasicUsers")
   @ResponseStatus(HttpStatus.OK)
   public List<InfoBasicResponseView> getInfoBasicUsers(@RequestBody InfoBasicRequestView view) throws UserAppException {
      logger.info("- Obtener informacipon básica de los usuarios: {} ", view.getIdUsers());
      logger.debug("- Obtener informacipon básica de los usuarios: {} ", view.getIdUsers());
      return serviceUserApp.getInfoBasicUsers(view);
   }


   @RequestMapping(value = "rollback/{idUserApp}", method = RequestMethod.DELETE)
   @ResponseStatus(HttpStatus.OK)
   public void deleteRollBack(@PathVariable("idUserApp") Long idUserApp) throws UserAppException {
      logger.info("Haciendo el rollback de usuario: {}", idUserApp);
      serviceUserApp.deleteRollBack(idUserApp);
   }

   @RequestMapping(value = "id-usuarios-de-grupos", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<Long> obtenerIdUserApp(@RequestParam(required = true) List<Integer> grupos) throws  UserAppException{
      logger.info("obtenerIdUserApp() - grupos: {}",grupos);
      return serviceUserApp.obtenerIdUserAppDeGrupos(grupos);
   }

    // Sre22052020 Agrego método para buscar un usuario por username
    @RequestMapping(value = "username/{username}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public UserAppView getUserAppByUsername(@PathVariable("username") String username) throws UserAppException {
        logger.info("Obtener los detalles del usuario por username: " + username);
        UserAppView userAppView = serviceUserApp.findViewByUsername(username);
        return userAppView;
    }


   // GGR20200709 Agrego método para buscar horario por idUser
   @RequestMapping(value = "horario/{idUserApp}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public Map<String, Object> getHorarioByIdUser(@PathVariable("idUserApp") Long idIUserApp) throws UserAppException {
      logger.info("Obtener horario del usuario: " + idIUserApp);
      Map<String, Object> listaUsuarios = serviceUserApp.findHorarioByIdUser(idIUserApp);
      return listaUsuarios;
   }

   @RequestMapping(value = "{idUserApp}/reset", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public void resetPassword(@PathVariable() Long idUserApp, @RequestBody @Valid ResetPasswordView userPassword) throws UserAppException {
      logger.info("Id usuario: {} - Password: {} - New Password: {}", idUserApp, userPassword.getPassword(), userPassword.getNewPassword());
      if(userPassword.getPassword() == null && userPassword.getPassword().isEmpty())
         throw new UserAppException("La contraseña no debe ser vacía", UserAppException.LAYER_REST, UserAppException.ACTION_SELECT);
      if(userPassword.getNewPassword() == null && userPassword.getNewPassword().trim().isEmpty())
         throw new UserAppException("El hash no debe ser vacío", UserAppException.LAYER_REST, UserAppException.ACTION_SELECT);
      userPassword.setIdUserApp(idUserApp);
      serviceUserApp.resetUserPassword(userPassword);
   }

}
