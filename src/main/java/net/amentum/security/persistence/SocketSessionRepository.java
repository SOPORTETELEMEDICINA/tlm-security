package net.amentum.security.persistence;

import net.amentum.security.model.SocketSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SocketSessionRepository extends JpaRepository<SocketSession,String>, JpaSpecificationExecutor {
}
