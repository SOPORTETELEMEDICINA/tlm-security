package net.amentum.security.persistence;
import net.amentum.security.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * @author Victor de la Cruz
 * Repositorio para persistencia de la tabla group_
 */
@Repository
public interface GroupRepository extends JpaRepository<Group,Long> {

    @Query(value = "SELECT g FROM Group g WHERE LOWER(g.groupName) LIKE :gname")
    Page<Group> findAllGroupByNameLike(@Param("gname")String groupName, Pageable pageable) throws Exception;

    @Query(value = "SELECT g FROM Group g WHERE g.active=:act AND LOWER(g.groupName) LIKE :gname")
    Page<Group> findAllGroupActiveByNameLike(@Param("gname")String groupName,@Param("act")Boolean active, Pageable pageable) throws Exception;

    /*
    // GGR20200610 Obtener imagen del grupo principal
    @Query(value="select imagen from group_ where id_group in (select id_group from user_has_group where es_principal = true and id_user_app = :idUserApp)", nativeQuery = true)
    String findImageGroup(@Param("idUserApp") Long idUserApp) throws Exception;

    // GGR20200610 Obtener name del grupo principal
    @Query(value="select group_name from group_ where id_group in (select id_group from user_has_group where es_principal = true and id_user_app = :idUserApp)", nativeQuery = true)
    String findNameMainGroup(@Param("idUserApp") Long idUserApp) throws Exception;

    // GGR20200610 Obtener id del grupo principal
    @Query(value="select id_group from group_ where id_group in (select id_group from user_has_group where es_principal = true and id_user_app = :idUserApp)", nativeQuery = true)
    Long findIdMainGroup(@Param("idUserApp") Long idUserApp) throws Exception;
    */
}
