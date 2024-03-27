package net.amentum.security.persistence;

import net.amentum.security.model.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;

public interface UserImageRepository extends JpaRepository<UserImage,Long>,JpaSpecificationExecutor{

    UserImage findByUserApp_Username(String username);

    @Modifying
    @Query(value = "DELETE FROM user_image uag WHERE uag.user_app_id = :idUserApp", nativeQuery = true)
    Integer deleteByUserAppId(@NotNull @Param("idUserApp") Long idUserApp) throws Exception;
}
