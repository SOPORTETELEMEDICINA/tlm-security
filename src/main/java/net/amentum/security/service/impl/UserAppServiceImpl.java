package net.amentum.security.service.impl;


import net.amentum.security.Utils;
import net.amentum.security.converter.UserImageConverter;
import net.amentum.security.exception.ExceptionServiceCode;
import net.amentum.security.exception.UserAppException;
import net.amentum.security.model.Group;
import net.amentum.security.model.ModulePermission;
import net.amentum.security.model.Profile;
import net.amentum.security.model.RowStatus;
import net.amentum.security.model.UserApp;
import net.amentum.security.model.UserExtraInfo;
import net.amentum.security.model.UserHasBoss;
import net.amentum.security.model.UserHasGroup;
import net.amentum.security.model.UserHasGroupId;
import net.amentum.security.model.UserHasPermission;
import net.amentum.security.model.UserImage;
import net.amentum.security.persistence.*;
import net.amentum.security.rest.UserAppRest;
import net.amentum.security.service.UserAppService;
import net.amentum.security.utils.email.Email;
import net.amentum.security.utils.email.EmailService;
import net.amentum.security.utils.email.EmailTemplate;
import net.amentum.security.views.*;
import org.apache.commons.codec.binary.Base64;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.*;
import javax.mail.internet.*;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author by marellano on 25/04/17.
 */

@Service
@Transactional(readOnly = true)
public class UserAppServiceImpl implements UserAppService {

   private final Logger logger = LoggerFactory.getLogger(UserAppServiceImpl.class);

   private String emailTemplateHtml = "reset-password.html";

   private ProfileRepository profileRepository;

   @Autowired
   public void setProfileRepository(ProfileRepository profileRepository) {
      this.profileRepository = profileRepository;
   }

   private GroupRepository groupRepository;

   private UserImageConverter imageConverter;

   private ModulePermissionRepository modulePermissionRepository;

   private UserAppRepository userAppRepository;

   private WorkShiftRepository workShiftRepository;

   private ProfileHasBossRepository hasBossRepository;

   private UserHassBossRepository userHassBossRepository;

   private UserImageRepository userImageRepository;

   private EmailService emailService;

   @Autowired
   public void setEmailService(EmailService emailService) {
      this.emailService = emailService;
   }

   @Autowired
   public void setUserImageRepository(UserImageRepository userImageRepository) {
      this.userImageRepository = userImageRepository;
   }

   ////////////////////////////////////////////// catalogo tipo usuario
   private TipoUsuarioRepository tipoUsuarioRepository;

   @Autowired
   public void setTipoUsuarioRepository(TipoUsuarioRepository tipoUsuarioRepository) {
      this.tipoUsuarioRepository = tipoUsuarioRepository;
   }
   ////////////////////////////////////////////// catalogo tipo usuario

   @Autowired
   public void setUserHassBossRepository(UserHassBossRepository userHassBossRepository) {
      this.userHassBossRepository = userHassBossRepository;
   }

   @Autowired
   public void setHasBossRepository(ProfileHasBossRepository hasBossRepository) {
      this.hasBossRepository = hasBossRepository;
   }

   @Autowired
   public void setWorkShiftRepository(WorkShiftRepository workShiftRepository) {
      this.workShiftRepository = workShiftRepository;
   }

   @Autowired
   public void setGroupRepository(GroupRepository GroupRepository) {
      this.groupRepository = GroupRepository;
   }

   @Autowired
   public void setModulePermissionRepository(ModulePermissionRepository modulePermissionRepository) {
      this.modulePermissionRepository = modulePermissionRepository;
   }

   @Autowired
   public void setImageConverter(UserImageConverter imageConverter) {
      this.imageConverter = imageConverter;
   }

   @Autowired
   public void setUserAppRepository(UserAppRepository userAppRepository) {
      this.userAppRepository = userAppRepository;
   }

   private UserExtraInfoRepository userExtraInfoRepository;

   @Autowired
   public void setUserExtraInfoRepository(UserExtraInfoRepository userExtraInfoRepository) {
      this.userExtraInfoRepository = userExtraInfoRepository;
   }

   private UserHasGroupRepository userHasGroupRepository;

   @Autowired
   public void setUserHasGroupRepository(UserHasGroupRepository userHasGroupRepository) {
      this.userHasGroupRepository = userHasGroupRepository;
   }

   private UserHasPermissionRepository userHasPermissionRepository;

   @Autowired
   public void setUserHasPermissionRepository(UserHasPermissionRepository userHasPermissionRepository) {
      this.userHasPermissionRepository = userHasPermissionRepository;
   }

   private final Map<String, Object> colOrderNames = new HashMap<>();

   {
      colOrderNames.put("idUserApp", "userAppId");
      colOrderNames.put("username", "username");
      colOrderNames.put("email", "email");
      colOrderNames.put("name", "name");
      colOrderNames.put("profileName", "profile.profileName");
   }

   @Transactional(readOnly = false, rollbackFor = {UserAppException.class})
   @Override
   /**
    * {@inheritDoc}
    * */
   public UserAppView createdUserApp(UserAppView userAppView) throws UserAppException {
      try {
         UserApp user = convertViewToEntity(userAppView, null, false);
         logger.info(" - Insertar nuevo usuario: {}", user);

         if (userAppView.getIdTipoUsuario() == null) {
            logger.error("===>>>idTipoUsuario NULO/VACIO");
            UserAppException uae = new UserAppException("Existe un Error", UserAppException.LAYER_DAO, UserAppException.ACTION_VALIDATE);
            uae.addError("idTipoUsuario NULO/VACIO");
            throw uae;
         }

         if (userAppRepository.findByUsername(userAppView.getUsername()) != null) {
            logger.error("===>>>Username DUPLICADO: {}", userAppView.getUsername());
            UserAppException uae = new UserAppException("Existe un error", UserAppException.LAYER_DAO, UserAppException.ACTION_VALIDATE);
            uae.addError("Username DUPLICADO: " + userAppView.getUsername());
            throw uae;
         }

         if (userAppView.getEmail() != null && userAppRepository.findByEmail(userAppView.getEmail()) != null) {
            logger.error("===>>>Correo DUPLICADO: {}", userAppView.getEmail());
            UserAppException uae = new UserAppException("Existe un error -" +
                    " Correo DUPLICADO: " + userAppView.getEmail(), UserAppException.LAYER_DAO, UserAppException.ACTION_VALIDATE);
            uae.addError("Correo DUPLICADO: " + userAppView.getEmail());
            throw uae;
         }

         List<UserApp> usersWithSamePhone = userAppRepository.findByTelefono(userAppView.getTelefono());
         if (!usersWithSamePhone.isEmpty() && userAppView.getIdTipoUsuario() != 3) {
            logger.error("===>>>Teléfono DUPLICADO: {}", userAppView.getTelefono());
            UserAppException uae = new UserAppException("Existe un error -" +
                    " Teléfono DUPLICADO: " + userAppView.getTelefono(), UserAppException.LAYER_DAO, UserAppException.ACTION_VALIDATE);
            uae.addError("Teléfono DUPLICADO: " + userAppView.getTelefono());
            throw uae;
         }

         logger.info(" Previo a insertar el usuario - {} ", user.getUsername());
         userAppRepository.save(user);
         logger.info(" Insertando en tabla user_image ");
         UserImage image = new UserImage();
         image.setUserImageId(user.getUserAppId());
         image.setUserAppId(user.getUserAppId());

         if (userAppView.getIdTipoUsuario() == 3) {
            if (user.getUserImage() != null) {
               if (user.getUserImage().getContentType() != null && !user.getUserImage().getContentType().isEmpty()) {
                  image.setContentType(user.getUserImage().getContentType());
               }
               if (user.getUserImage().getImageContent() != null && user.getUserImage().getImageContent().length > 0) {
                  image.setImageContent(user.getUserImage().getImageContent());
               }
               if (user.getUserImage().getImageContentSmall() != null && user.getUserImage().getImageContentSmall().length > 0) {
                  image.setImageContentSmall(user.getUserImage().getImageContentSmall());
               }
               if (user.getUserImage().getImageName() != null && !user.getUserImage().getImageName().isEmpty()) {
                  image.setImageName(user.getUserImage().getImageName());
               }
            }
         }

         userImageRepository.save(image);
         logger.info("user_image con id {} - {} ", image.getUserAppId(), image.getUserImageId());
         if (userAppView.getIdUsersAssigned() != null && userAppView.getIdUsersAssigned().size() > 0) {
            for (Long idUser : userAppView.getIdUsersAssigned()) {
               UserHasBoss boss = new UserHasBoss();
               boss.setUserApp(userAppRepository.findOne(idUser));
               boss.setUserAppBoss(user);
               userHassBossRepository.save(boss);
            }
         }
         return convertEntityToView(user, Boolean.FALSE, Boolean.FALSE);
      } catch (UserAppException uae) {
         throw uae;
      } catch (DataIntegrityViolationException dte) {
         UserAppException uae = new UserAppException("No fue posible agregar  Usuario", UserAppException.LAYER_DAO, UserAppException.ACTION_INSERT);
         uae.addError("Ocurrio un error al agregar Usuario: " + dte);
         logger.error("===>>>Error al insertar nuevo Usuario - CODE: {} - {}", uae.getExceptionCode(), userAppView, dte);
         throw uae;
      } catch (ConstraintViolationException dte) {
         UserAppException uae = new UserAppException("No fue posible agregar  Usuario", UserAppException.LAYER_DAO, UserAppException.ACTION_INSERT);
         uae.addError("Ocurrio un error al agregar Usuario: " + dte);
         logger.error("===>>>Error al insertar nuevo Usuario - CODE: {} - {}", uae.getExceptionCode(), userAppView, dte);
         throw uae;
      } catch (Exception ex) {
         UserAppException uae = new UserAppException("No fue posible insertar el usuario", UserAppException.LAYER_DAO, UserAppException.ACTION_INSERT);
         uae.addError("Ocurrio un error al insertar el usuario: " + ex);
         logger.error("Error al insertar nuevo usuario - CODE: {} - {}", uae.getExceptionCode(), userAppView, ex);
         throw uae;
      }
   }


   @Override
   @Transactional(readOnly = false, rollbackFor = {UserAppException.class})
   /**
    * {@inheritDoc}
    * */
   public void updateUserApp(UserAppView userAppView) throws UserAppException {
      try {
         if (!userAppRepository.exists(userAppView.getIdUserApp())) {
            UserAppException uae = new UserAppException("No se encuentra en el sistema el usuario.", UserAppException.LAYER_DAO, UserAppException.ACTION_VALIDATE);
            uae.addError("Usuario no encontrado.");
            throw uae;
         }

         UserApp user = userAppRepository.findOne(userAppView.getIdUserApp());


         if (userAppView.getIdTipoUsuario() == null) {
            logger.error("===>>>idTipoUsuario NULO/VACIO");
            UserAppException uae = new UserAppException("Existe un Error", UserAppException.LAYER_DAO, UserAppException.ACTION_VALIDATE);
            uae.addError("idTipoUsuario NULO/VACIO");
            throw uae;
         }

//         SI VIENE VACIO EL PWD SE PONE EL QUE YA EXISTE EN LA DB
         if (userAppView.getPassword() == null || userAppView.getPassword().trim().isEmpty()) {
            userAppView.setPassword(user.getPassword());
         }

//         if (userAppRepository.findByUsername(userAppView.getUsername()) != null) {
//            logger.error("===>>>Username DUPLICADO: {}", userAppView.getUsername());
//            UserAppException uae = new UserAppException("Existe un error", UserAppException.LAYER_DAO, UserAppException.ACTION_VALIDATE);
//            uae.addError("Username DUPLICADO: " + userAppView.getUsername());
//            throw uae;
//         }

         UserApp otroUserName = userAppRepository.findByUsername(userAppView.getUsername());
         if (otroUserName != null) {
            if (user.getUserAppId() != otroUserName.getUserAppId()) {
               logger.error("===>>>Username DUPLICADO: {}", userAppView.getUsername());
               UserAppException uae = new UserAppException("Existe un error", UserAppException.LAYER_DAO, UserAppException.ACTION_VALIDATE);
               uae.addError("Username DUPLICADO: " + userAppView.getUsername());
               throw uae;
            }
         }


//         UserApp user = userAppRepository.findOne(userAppView.getIdUserApp());

         for (UserHasBoss u : user.getUserAppBossList()) {
            logger.info("ub: " + u.getUserAppBoss().getUserAppId());
            logger.info("ua: " + u.getUserApp().getUserAppId());
         }


         if (userAppView.getIdWorkShift() != null) {
            user.setWorkShift(workShiftRepository.findOne(userAppView.getIdWorkShift()));
         }

         ////////////////////////////////////////////// catalogo tipo usuario
         if (userAppView.getIdTipoUsuario() != null) {
            user.setTipoUsuario(tipoUsuarioRepository.findOne(userAppView.getIdTipoUsuario()));
         }
         ////////////////////////////////////////////// catalogo tipo usuario


         List<UserExtraInfo> infoExtra = user.getInfoList().stream().collect(Collectors.toList());//obtener informacion extra y tenerlos en memoria
         List<UserHasGroup> groupList = user.getGroupList().stream().collect(Collectors.toList());//obtener grupos y tenerlos en memoria
         List<UserHasPermission> permissionList = user.getPermissionList().stream().collect(Collectors.toList());//obtener permisos y tenerlos en memoria
         List<UserHasBoss> userHasBossList = user.getUserAppBossList().stream().collect(Collectors.toList());


         user.setInfoList(new ArrayList<>());
         user.setGroupList(new ArrayList<>());
         user.setPermissionList(new ArrayList<>());
         user.setUserAppBossList(new ArrayList<>());

         user = convertViewToEntity(userAppView, user, true);

         List<UserHasBoss> uhbList = new ArrayList<>();
         for (Long idUser : userAppView.getIdUsersAssigned()) {
            UserHasBoss uhb = new UserHasBoss();
            uhb.setUserAppBoss(user);
            uhb.setUserApp(userAppRepository.findOne(idUser));
            uhbList.add(uhb);
         }
         user.setUserAppBossList(uhbList);

         logger.info("User: {}", user);
         for (UserExtraInfo e : infoExtra) {
            if (!existUserExtraInfo(e.getUserExtraInfoId(), user.getInfoList())) {
               logger.debug("delete extra info. {}", e);
               e.setUserApp(null);
               userExtraInfoRepository.delete(e.getUserExtraInfoId());
            }
         }

         for (UserHasGroup g : groupList) {
            if (!existUserHasGroup(g.getUserApp().getUserAppId(), g.getGroup().getGroupId(), user.getGroupList())) {

               userHasGroupRepository.delete(g);
            }
         }
         for (UserHasPermission p : permissionList) {
            if (!existUserHasPermission(p.getUserApp().getUserAppId(), p.getModulePermission().getModulePermissionId(), user.getPermissionList())) {

               userHasPermissionRepository.delete(p);
            }
         }

         for (UserHasBoss b : userHasBossList) {
            logger.info("se recorre los susuarios: " + b.getUserAppBoss().getUserAppId() + " - " + b.getUserApp().getUserAppId());
            if (!existUserHasBoss(b.getUserApp().getUserAppId(), user.getUserAppBossList())) {
               logger.info("se elimino ");
               userHassBossRepository.delete(b);
            }
         }

         logger.info(" - userApp: {}", user);
         userAppRepository.save(user);
      } catch (UserAppException uae) {
         throw uae;
      } catch (DataIntegrityViolationException dte) {
         UserAppException uae = new UserAppException("No fue posible modificar  Usuario", UserAppException.LAYER_DAO, UserAppException.ACTION_INSERT);
         uae.addError("Ocurrio un error al modificar Usuario");
         logger.error("===>>>Error al modificar Usuario - CODE: {} - {}", uae.getExceptionCode(), userAppView, dte);
         throw uae;
      } catch (ConstraintViolationException dte) {
         UserAppException uae = new UserAppException("No fue posible modificar  Usuario", UserAppException.LAYER_DAO, UserAppException.ACTION_INSERT);
         uae.addError("Ocurrio un error al modificar Usuario");
         logger.error("===>>>Error al modificar Usuario - CODE: {} - {}", uae.getExceptionCode(), userAppView, dte);
         throw uae;
      } catch (Exception ex) {
         UserAppException uae = new UserAppException("No fue posible modificar el usuario", UserAppException.LAYER_DAO, UserAppException.ACTION_INSERT);
         uae.addError("Ocurrio un error al modificar el usuario: {}");
         logger.error("Error al modificar el usuario - CODE: {} - {}", uae.getExceptionCode(), userAppView, ex);
         throw uae;
      }
   }

   private Boolean existUserHasBoss(Long idUser, List<UserHasBoss> usersAssigned) {
      Boolean exist = Boolean.FALSE;
      for (UserHasBoss id : usersAssigned) {
         if (id.getUserApp().getUserAppId() == idUser) {
            exist = Boolean.TRUE;
         }
      }
      return exist;
   }

   @Override
   @Transactional(readOnly = false, rollbackFor = {UserAppException.class})
   public void updateUserAppMovil(UserAppView userAppView) throws UserAppException {
      try {
         if (!userAppRepository.exists(userAppView.getIdUserApp())) {
            UserAppException uae = new UserAppException("No se encuentra en el sistema el usuario.", UserAppException.LAYER_DAO, UserAppException.ACTION_VALIDATE);
            uae.addError("Usuario no encontrado.");
            throw uae;
         }

         UserApp user = userAppRepository.findOne(userAppView.getIdUserApp());
         logger.info("user {}", user);
         if (userAppView.getProfileId() != null) {
            user.setProfile(profileRepository.findOne(userAppView.getProfileId()));
         }
         if (userAppView.getIdWorkShift() != null) {
            user.setWorkShift(workShiftRepository.findOne(userAppView.getIdWorkShift()));
         }

         ////////////////////////////////////////////// catalogo tipo usuario
         if (userAppView.getIdTipoUsuario() != null) {
            user.setTipoUsuario(tipoUsuarioRepository.findOne(userAppView.getIdTipoUsuario()));
         }
         ////////////////////////////////////////////// catalogo tipo usuario

         if (userAppView.getPassword() != null && !userAppView.getPassword().isEmpty()) {
            user.setPassword(userAppView.getPassword());
         }
         if (userAppView.getImageProfile() != null) {
            if (user.getUserImage() == null) {
               UserImage image = imageConverter.fromView(userAppView.getImageProfile());
               image.setUserApp(user);
               user.setUserImage(image);
            } else {
               user.getUserImage().setImageName(userAppView.getImageProfile().getImageName());
               try {
                  user.getUserImage().setImageContent(Base64.decodeBase64(userAppView.getImageProfile().getEncodedImageContent()));
               } catch (Exception ex) {
               }
               user.getUserImage().setContentType(userAppView.getImageProfile().getContentType());
            }
         }
         if (userAppView.getEmail() != null && !userAppView.getEmail().isEmpty()) {
            user.setEmail(userAppView.getEmail());
         }
         if (userAppView.getName() != null && !userAppView.getName().isEmpty()) {
            user.setName(userAppView.getName());
         }
         if (userAppView.getStatus() != null && !userAppView.getStatus().isEmpty()) {
            if (userAppView.getStatus().equals("ACTIVO")) {
               user.setStatus(RowStatus.ACTIVO);
            } else {
               user.setStatus(RowStatus.INACTIVO);
            }
         }
         logger.debug(" - userApp: {}", user);
         userAppRepository.save(user);
      } catch (DataIntegrityViolationException dte) {
         UserAppException uae = new UserAppException("No fue posible modificar el usuario, ya se encuentra otro con el mismo username.", UserAppException.LAYER_DAO, UserAppException.ACTION_UPDATE);
         uae.addError("Ocurrio un error al insertar el usuario, ya se encuentra uno con el mismo username.");
         logger.error(ExceptionServiceCode.USER + "Error al modificar usuario - CODE: {} - {}", uae.getExceptionCode(), userAppView, dte);
         throw uae;
      } catch (ConstraintViolationException dte) {
         UserAppException uae = new UserAppException("Ya se encuentra un usuario con el mismo username.", UserAppException.LAYER_DAO, UserAppException.ACTION_UPDATE);
         uae.addError("Ocurrio un error al modificar el usuario.");
         logger.error("Ya se encuentra un usuario con el mismo username. - CODE: {} - {}", uae.getExceptionCode(), userAppView, dte);
         throw uae;
      } catch (UserAppException uae) {
         throw uae;
      } catch (Exception ex) {
         UserAppException uae = new UserAppException("No fue posible modificar el usuario", UserAppException.LAYER_DAO, UserAppException.ACTION_UPDATE);
         logger.error("Error al modificar usuario - CODE: {} - {}", uae.getExceptionCode(), userAppView, ex);
         throw uae;
      }
   }

    @Override
    @Transactional(readOnly = false, rollbackFor = {UserAppException.class})
/**
 * {@inheritDoc}
 */
    public void deleteUserApp(Long idUserApp, String motivo) throws UserAppException {
        try {
            // 1) Cargar una vez y validar existencia
            UserApp user = userAppRepository.findOne(idUserApp);
            if (user == null) {
                UserAppException uae = new UserAppException(
                        "No se encuentra en el sistema el usuario: " + idUserApp,
                        UserAppException.LAYER_DAO,
                        UserAppException.ACTION_VALIDATE
                );
                uae.addError("Usuario no encontrado.");
                throw uae;
            }

            // 2) Validar motivo permitido
            if (motivo == null || (!"Muerte".equalsIgnoreCase(motivo) && !"Suspension".equalsIgnoreCase(motivo))) {
                UserAppException uae = new UserAppException(
                        "Motivo inválido.",
                        UserAppException.LAYER_SERVICE,
                        UserAppException.ACTION_VALIDATE
                );
                uae.addError("El motivo debe ser 'Muerte' o 'Suspension'.");
                throw uae;
            }

            // 3) Inactivar y guardar motivo (UPDATE JPQL)
            int rows = userAppRepository.updateStatusAndMotivo(RowStatus.INACTIVO, motivo, idUserApp);
            if (rows == 0) {
                UserAppException uae = new UserAppException(
                        "No fue posible inactivar el usuario: " + idUserApp,
                        UserAppException.LAYER_DAO,
                        UserAppException.ACTION_UPDATE
                );
                uae.addError("El UPDATE no afectó filas.");
                throw uae;
            }

            // 4) Inactivar info relacionada (evitar NPE)
            List<UserExtraInfo> list = user.getInfoList();
            if (list != null) {
                for (UserExtraInfo u : list) {
                    userExtraInfoRepository.updateStatus(RowStatus.INACTIVO, u.getUserExtraInfoId(), new Date());
                }
            }

        } catch (DataIntegrityViolationException dte) {
            UserAppException uae = new UserAppException(
                    "No fue posible eliminar el usuario " + idUserApp,
                    UserAppException.LAYER_DAO,
                    UserAppException.ACTION_DELETE
            );
            uae.addError("Ocurrió un error al eliminar el usuario");
            logger.error(ExceptionServiceCode.USER + "Error al eliminar usuario - CODE: {} - {}", uae.getExceptionCode(), idUserApp, dte);
            throw uae;
        } catch (ConstraintViolationException dte) {
            UserAppException uae = new UserAppException(
                    "Error al eliminar usuario: " + idUserApp,
                    UserAppException.LAYER_DAO,
                    UserAppException.ACTION_DELETE
            );
            uae.addError("Ocurrió un error al eliminar el usuario.");
            logger.error("Error al eliminar usuario. - CODE: {} - {}", uae.getExceptionCode(), idUserApp, dte);
            throw uae;
        } catch (UserAppException uae) {
            throw uae;
        } catch (Exception ex) {
            UserAppException uae = new UserAppException(
                    "No fue posible eliminar el usuario",
                    UserAppException.LAYER_DAO,
                    UserAppException.ACTION_DELETE
            );
            logger.error("Error al eliminar usuario - CODE: {} - {}", uae.getExceptionCode(), idUserApp, ex);
            throw uae;
        }
    }

    @Override
   /**
    * {@inheritDoc}
    * */
    public Page<UserAppPageView> findUsers(@NotNull String name,
                                           @NotNull Integer page,
                                           @NotNull Integer size,
                                           @NotNull Long idGroup,
                                           String columnOrder,
                                           String orderType,
                                           List<Long> idUsersList) throws UserAppException {
        try {
            // Orden por default seguro
            Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("idUserApp"));
            if (columnOrder != null && orderType != null && colOrderNames.containsKey(columnOrder)) {
                sort = orderType.equalsIgnoreCase("asc")
                        ? new Sort(Sort.Direction.ASC, (String) colOrderNames.get(columnOrder))
                        : new Sort(Sort.Direction.DESC, (String) colOrderNames.get(columnOrder));
            }

            final PageRequest pageRequest = new PageRequest(page, size, sort);
            final String like = Utils.getPatternLike(name);

            // 🔴 CAMBIO CLAVE: filtrar por grupo (y por ids si aplica) DESDE EL REPOSITORIO
            Page<UserApp> pageUsers = (idUsersList != null && !idUsersList.isEmpty())
                    ? userAppRepository.findUserAppByGroupAndIds(
                    idGroup, idUsersList, like, like, like, like, pageRequest
            )
                    : userAppRepository.findUserAppByGroupAndIds(
                    idGroup, null,      like, like, like, like, pageRequest
            );

            // Map directo (sin filtrados posteriores)
            return pageUsers.map(userApp -> {
                ArrayList<GroupView> groups = new ArrayList<>();
                if (userApp.getGroupList() != null) {
                    for (UserHasGroup g : userApp.getGroupList()) {
                        if (g.getGroup() != null && Boolean.TRUE.equals(g.getGroup().getActive())) {
                            GroupView gv = new GroupView();
                            gv.setGroupName(g.getGroup().getGroupName());
                            groups.add(gv);
                        }
                    }
                }
                return new UserAppPageView(
                        userApp.getUserAppId(),
                        userApp.getUsername(),
                        userApp.getEmail(),
                        userApp.getName(),
                        userApp.getProfile() != null ? userApp.getProfile().getProfileName() : null,
                        groups
                );
            });

        } catch (Exception ex) {
            UserAppException uae = new UserAppException("Ocurrio un error al seleccionar lista de usuarios",
                    UserAppException.LAYER_DAO, UserAppException.ACTION_SELECT);
            logger.error("Error al seleccionar lista de usuarios - CODE: {} - {}", uae.getExceptionCode(), ex);
            throw uae;
        }
    }


    @Override
/**
 * {@inheritDoc}
 */
    public Page<UserAppPageView> findUsersAdmin(@NotNull String name,
                                                @NotNull Integer page,
                                                @NotNull Integer size,
                                                @NotNull Long idGroup,
                                                String columnOrder,
                                                String orderType,
                                                List<Long> idUsersList) throws UserAppException {
        try {
            // Orden por default
            Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("idUserApp"));
            if (columnOrder != null && orderType != null && colOrderNames.containsKey(columnOrder)) {
                sort = orderType.equalsIgnoreCase("asc")
                        ? new Sort(Sort.Direction.ASC, (String) colOrderNames.get(columnOrder))
                        : new Sort(Sort.Direction.DESC, (String) colOrderNames.get(columnOrder));
            }

            String likePattern = Utils.getPatternLike(name);
            PageRequest pageRequest = new PageRequest(page, size, sort);

            // 🔴 CAMBIO CLAVE: la consulta YA filtra por grupo en el repositorio (antes de paginar)
            Page<UserApp> pageUsers = userAppRepository.findUserAppAdminByGroup(
                    idGroup,               // <-- nuevo primer parámetro
                    likePattern,           // username
                    likePattern,           // email
                    likePattern,           // name
                    likePattern,           // profile.profileName
                    pageRequest
            );

            // Map directo sin volver a filtrar por grupo en memoria
            return pageUsers.map(userApp -> {
                ArrayList<GroupView> groups = new ArrayList<>();
                if (userApp.getGroupList() != null) {
                    for (UserHasGroup g : userApp.getGroupList()) {
                        if (g.getGroup() != null && Boolean.TRUE.equals(g.getGroup().getActive())) {
                            GroupView gv = new GroupView();
                            gv.setGroupName(g.getGroup().getGroupName());
                            groups.add(gv);
                        }
                    }
                }
                return new UserAppPageView(
                        userApp.getUserAppId(),
                        userApp.getUsername(),
                        userApp.getEmail(),
                        userApp.getName(),
                        userApp.getProfile() != null ? userApp.getProfile().getProfileName() : null,
                        groups
                );
            });

        } catch (Exception ex) {
            UserAppException uae = new UserAppException(
                    "Ocurrio un error al seleccionar lista de usuarios",
                    UserAppException.LAYER_DAO,
                    UserAppException.ACTION_SELECT
            );
            logger.error("Error al seleccionar lista de usuarios - CODE: {} - {}", uae.getExceptionCode(), ex);
            throw uae;
        }
    }

    @Override
   /**
    * {@inheritDoc}
    * */
   public Page<UserAppPageView> searchUsersByGroups(String name, Integer page, Integer size, String columnOrder, String orderType, List<Long> idGroups,
                                                    List<Long> idUsers) throws UserAppException {
      try {
         logger.info("Buscar usuario por nombre: " + name + " page: " + page + " size: " + size + " column order: " + columnOrder + " type: " + orderType + " grupós: " + idGroups);
         logger.info("Users: " + idUsers);
         List<UserAppPageView> userAppPageViews = new ArrayList<>();
         Page<UserApp> userAppPage = null;

         Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("idUserApp"));
         if (columnOrder != null && orderType != null) {
            if (orderType.equalsIgnoreCase("asc"))
               sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(columnOrder));
            else
               sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(columnOrder));
         }
         String likePattern = Utils.getPatternLike(name);
         PageRequest pageRequest = new PageRequest(page, size, sort);

         Specifications<UserApp> spec = Specifications.where(
               (root, query, cb) -> {
                  Join<UserApp, UserHasGroup> mapping = root.join("groupList");
                  Join<UserHasGroup, UserHasGroupId> pk = mapping.join("pk");
                  Join<UserHasGroupId, Group> grupo = pk.join("group");
                  Join<UserApp, Profile> profileJoin = root.join("profile", JoinType.LEFT);
                  Expression<Long> groupIn = grupo.get("groupId");
                  Expression<Long> usersIn = root.get("userAppId");
                  Predicate or = null;
                  or = cb.or(
                        cb.like(cb.lower(root.get("username")), likePattern),
                        cb.like(cb.lower(root.get("email")), likePattern),
                        cb.like(cb.lower(root.get("name")), likePattern),
                        cb.like(cb.lower(profileJoin.get("profileName")), likePattern)
                  );
                  or = or != null ? cb.and(or, groupIn.in(idGroups)) : groupIn.in(idGroups);
                  or = or != null ? cb.and(or, usersIn.in(idUsers)) : usersIn.in(idUsers);
                  query.distinct(true);
                  return or;
               }
         );

         if (spec == null) {
            userAppPage = userAppRepository.findAll(pageRequest);
         } else {
            userAppPage = userAppRepository.findAll(spec, pageRequest);
         }

         userAppPage.getContent().forEach(userApp ->
               userAppPageViews.add(new UserAppPageView(userApp.getUserAppId(), userApp.getUsername(), userApp.getEmail(), userApp.getName(),
                     userApp.getProfile().getProfileName())));

         PageImpl<UserAppPageView> pageUsers = new PageImpl<UserAppPageView>(userAppPageViews, pageRequest, userAppPage.getTotalElements());

         return pageUsers;
      } catch (Exception ex) {
         UserAppException uae = new UserAppException("Ocurrio un error al seleccionar lista de usuarios", UserAppException.LAYER_DAO, UserAppException.ACTION_SELECT);
         logger.error("Error al seleccionar lista de usuarios - CODE: {} - {}", uae.getExceptionCode(), ex);
         throw uae;
      }
   }

   @Override
   /**
    * {@inheritDoc}
    * */
   public UserAppView getDetailsUserApp(Long idUserApp, Boolean image) throws UserAppException {
      try {
         if (!userAppRepository.exists(idUserApp)) {
            UserAppException uae = new UserAppException("No se encuentra en el sistema el usuario: " + idUserApp, UserAppException.LAYER_DAO, UserAppException.ACTION_VALIDATE);
            uae.addError("Usuario no encontrado.");
            throw uae;
         }
         UserApp user = userAppRepository.findOne(idUserApp);
         List<Long> idGroups = new ArrayList<>();
         List<Long> idUsers = new ArrayList<>();

         for (UserHasBoss b : user.getUserAppBossList()) {
            idUsers.add(b.getUserApp().getUserAppId());
         }
         for (UserHasGroup g : user.getGroupList()) {
            idGroups.add(g.getGroup().getGroupId());
         }
         List<UsersAssignedView> usersAssignedViews = getUsersAssignedToBoss(user.getProfile().getProfileId(), idGroups);
         logger.info("userAssigned " + usersAssignedViews);
         for (UsersAssignedView ua : usersAssignedViews) {
            if (setTrue(ua.getIdUser(), idUsers)) {
               ua.setSelected(Boolean.TRUE);
            }
         }
         UserAppView uav = convertEntityToView(user, Boolean.TRUE, image);
         uav.setInfoUsers(usersAssignedViews);
         return uav;
      } catch (DataIntegrityViolationException dte) {
         UserAppException uae = new UserAppException("No fue posible obtener detalles del usuario" + idUserApp, UserAppException.LAYER_DAO, UserAppException.ACTION_SELECT);
         uae.addError("Ocurrio un error al obtener detalles del usuario");
         logger.error(ExceptionServiceCode.USER + "Error al obtener detalles del usuario - CODE: {} - {}", uae.getExceptionCode(), idUserApp, dte);
         throw uae;
      } catch (ConstraintViolationException dte) {
         UserAppException uae = new UserAppException("Error al obtener detalles del usuario: " + idUserApp, UserAppException.LAYER_DAO, UserAppException.ACTION_SELECT);
         uae.addError("Ocurrio un error al obtener detalles del usuario.");
         logger.error("Error al obtener detalles del usuario. - CODE: {} - {}", uae.getExceptionCode(), idUserApp, dte);
         throw uae;
      } catch (UserAppException uae) {
         throw uae;
      } catch (Exception ex) {
         UserAppException uae = new UserAppException("No fue posible obtener detalles del usuario", UserAppException.LAYER_DAO, UserAppException.ACTION_SELECT);
         logger.error("Error al obtener detalles del usuario - CODE: {} - {}", uae.getExceptionCode(), idUserApp, ex);
         throw uae;
      }
   }

   // GGR20200709 Agrego método para buscar horario por idUser
   @Override
   public Map<String, Object> findHorarioByIdUser(Long idUserApp) throws UserAppException {
      logger.info("===>>>findHorarioByIdUser() con ID usuario: {}", idUserApp);
      try {
         Map<String, Object> horarios = new HashMap<String, Object>();
         Object horarioObj = userAppRepository.findHorarioByIdUser(idUserApp);
         if (horarioObj != null) {
            horarios.put("horarios", (String) horarioObj);
         }
         logger.info("===>>>findHorarioByIdUser() tengo horarios: {}", horarios);
         return horarios;
      } catch (Exception ex) {
         UserAppException uce = new UserAppException("Ocurrio un error al pedir horario del usuario", UserAppException.LAYER_DAO, UserAppException.ACTION_SELECT);
         logger.error("Error al horario del usuario - CODE: {} - id: {}", uce.getExceptionCode(), idUserApp, ex);
         throw uce;
      }
   }


   private Boolean setTrue(Long idUser, List<Long> idUsers) {
      Boolean likeTrue = Boolean.FALSE;
      if (idUsers != null && idUsers.size() > 0) {
         for (Long id : idUsers) {
            if (id == idUser) {
               likeTrue = Boolean.TRUE;
            }
         }
      }
      return likeTrue;
   }

   private UserAppView convertEntityToView(UserApp user, Boolean completeConversion, Boolean image) {
      UserAppView view = new UserAppView();
      view.setStatus(user.getStatus().toString());
      view.setCreatedDate(user.getCreatedDate());
      view.setIdUserApp(user.getUserAppId());
      view.setUsername(user.getUsername());
      view.setProfileId(user.getProfile().getProfileId());
      view.setPassword(null);
      view.setName(user.getName());
      view.setEmail(user.getEmail());
      view.setCustomerId(user.getCustomerId());
      if (user.getWorkShift() != null) {
         view.setIdWorkShift(user.getWorkShift().getIdWorkShift());
         view.setNameWorkShift(user.getWorkShift().getName());
      }

      ////////////////////////////////////////////// catalogo tipo usuario
      if (user.getTipoUsuario() != null) {
         view.setIdTipoUsuario(user.getTipoUsuario().getIdTipoUsuario());
         view.setDescripcion(user.getTipoUsuario().getDescripcion());
      }
      ////////////////////////////////////////////// catalogo tipo usuario

      view.setConnectionStatus(user.getConnectionStatus());
      List<UserExtraInfoView> list = new ArrayList<>();
      List<Long> groups = new ArrayList<>();
      List<Long> permissions = new ArrayList<>();
      view.setImageProfile(null);
      view.setProfileName(user.getProfile().getProfileName());
      if (completeConversion) {
         for (UserExtraInfo u : user.getInfoList()) {
            UserExtraInfoView extraInfoView = new UserExtraInfoView();
            extraInfoView.setStatus(u.getStatus().toString());
            extraInfoView.setUserExtraInfoId(u.getUserExtraInfoId());
            extraInfoView.setValue(u.getValue());
            extraInfoView.setKey(u.getKey());
            extraInfoView.setCreatedDate(u.getCreatedDate());
            extraInfoView.setModifiedDate(u.getModifiedDate());
            list.add(extraInfoView);
         }
         view.setExtraList(list);

         for (UserHasGroup g : user.getGroupList()) {
            groups.add(g.getGroup().getGroupId());
            GroupView gv = new GroupView();
            gv.setGroupId(g.getGroup().getGroupId());
            gv.setGroupName(g.getGroup().getGroupName());
            view.getGroups().add(gv);
         }
         view.setGroupList(groups);

         for (UserHasPermission p : user.getPermissionList()) {
            permissions.add(p.getModulePermission().getModulePermissionId());
         }
         view.setPermissionsList(permissions);

         if (user.getUserImage() != null) {
            if (image != null && image) {
               view.setImageProfile(imageConverter.fromEntity(user.getUserImage(), null));
            } else {
               view.setImageProfile(imageConverter.fromEntity(user.getUserImage(), Boolean.TRUE));
            }

         }

      }

      return view;
   }


   private UserApp convertViewToEntity(UserAppView userAppView, UserApp userApp, Boolean update) {
      if (userApp == null) {
         userApp = new UserApp();
      }
      if (update) {
         if (userAppView.getStatus().equals("ACTIVO")) {
            userApp.setStatus(RowStatus.ACTIVO);
         } else {
            userApp.setStatus(RowStatus.INACTIVO);
         }
         userApp.setUserAppId(userAppView.getIdUserApp());
         if (userAppView.getPassword() == null || userAppView.getPassword().isEmpty()) {
            userApp.setPassword(userApp.getPassword());
         } else {
            userApp.setPassword(userAppView.getPassword());
         }
         if (userAppView.getPassword2() == null || userAppView.getPassword2().isEmpty())
            userApp.setPassword2(userApp.getPassword2());
         else
            userApp.setPassword2(userAppView.getPassword2());
         if (userAppView.getPassword3() == null || userAppView.getPassword3().isEmpty())
            userApp.setPassword3(userApp.getPassword3());
         else
            userApp.setPassword3(userAppView.getPassword3());
         if (userAppView.getTelefono() == null || userAppView.getTelefono().isEmpty())
            userApp.setTelefono(userApp.getTelefono());
         else
            userApp.setTelefono(userAppView.getTelefono());
      } else {
         userApp.setStatus(RowStatus.ACTIVO);
         userApp.setCreatedDate(new Date());
         userApp.setPassword(userAppView.getPassword());
         userApp.setPassword2(userAppView.getPassword2());
         userApp.setPassword3(userAppView.getPassword3());
         userApp.setTelefono(userAppView.getTelefono());
      }
      userApp.setUsername(userAppView.getUsername());
      userApp.setName(userAppView.getName());
      userApp.setCustomerId(userAppView.getCustomerId());
      userApp.setEmail(userAppView.getEmail());
      userApp.setProfile(profileRepository.findOne(userAppView.getProfileId()));
      if (userAppView.getIdWorkShift() != null) {
         userApp.setWorkShift(workShiftRepository.findOne(userAppView.getIdWorkShift()));
      }

      ////////////////////////////////////////////// catalogo tipo usuario
      if (userAppView.getIdTipoUsuario() != null) {
         userApp.setTipoUsuario(tipoUsuarioRepository.findOne(userAppView.getIdTipoUsuario()));
      }
      ////////////////////////////////////////////// catalogo tipo usuario


      if (userAppView.getExtraList() != null && !userAppView.getExtraList().isEmpty()) {
         for (UserExtraInfoView uev : userAppView.getExtraList()) {
            UserExtraInfo userExtraInfo = new UserExtraInfo();
            userExtraInfo.setKey(uev.getKey());
            userExtraInfo.setValue(uev.getValue());
            userExtraInfo.setUserApp(userApp);
            if (update) {
               if (uev.getStatus().equals("ACTIVO")) {
                  userExtraInfo.setStatus(RowStatus.ACTIVO);
               } else {
                  userExtraInfo.setStatus(RowStatus.INACTIVO);
               }

               if (uev.getUserExtraInfoId() != null) {
                  userExtraInfo.setUserExtraInfoId(uev.getUserExtraInfoId());
               }
            } else {
               userExtraInfo.setStatus(RowStatus.ACTIVO);
            }
            userExtraInfo.setCreatedDate(new Date());
            userExtraInfo.setModifiedDate(new Date());
            userApp.getInfoList().add(userExtraInfo);
         }
      }
      if (userAppView.getGroupList() != null && !userAppView.getGroupList().isEmpty()) {
         for (Long idGroup : userAppView.getGroupList()) {
            Group group = groupRepository.findOne(idGroup);
            UserHasGroup userHasGroup = new UserHasGroup();
            userHasGroup.setCreatedDate(new Date());
            userHasGroup.setUserApp(userApp);
            userHasGroup.setGroup(group);
            logger.debug(" - id: {}", userHasGroup);
            userApp.getGroupList().add(userHasGroup);
         }
      }
      if (userAppView.getPermissionsList() != null && !userAppView.getPermissionsList().isEmpty()) {
         for (Long idPermission : userAppView.getPermissionsList()) {
            ModulePermission permission = modulePermissionRepository.findOne(idPermission);

            UserHasPermission userHasPermission = new UserHasPermission();
            userHasPermission.setUserApp(userApp);
            userHasPermission.setModulePermission(permission);
            logger.debug(" - hasPermission: {}", userHasPermission);
            userApp.getPermissionList().add(userHasPermission);
         }
      }
      if (userAppView.getImageProfile() != null) {
         //agregar imagen de perfil a usuario
         if (userApp.getUserImage() == null) {
            UserImage image = imageConverter.fromView(userAppView.getImageProfile());
            image.setUserApp(userApp);
            userApp.setUserImage(image);
         } else {
            userApp.getUserImage().setImageName(userAppView.getImageProfile().getImageName());
            try {
               userApp.getUserImage().setImageContent(Base64.decodeBase64(userAppView.getImageProfile().getEncodedImageContent()));
               userApp.getUserImage().setImageContentSmall(Base64.decodeBase64(userAppView.getImageProfile().getEncodedImageContentSmall()));
            } catch (Exception ex) {
            }
            userApp.getUserImage().setContentType(userAppView.getImageProfile().getContentType());
         }

      }
      logger.debug("return userapp: {}", userApp);
      return userApp;

   }

   private Boolean existUserExtraInfo(Long idUserExtraInfo, List<UserExtraInfo> extraInfoList) {
      List<UserExtraInfo> infoList = new ArrayList<>();
      for (UserExtraInfo u : extraInfoList) {
         if (u.getUserExtraInfoId() != null) {
            if (u.getUserExtraInfoId() == idUserExtraInfo) {
               infoList.add(u);
            }
         }
      }
      return !infoList.isEmpty();
   }

   private Boolean existUserHasGroup(Long idUserApp, Long idGroup, List<UserHasGroup> groupList) {
      List<UserHasGroup> list = groupList.stream().filter(userHasGroup -> userHasGroup.getUserApp().getUserAppId().compareTo(idUserApp) == 0
            && userHasGroup.getGroup().getGroupId().compareTo(idGroup) == 0).collect(Collectors.toList());
      return !list.isEmpty();
   }

   private Boolean existUserHasPermission(Long idUserApp, Long idPermission, List<UserHasPermission> permissionList) {
      List<UserHasPermission> list = permissionList.stream().filter(userHasPermission -> userHasPermission.getUserApp().getUserAppId().compareTo(idUserApp) == 0
            && userHasPermission.getModulePermission().getModulePermissionId().compareTo(idPermission) == 0).collect(Collectors.toList());
      return !list.isEmpty();
   }


   @Override
   public UserApp findByUsername(@NotNull String username) throws UserAppException {
      try {
         return userAppRepository.findByUsername(username);
      } catch (Exception ex) {
         UserAppException uae = new UserAppException("Ocurrio un error al seleccionar usuario", UserAppException.LAYER_DAO, UserAppException.ACTION_SELECT);
         logger.error("Error al seleccionar lista de usuarios - CODE: {} - username: {}", uae.getExceptionCode(), username, ex);
         throw uae;
      }
   }

   @Override
   @Transactional(readOnly = false, rollbackFor = UserAppException.class)
   public void updateUsersConnectionStatus(String username, String status) throws UserAppException {
      try {
         UserApp userApp = userAppRepository.findByUsername(username);
         if (userApp != null) {
            userApp.setConnectionStatus(status);
         } else {
            logger.warn("El usuario no existe: {}", username);
         }
      } catch (Exception ex) {
         UserAppException uae = new UserAppException("Ocurrio un error al actualizar usuario", UserAppException.LAYER_DAO, UserAppException.ACTION_UPDATE);
         logger.error("Error al actualizar usuario - CODE: {} - username: {} -estado: {}", uae.getExceptionCode(), username, status, ex);
         throw uae;
      }
   }

   @Override
   public List<UserAppView> findByGroups(List<Long> groups, List<Long> idUsers) throws UserAppException {
      try {
         List<UserAppView> userAppViews = new ArrayList<>();
         userHasGroupRepository.findDistinctByPk_Group_GroupIdIn(groups).stream().forEach(userHasGroup -> {
            if (!findUser(userHasGroup.getUserApp().getUserAppId(), userAppViews) && userAssigned(userHasGroup.getUserApp().getUserAppId(), idUsers)) {
               userAppViews.add(convertEntityToView(userHasGroup.getUserApp(), Boolean.TRUE, null));
            }
         });

         return userAppViews;
      } catch (Exception ex) {
         UserAppException uae = new UserAppException("Ocurrio un error al seleccionar lista de usuarios por grupo", UserAppException.LAYER_DAO, UserAppException.ACTION_UPDATE);
         logger.error("Error al obtener usuarios- CODE: {} - grupos: {}", uae.getExceptionCode(), groups, ex);
         throw uae;
      }
   }

   private Boolean findUser(Long idUserApp, List<UserAppView> usersView) {
      Boolean found = Boolean.FALSE;
      for (UserAppView view : usersView) {
         if (view.getIdUserApp() == idUserApp) {
            found = Boolean.TRUE;
         }
      }
      return found;
   }

   private Boolean userAssigned(Long idUserApp, List<Long> idUsers) {
      Boolean found = Boolean.FALSE;
      for (Long id : idUsers) {
         if (id == idUserApp) {
            found = Boolean.TRUE;
         }
      }
      return found;
   }

   @Override
   public List<UsersAssignedView> getUsersAssignedToBoss(Long idProfile, List<Long> groups) throws UserAppException {
      try {
         List<UsersAssignedView> usersAssignedViews = new ArrayList<>();
         List<Long> idProfilesAssigned = hasBossRepository.findProfileAssignedToBoss(idProfile);
         logger.info("profiles: " + idProfilesAssigned);
         if (idProfilesAssigned != null && idProfilesAssigned.size() > 0) {
            if (groups != null && groups.size() > 0 && idProfilesAssigned != null && idProfilesAssigned.size() > 0) {
               Specifications<UserApp> spec = Specifications.where(
                     (root, query, cb) -> {
                        Join<UserApp, UserHasGroup> groupList = root.join("groupList");
                        Join<UserHasGroup, UserHasGroupId> pk = groupList.join("pk");
                        Join<UserHasGroupId, Group> group = pk.join("group");
                        Join<UserApp, Profile> profileJoin = root.join("profile", JoinType.LEFT);
                        Expression<Long> groupIn = group.get("groupId");
                        Expression<Long> profileIn = profileJoin.get("profileId");
                        Predicate q = null;
                        q = q != null ? cb.and(q, groupIn.in(groups)) : groupIn.in(groups);
                        q = q != null ? cb.and(q, profileIn.in(idProfilesAssigned)) : profileIn.in(idProfilesAssigned);
                        query.distinct(true);
                        return q;
                     }
               );

               List<UserApp> userAppList = userAppRepository.findAll(spec);

               for (UserApp u : userAppList) {
                  UsersAssignedView view = new UsersAssignedView();
                  view.setIdUser(u.getUserAppId());
                  view.setNameUser(u.getName());
                  view.setSelected(Boolean.FALSE);
                  usersAssignedViews.add(view);
               }
            }
         }
         return usersAssignedViews;
      } catch (Exception ex) {
         UserAppException uae = new UserAppException("Ocurrio un error al seleccionar lista de usuarios asignados al perfil: " + idProfile, UserAppException.LAYER_DAO, UserAppException.ACTION_UPDATE);
         logger.error("Error al obtener usuarios- CODE: {} - grupos: {}", uae.getExceptionCode(), groups, ex);
         throw uae;
      }
   }

   public List<InfoBasicResponseView> getInfoBasicUsers(InfoBasicRequestView view) throws UserAppException {
      try {
         List<InfoBasicResponseView> infoBasicResponseViews = new ArrayList<>();
         for (Long idUser : view.getIdUsers()) {
            UserApp user = userAppRepository.findOne(idUser);
            InfoBasicResponseView info = new InfoBasicResponseView();
            info.setIdUser(user.getUserAppId());
            info.setName(user.getName());
            info.setUsername(user.getUsername());
            if (user.getUserImage() != null && user.getUserImage().getImageContentSmall() != null) {
               UserImageView imageView = new UserImageView();
               imageView.setContentType(user.getUserImage().getContentType());
               try {
                  imageView.setEncodedImageContentSmall(Base64.encodeBase64String(user.getUserImage().getImageContentSmall()));
               } catch (Exception ex) {
               }
               imageView.setImageName(user.getUserImage().getImageName());
               info.setImage(imageView);
            }
            infoBasicResponseViews.add(info);
         }
         return infoBasicResponseViews;
      } catch (Exception ex) {
         UserAppException uae = new UserAppException("Ocurrio un error al seleccionar lista de usuarios para la información básica: " + view.getIdUsers(), UserAppException.LAYER_DAO, UserAppException.ACTION_UPDATE);
         logger.error("Error al obtener lista de usuarios para la información básica: - CODE: {} - users: {}", uae.getExceptionCode(), view.getIdUsers(), ex);
         throw uae;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {UserAppException.class})
   @Override
   public void deleteRollBack(Long idUserApp) throws UserAppException {
      try {
         logger.info("deleteRollBack() - ejecutando userHasPermissionRepository");
         userHasPermissionRepository.deleteByIdUserApp(idUserApp);
         logger.info("deleteRollBack() - ejecutando userHasGroupRepository");
         userHasGroupRepository.deleteByIdUserApp(idUserApp);
         logger.info("deleteRollBack() - ejecutando userImageRepository");
         userImageRepository.deleteByUserAppId(idUserApp);
         logger.info("deleteRollBack() - ejecutando userAppRepository");
         userAppRepository.deleteByUserAppId(idUserApp);
      } catch (Exception ex) {
         UserAppException uae = new UserAppException("Ocurrio un error al hacer ROLLBACK de idUserApp: {} " + idUserApp, UserAppException.LAYER_DAO, UserAppException.ACTION_UPDATE);
         logger.error("Ocurrio un error al hacer ROLLBACK de idUserApp: {}, exception: {}", idUserApp, ex);
         throw uae;
      }
   }

   @Override
   public List<Long> obtenerIdUserAppDeGrupos(List<Integer> grupos) throws UserAppException {
      try {
         List<BigInteger> idUsuariosBigInt = userHasGroupRepository.obtenerIdUserAppGrupos(grupos);
         List<Long> idUsuariosLong = new ArrayList<>();
         for (BigInteger temp : idUsuariosBigInt) {
            idUsuariosLong.add(temp.longValue());
         }
         return idUsuariosLong;
      } catch (Exception ex) {
         UserAppException uae = new UserAppException("Ocurrio un error al obtener lista idUsuarios de grupos: {} " + grupos, UserAppException.LAYER_DAO, UserAppException.ACTION_UPDATE);
         logger.error("Ocurrio un error al obtener lista idUsuarios de grupos: {}, exception: {}", grupos, ex);
         throw uae;
      }

   }
   
    // Sre22052020 Nuevo para buscar view por username
   @Override
   public UserAppView findViewByUsername(@NotNull String username) throws UserAppException {
      try {
            UserApp userApp = userAppRepository.findByUsername(username);
            if (userApp == null) {
                UserAppView view = new UserAppView();
                view.setStatus("N/A");
                return view;
            }
            return convertEntityToView(userApp, Boolean.TRUE, Boolean.FALSE);
      } catch (Exception ex) {
         UserAppException uae = new UserAppException("Ocurrio un error al seleccionar usuario", UserAppException.LAYER_DAO, UserAppException.ACTION_SELECT);
         logger.error("Error al seleccionar lista de usuarios - CODE: {} - username: {}", uae.getExceptionCode(), username, ex);
         throw uae;
      }
   }

   @Override
   @Transactional(readOnly = false, rollbackFor = UserAppException.class)
   public void resetUserPassword(ResetPasswordView resetPasswordView) throws UserAppException {
      try {
         if (!userAppRepository.exists(resetPasswordView.getIdUserApp())) {
            UserAppException uae = new UserAppException("No se encuentra en el sistema el usuario.", UserAppException.LAYER_SERVICE, UserAppException.ACTION_VALIDATE);
            uae.addError("Usuario no encontrado.");
            throw uae;
         }
         UserApp userApp = userAppRepository.findByUserAppId(resetPasswordView.getIdUserApp());
         logger.info("Usuario para actualizar contraseña - {}", userApp.getUsername());
         if(userApp.getEmail() == null || userApp.getEmail().trim().isEmpty())
            throw new UserAppException("El usuario no tiene correo registrado", UserAppException.LAYER_SERVICE, UserAppException.ACTION_SELECT);
         userApp.setPassword(resetPasswordView.getNewPassword());
         userAppRepository.save(userApp);
         logger.info("Contraseña actualizada - {}", userApp.getPassword());

         Map<String,String> replacements = new HashMap<>();
         replacements.put("recoverEmail",userApp.getEmail());
         replacements.put("recoverHash",resetPasswordView.getPassword());
         String message = "<!DOCTYPE html>\n" +
                 "<html lang=\"en\">\n" +
                 "<head>\n" +
                 "    <meta charset=\"UTF-8\">\n" +
                 "    <title>Generación de nueva contraseña</title>\n" +
                 "</head>\n" +
                 "<body>\n" +
                 "    <h2>Generación de nueva contraseña</h2>\n" +
                 "    <p></p>\n" +
                 "    <p>El presente correo es para notificar la generación de nueva contraseña para el usuario <strong>{{recoverEmail}}</strong></p>\n" +
                 "    <p>Contraseña nueva:  {{recoverHash}}</p>\n" +
                 "</body>\n" +
                 "</html>";
         for (Map.Entry<String, String> entry : replacements.entrySet())
            message = message.replace("{{" + entry.getKey() + "}}", entry.getValue());
         sendEmail("Generación de nueva contraseña",message, userApp.getEmail());
      } catch (Exception ex) {
         UserAppException uae = new UserAppException("Ocurrio un error al actualizar la contraseña", UserAppException.LAYER_SERVICE, UserAppException.ACTION_UPDATE);
         uae.addError(ex.getMessage());
         logger.error("Error al actualizar la contraseña - CODE: {} - username: {} - error - {}", uae.getExceptionCode(), resetPasswordView.getIdUserApp(), ex.getMessage());
         throw uae;
      }
   }

   public void sendEmail(String title, String message, String recipient) {
      try {
         Properties props = new Properties();
         props.put("mail.smtp.auth", "true");
         props.put("mail.smtp.starttls.enable", "true");
         props.put("mail.smtp.ssl.enable", "true");
         props.put("mail.smtp.host", "mail.telemedicina.lat");
         props.put("mail.smtp.port", "465");

         Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication("noresponder@telemedicina.lat", "Z{hcvq1x}T9.");
            }
         });
         Message msg = new MimeMessage(session);
         msg.setFrom(new InternetAddress("noresponder@telemedicina.lat", false));

         msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
         msg.setSubject(title);
         msg.setContent(message, "text/html");
         msg.setSentDate(new Date());

         MimeBodyPart messageBodyPart = new MimeBodyPart();
         messageBodyPart.setContent(message, "text/html");

         Multipart multipart = new MimeMultipart();
         multipart.addBodyPart(messageBodyPart);
         MimeBodyPart attachPart = new MimeBodyPart();
         Transport.send(msg);
         logger.info("Correo enviado");
      }catch (Exception ex) {
         logger.error("Error al enviar correo - {}",ex.getMessage());
      }
   }
}
