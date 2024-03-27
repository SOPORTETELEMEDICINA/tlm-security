package net.amentum.security.persistence;

import net.amentum.security.model.OauthClientDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author dev06
 */
@Repository
public interface OauthClientDetailsRepository extends JpaRepository<OauthClientDetails,Long>{

    @Query("SELECT o FROM OauthClientDetails o WHERE LOWER(o.clientId) LIKE :clientId OR LOWER(o.clientSecret) LIKE :secret")
    Page<OauthClientDetails> findByName(@Param(value = "clientId")String clientId,
                                        @Param(value = "secret")String secret, Pageable pageable) throws Exception;
}
