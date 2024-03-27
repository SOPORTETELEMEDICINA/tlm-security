package net.amentum.security.persistence;

import net.amentum.security.model.GroupHasTypeTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author  marellano
 */
@Repository
public interface GroupHasTypeTicketRepository extends JpaRepository<GroupHasTypeTicket, Long>, JpaSpecificationExecutor {
}
