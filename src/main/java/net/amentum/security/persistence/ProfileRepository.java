package net.amentum.security.persistence;

import net.amentum.security.model.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by dev06 on 18/04/17.
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile,Long>{

    @Query("SELECT c FROM Profile c WHERE LOWER(c.profileName) LIKE :name")
    Page<Profile> findByName(@Param("name")String name, Pageable pageable) throws Exception;


    @Query("SELECT c FROM Profile c order by c.profileId")
    List<Profile> findAllOrOrderByProfileId() throws Exception;
}
