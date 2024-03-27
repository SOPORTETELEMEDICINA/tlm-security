package net.amentum.security.persistence;

import net.amentum.security.model.RecoverPasswordRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository()
public interface RecoverPasswordRepository extends JpaRepository<RecoverPasswordRequest,Long>,JpaSpecificationExecutor{

    @Modifying()
    @Query("UPDATE RecoverPasswordRequest p SET p.active = :active WHERE p.username=:username")
    Integer updateRecoverPasswordRequestByUsername(@Param("active") Boolean active, @Param("username") String username);
}
