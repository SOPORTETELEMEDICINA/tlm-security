package net.amentum.security.persistence;

import net.amentum.security.model.ProfileHasBoss;
import net.amentum.security.model.ProfileHasBossId;
import net.amentum.security.model.UserHasGroup;
import net.amentum.security.model.UserHasGroupId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author marellano on 03/07/18.
 */
@Repository
public interface ProfileHasBossRepository extends JpaRepository<ProfileHasBoss, ProfileHasBossId>,JpaSpecificationExecutor {



    @Query("SELECT p.pk.profileId.profileId FROM ProfileHasBoss p where p.pk.boss.profileId = :idBoss")
    List<Long> findProfileAssignedToBoss(@Param("idBoss")Long idBoss) throws Exception;
}
