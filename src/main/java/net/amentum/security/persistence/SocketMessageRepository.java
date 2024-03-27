package net.amentum.security.persistence;

import net.amentum.security.model.SocketMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SocketMessageRepository extends JpaRepository<SocketMessage,Long>,JpaSpecificationExecutor{
}
