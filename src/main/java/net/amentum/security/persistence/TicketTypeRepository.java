package net.amentum.security.persistence;

import net.amentum.security.model.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author  marellano
 */
@Repository
public interface TicketTypeRepository  extends JpaRepository<TicketType, Long>, JpaSpecificationExecutor {
}
