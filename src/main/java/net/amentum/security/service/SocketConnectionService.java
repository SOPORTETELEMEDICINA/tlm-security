package net.amentum.security.service;

import net.amentum.security.exception.SocketSessionException;
import net.amentum.security.views.SocketSessionView;

public interface SocketConnectionService {
    void addSocketSession(SocketSessionView socketSessionView) throws SocketSessionException;
    void recoverMessages(SocketSessionView socketSessionView) throws SocketSessionException;
    void removeSocketSession(String sessionId) throws SocketSessionException;
    Boolean isSessionActive(String username) throws SocketSessionException;
}
