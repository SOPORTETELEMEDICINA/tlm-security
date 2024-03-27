package net.amentum.security.persistence;

import net.amentum.security.model.UserHasGroup;
import net.amentum.security.model.UserHasGroupId;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.math.BigInteger;

/**
 * @author marellano on 26/04/17.
 */
@Repository
public interface UserHasGroupRepository extends JpaRepository<UserHasGroup, UserHasGroupId>, JpaSpecificationExecutor {

   List<UserHasGroup> findDistinctByPk_Group_GroupIdIn(List<Long> groupIdList);

   @Modifying
   @Query(value = "DELETE FROM user_has_group uag WHERE uag.id_user_app = :idUserApp", nativeQuery = true)
   Integer deleteByIdUserApp(@NotNull @Param("idUserApp") Long idUserApp) throws Exception;


   @Query(value = "select distinct(uhg.id_user_app) from user_has_group uhg where uhg.id_group in(:grupos)", nativeQuery = true)
   List<BigInteger> obtenerIdUserAppGrupos(@NotNull @Param("grupos") List<Integer> grupos) throws Exception;

}
