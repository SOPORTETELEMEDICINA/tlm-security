package net.amentum.security.persistence;

import net.amentum.security.model.ModulePermission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by dev06 on 25/04/17.
 */
public interface ModulePermissionRepository extends JpaRepository<ModulePermission,Long> {
}
