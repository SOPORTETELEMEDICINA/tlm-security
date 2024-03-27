package net.amentum.security.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table
public class SocketMessage implements Serializable {
    @Id
    private Long socketMessageId;
    private String code;
    private String queue;
    @Lob
    private String message;

    public Long getSocketMessageId() {
        return socketMessageId;
    }

    public void setSocketMessageId(Long socketMessageId) {
        this.socketMessageId = socketMessageId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SocketMessage{" +
                "socketMessageId=" + socketMessageId +
                ", code='" + code + '\'' +
                ", queue='" + queue + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
