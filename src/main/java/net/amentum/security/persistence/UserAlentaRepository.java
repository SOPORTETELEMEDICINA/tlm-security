package net.amentum.security.persistence;

import net.amentum.security.model.UserAlenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Repository
public interface UserAlentaRepository extends JpaRepository<UserAlenta, Long>, JpaSpecificationExecutor {

    UserAlenta findByIdUser(@NotNull String idUser);

    UserAlenta findByIdAlenta(@NotNull String idAlenta);

}
