package net.amentum.security.persistence;

import net.amentum.security.model.RowStatus;
import net.amentum.security.model.UserApp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author  marellano
 */
@Repository
public interface UserAppRepository extends JpaRepository<UserApp, Long>, JpaSpecificationExecutor {

    /**
     * Método para modificar el status de acuerdo al id del usuario.
     * @param idUserApp identificador único del usuario.
     * @param status estatus nuevo del usuario.
     * */
    @Query("UPDATE UserApp ua SET ua.status =:status WHERE ua.userAppId =:idUserApp")
    @Modifying
    void updateStatus(@Param("status")RowStatus status, @Param("idUserApp") Long idUserApp) throws Exception;

    /**
     * Método para obtener todos los usuarios o buscar un usuario
     * @param username  nombre de usuario.
     * @param email email del usuario.
     * @param name nombre del usuario(s).
     * @param profile perfil de usuario(s).
     * @param pageable configuracion de la paginacion.
     * */
    @Query("SELECT u FROM UserApp u WHERE u.userAppId in (:idUsersAssigned) AND ( LOWER(u.username) LIKE :username OR LOWER(u.email) LIKE :email OR LOWER(u.name) LIKE :name OR LOWER(u.profile.profileName) LIKE :profile)")
    Page<UserApp> findUserApp(@Param("username")String username, @Param("email")String email,
                              @Param("name")String name, @Param("profile")String profile, @Param("idUsersAssigned")List<Long> idUsersAssigned,
                              Pageable pageable) throws Exception;

    /**
     * Método para obtener detalles de usuario utilizando el 'username'
     * @param username usuario a obtener en base de datos
     * @return Usuario obtenido en base de datos
     * @throws Exception si no es poisble obtener el usuario
     * */
    UserApp findByUsername(@NotNull String username) throws Exception;

    UserApp findByEmail(@NotNull String email) throws Exception;

    UserApp findByCurp(@NotNull String curp) throws Exception;

    @Query("SELECT u FROM UserApp u WHERE UPPER(u.email) = UPPER(:email)")
    UserApp findByEmailUpper(@Param("email") String email) throws Exception;

    List<UserApp> findByTelefono(@NotNull String telefono) throws Exception;

    UserApp findByUserAppId(@NotNull Long idUserApp) throws Exception;

    Integer deleteByUserAppId(@NotNull Long idUserApp) throws Exception;

    @Modifying
    @Query("UPDATE UserApp u SET u.status = :status, u.motivo = :motivo WHERE u.userAppId = :idUserApp")
    int  updateStatusAndMotivo(@Param("status") RowStatus status,
                               @Param("motivo") String motivo,
                               @Param("idUserApp") Long idUserApp) throws Exception;


    /**
     * Método para obtener todos los usuarios o buscar un usuario cuando se es Admin Sre22052020
     * @param username  nombre de usuario.
     * @param email email del usuario.
     * @param name nombre del usuario(s).
     * @param profile perfil de usuario(s).
     * @param pageable configuracion de la paginacion.
     * */
    @Query("SELECT u FROM UserApp u WHERE ( LOWER(u.username) LIKE :username OR LOWER(u.email) LIKE :email OR LOWER(u.name) LIKE :name OR LOWER(u.profile.profileName) LIKE :profile)")
    Page<UserApp> findUserAppAdmin(@Param("username")String username, @Param("email")String email,
                              @Param("name")String name, @Param("profile")String profile,
                              Pageable pageable) throws Exception;

    // GGR20200709 Agrego método para buscar horario por idUser
    @Query(value="select STRING_AGG (case when day = 0 then 'Domingo' when day = 1 then 'Lunes' when day = 2 then 'Martes' " +
            " when day = 3 then 'Miercoles' when day = 4 then 'Jueves' when day = 5 then 'Viernes' " +
            " when day = 6 then 'Sabado' else '' end " +
            "|| ' ' || to_char(start_time, 'HH24:MI') || '- ' || to_char(end_time, 'HH24:MI'), ' , ') as horario " +
            "from user_app usu " +
            "join work_shift ws on ws.id_work_shift = usu.work_shift_id_work_shift " +
            "join shift_hour hor on ws.id_work_shift = hor.id_work_shift " +
            "where usu.id_user_app = :idUserApp", nativeQuery = true)
    String findHorarioByIdUser (@Param("idUserApp")Long idUserApp) throws Exception;

    @Query("SELECT DISTINCT u FROM UserApp u " +
            "JOIN u.groupList ug " +
            "JOIN ug.pk.group g " +                           // <- aquí el cambio
            "WHERE g.groupId = :idGroup AND g.active = TRUE " +
            "AND (LOWER(u.username) LIKE LOWER(:username) " +
            "  OR LOWER(u.email) LIKE LOWER(:email) " +
            "  OR LOWER(u.name) LIKE LOWER(:name) " +
            "  OR LOWER(u.profile.profileName) LIKE LOWER(:profile))")
    Page<UserApp> findUserAppAdminByGroup(@Param("idGroup") Long idGroup,
                                          @Param("username") String username,
                                          @Param("email") String email,
                                          @Param("name") String name,
                                          @Param("profile") String profile,
                                          Pageable pageable);

    @Query("SELECT DISTINCT u FROM UserApp u " +
            "JOIN u.groupList ug " +
            "JOIN ug.pk.group g " +                           // <- igual aquí
            "WHERE g.groupId = :idGroup AND g.active = TRUE " +
            "AND ( (:ids) IS NULL OR u.userAppId IN :ids ) " +
            "AND (LOWER(u.username) LIKE LOWER(:username) " +
            "  OR LOWER(u.email) LIKE LOWER(:email) " +
            "  OR LOWER(u.name) LIKE LOWER(:name) " +
            "  OR LOWER(u.profile.profileName) LIKE LOWER(:profile))")
    Page<UserApp> findUserAppByGroupAndIds(@Param("idGroup") Long idGroup,
                                           @Param("ids") List<Long> ids,
                                           @Param("username") String username,
                                           @Param("email") String email,
                                           @Param("name") String name,
                                           @Param("profile") String profile,
                                           Pageable pageable);



}
