package net.amentum.security.persistence;

import net.amentum.security.model.GroupCrud;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupCrudRepository extends JpaRepository <GroupCrud, Long> {

    @Query(value = "SELECT gc FROM GroupCrud gc WHERE gc.idGroup = :gid")
    Page<GroupCrud> findAllGroupCrudByGroup(@Param("gid") Integer gid, Pageable pageable) throws Exception;

    @Query(value = "SELECT gc.image FROM GroupCrud gc WHERE gc.idGroup = :gid AND gc.color = :color")
    String findImageGroup(@Param("gid") Integer gid, @Param("color") String color) throws Exception;
}
