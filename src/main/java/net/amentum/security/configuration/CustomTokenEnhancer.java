package net.amentum.security.configuration;

import net.amentum.security.exception.GroupException;
import net.amentum.security.exception.UserAppException;
import net.amentum.security.model.*;
import net.amentum.security.persistence.ProfileRepository;
import net.amentum.security.persistence.UserHassBossRepository;
import net.amentum.security.service.GroupService;
import net.amentum.security.service.UserAppService;
import net.amentum.security.views.GroupView;
import net.amentum.security.views.TicketCategoryView;
import net.amentum.security.views.TicketTypeView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value; // Sre22052020 Nuevo

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author dev06
 */
@Component
public class CustomTokenEnhancer implements TokenEnhancer {

   private List<TokenEnhancer> delegates = Collections.emptyList();

   private UserAppService userAppService;

   private GroupService groupService;

   private UserHassBossRepository userHassBossRepository;

   private final Logger logger = LoggerFactory.getLogger(CustomTokenEnhancer.class);

   // Sre22052020 Agrego nueva variable de ambiente para que la ruta al secret.key no este hard-coded
   @Value("${secret.path:/opt/secrets/}")
   private String secretPath;

   @Autowired
   public void setUserHassBossRepository(UserHassBossRepository userHassBossRepository) {
      this.userHassBossRepository = userHassBossRepository;
   }

   @Autowired
   public void setUserAppService(UserAppService userAppService) {
      this.userAppService = userAppService;
   }

   @Autowired
   public void setGroupService(GroupService groupService) {
      this.groupService = groupService;
   }

   public void setTokenEnhancers(List<TokenEnhancer> delegates) {
      this.delegates = delegates;
   }

   /**
    * Se sobreescribe método para agregar más detalles a la respuesta de login por oauth, se agrega información extra a la sesión generada por oauth
    **/
   @Override
   public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
      DefaultOAuth2AccessToken tempResult = (DefaultOAuth2AccessToken) accessToken;
      final Map<String, Object> additionalInformation = new HashMap<>();
      Collection<GrantedAuthority> infoValue = authentication.getAuthorities();
      List<String> roles = new ArrayList<>();
      for (GrantedAuthority authority : infoValue) {
         /**
          *  Se obtiene la lista de permisos
          * */
         roles.addAll(Arrays.asList(authority.getAuthority().split(",")));
      }
      /**
       * Se agrega la lista de permisos
       * */
      additionalInformation.put("roles", roles);

      try {
         User principal = (User) authentication.getPrincipal();
         UserApp userApp = userAppService.findByUsername(principal.getUsername());
         String imagenGrupoPrincipal = null; //groupService.findImageGroup(userApp.getUserAppId()); // GGR20200610
         String nombreGrupoPrincipal = null; //groupService.findNameMainGroup(userApp.getUserAppId()); // GGR20200612
         Long idGrupoPrincipal = null; //groupService.findIdMainGroup(userApp.getUserAppId()); // GGR20200612

         logger.info("Permisos iniciales: {}", roles);
         for (ProfilePermission profilePermission : userApp.getProfile().getPermissionList()) {
            if(!roles.contains(profilePermission.getModulePermission().getCodeModulePermission())) {
               logger.info("Permiso añadido: {}", profilePermission.getModulePermission().getNamePermission());
               roles.add(profilePermission.getModulePermission().getCodeModulePermission());
            } else
               logger.info("Permiso repedito: {}", profilePermission.getModulePermission().getNamePermission());
         }

         additionalInformation.put("username", userApp.getUsername());
         additionalInformation.put("name", userApp.getName());
         additionalInformation.put("userId", userApp.getUserAppId());
         ////////////////////////////////////////////// catalogo tipo usuario
         additionalInformation.put("idTipoUsuario", (userApp.getTipoUsuario().getIdTipoUsuario()) != null ? userApp.getTipoUsuario().getIdTipoUsuario() : null);
         additionalInformation.put("decripcionTipoUsuario", (userApp.getTipoUsuario().getDescripcion()) != null ? userApp.getTipoUsuario().getDescripcion() : null);
         ////////////////////////////////////////////// catalogo tipo usuario

         List<Long> idGroups = userApp.getGroupList()
            .stream().map(uh -> uh.getGroup().getGroupId()).collect(Collectors.toList());
         List<Long> typeList = new ArrayList<>();
         List<Long> categories = new ArrayList<>();
         List<HashMap> listaGrupos = new ArrayList();

         for (Long idGroup : idGroups) {
            GroupView groupView = groupService.findGroup(idGroup);
            HashMap<Long, HashMap> unGrupo = new HashMap<>();
            HashMap<String, String> datosGrupo = new HashMap<>();
            imagenGrupoPrincipal = groupView.getImagen(); // GGR20200616
            nombreGrupoPrincipal = groupView.getGroupName(); // GGR20200616
            idGrupoPrincipal = groupView.getGroupId(); // GGR20200616
            datosGrupo.put(groupView.getGroupName(), groupView.getImagen());
            unGrupo.put(idGroup, datosGrupo);
            listaGrupos.add(unGrupo);
            for (TicketTypeView type : groupView.getTicketTypeView()) {
               if (!existInList(type.getIdTicketType(), typeList)) {
                  typeList.add(type.getIdTicketType());
                  for (TicketCategoryView category : type.getTicketCategoryView()) {
                     categories.add(category.getIdTicketCategory());
                  }
               }
            }
         }
         additionalInformation.put("grupos", listaGrupos); // GGR20200612
         additionalInformation.put("imagen_grupo_principal", imagenGrupoPrincipal); // GGR20206010
         additionalInformation.put("nombre_grupo_principal", nombreGrupoPrincipal); // GGR20206012
         additionalInformation.put("id_grupo_principal", idGrupoPrincipal); // GGR2020601
         String groups = convertListToString("G", idGroups);
         String tipoTicket = convertListToString("TT", typeList);
         String categoriesTicket = convertListToString("TC", categories);

         SortedSet<Long> userLongList = new TreeSet<>();
         userLongList.add(userApp.getUserAppId());
         getUsersAssigned(userApp.getUserAppId(), userLongList);
         String usersAssiged = convertListToString("UA", new ArrayList<Long>(userLongList));

         String hierarchy = groups + "|" + tipoTicket + "|" + categoriesTicket + "|" + usersAssiged + "|ID|" + userApp.getUserAppId() + "|NU|" + userApp.getName() +
            "|PAD|" + UUID.randomUUID().toString().toUpperCase();

         if (userApp.getTipoUsuario().getIdTipoUsuario() != null) {
            hierarchy += "|TU|" + userApp.getTipoUsuario().getIdTipoUsuario();
         }
         if(userApp.getTipoUsuario().getDescripcion() != null){
            hierarchy += "|DTU|" + userApp.getTipoUsuario().getDescripcion();
         }

//         logger.debug("===>>>{}",hierarchy);
//         logger.info("===>>>{}",hierarchy);
//         logger.info("hiera token: "+hierarchy);
         String hierarchy_msg = encriptHerarchy(hierarchy);
//         logger.info("ENCRIPTADO: "+hierarchy_msg);
         additionalInformation.put("hier_token", hierarchy_msg);

      } catch (UserAppException ex) {
         logger.error("Error al obtener detalles de usuario");
      } catch (GroupException ex) {
         logger.error("Error al obtener detalles grupos");
      } catch (Exception ex) {
         logger.error("Error al obtener detalles grupos", ex); // Sre10022020 Reporto ex
//         System.out.println(ex.getMessage());
      }

      tempResult.setAdditionalInformation(additionalInformation);

      OAuth2AccessToken result = tempResult;
      for (TokenEnhancer enhancer : delegates) {
         result = enhancer.enhance(result, authentication);
      }
      return result;
   }

   private void getUsersAssigned(Long idUser, Set<Long> users) {
      Specifications<UserHasBoss> spec = Specifications.where(
         (root, query, cb) -> {
            Join<UserHasBoss, UserHasBossId> pk = root.join("pk");
            Join<UserHasGroupId, UserApp> user = pk.join("boss");
            Predicate q = null;
            q = q != null ? cb.and(q, cb.equal(user.get("userAppId"), idUser)) : cb.equal(user.get("userAppId"), idUser);
            return q;
         }
      );
      List<UserHasBoss> bossList = userHassBossRepository.findAll(spec);
      users.addAll(bossList.stream().map(s -> s.getUserApp().getUserAppId()).collect(Collectors.toList()));
      bossList.stream().forEach(b -> getUsersAssigned(b.getUserApp().getUserAppId(), users));
   }

   private String convertListToString(String type, List<Long> longList) {
      String list = type;
      for (Long id : longList) {
         list += "|" + id;
      }
      return list;
   }

   private String encriptHerarchy(String hierarchy) throws Exception {
      // Sre22052020 Inicia uso variable para el path
      //AESEncrypt aesEncrypt = new AESEncrypt("/opt/secrets/secret.key");
      //AESEncrypt aesEncrypt = new AESEncrypt("secret.key");
      AESEncrypt aesEncrypt = new AESEncrypt(secretPath + "secret.key");
      // Sre22052020 Termina
      String encrypted_msg = null;
      try {
         //logger.info("Jeararquia: "+hierarchy);
         encrypted_msg = aesEncrypt.encrypt(hierarchy);
         //logger.info("Cifrado: "+encrypted_msg);
         //logger.info("Desifrado: "+aesEncrypt.decrypt(encrypted_msg));
      } catch (Exception ioe) {
         logger.error("", ioe);
      }
      return encrypted_msg;
   }

   private Boolean existInList(Long idTicketType, List<Long> listTicketType) throws Exception {
      Boolean exist = Boolean.FALSE;
      for (Long id : listTicketType) {
         if (id == idTicketType) {
            exist = Boolean.TRUE;
         }
      }
      return exist;
   }

}
