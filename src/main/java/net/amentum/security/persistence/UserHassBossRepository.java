package net.amentum.security.persistence;

import net.amentum.security.model.UserHasBoss;
import net.amentum.security.model.UserHasBossId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author marellano on 03/07/18.
 */
@Repository
public interface UserHassBossRepository extends JpaRepository<UserHasBoss, UserHasBossId>,JpaSpecificationExecutor {
}
