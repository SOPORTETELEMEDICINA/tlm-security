package net.amentum.security.persistence;

import net.amentum.security.model.TicketCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author  marellano
 */
@Repository
public interface TicketCategoryRepository  extends JpaRepository<TicketCategory, Long>, JpaSpecificationExecutor {
}
