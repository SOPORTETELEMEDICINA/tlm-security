package net.amentum.security.views;



public class SocketSessionView {
    private String sessionId;
    private String username;
    private String deviceId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "SocketSessionView{" +
                "sessionId='" + sessionId + '\'' +
                ", username='" + username + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
