package net.amentum.security.persistence;

import net.amentum.security.model.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by dev06 on 11/04/17.
 */
@Repository
public interface ModuleRepository extends JpaRepository<Module,Long> {

    @Query("SELECT m FROM Module m WHERE LOWER(m.moduleName) LIKE :name")
    Page<Module> findByNameLike(@Param("name")String name, Pageable pageable) throws Exception;

    @Modifying
    @Query("DELETE FROM ModulePermission p WHERE p.modulePermissionId=:id")
    void deletePermission(@Param("id")Long id) throws  Exception;

}
