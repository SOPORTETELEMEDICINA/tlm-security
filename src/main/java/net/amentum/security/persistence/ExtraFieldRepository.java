package net.amentum.security.persistence;

import net.amentum.security.model.ExtraField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtraFieldRepository extends JpaRepository<ExtraField,Long>,JpaSpecificationExecutor{
}
