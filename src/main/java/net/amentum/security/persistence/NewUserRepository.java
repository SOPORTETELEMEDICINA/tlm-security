package net.amentum.security.persistence;

import net.amentum.security.model.NewUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface NewUserRepository extends JpaRepository<NewUser, Long>, JpaSpecificationExecutor {

    NewUser findByUsername(String username);

    NewUser findByHash(String hash);

    NewUser findByUsernameAndIdGroup(String username, Long idGroup);
}
