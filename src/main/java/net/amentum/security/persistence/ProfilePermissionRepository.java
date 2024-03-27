package net.amentum.security.persistence;

import net.amentum.security.model.ProfilePermission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dev06
 */
public interface ProfilePermissionRepository extends JpaRepository<ProfilePermission,Long> {
}
