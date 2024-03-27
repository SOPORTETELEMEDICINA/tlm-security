package net.amentum.security.persistence;

import net.amentum.security.model.UserHasPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author marellano on 26/04/17.
 */
@Repository
public interface UserHasPermissionRepository extends JpaRepository<UserHasPermission, Long> {

   @Modifying
   @Query(value = "DELETE FROM user_has_permission uap WHERE uap.id_user_app = :idUserApp", nativeQuery = true)
   Integer deleteByIdUserApp(@NotNull @Param("idUserApp") Long idUserApp) throws Exception;

}
