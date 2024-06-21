package net.amentum.security.persistence;

import net.amentum.security.model.UserSignature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.validation.constraints.NotNull;

public interface UserSignatureRepository extends JpaRepository<UserSignature,Long>, JpaSpecificationExecutor {
    UserSignature existsUserSignatureByUserAppId(@NotNull Long userAppId);
}
