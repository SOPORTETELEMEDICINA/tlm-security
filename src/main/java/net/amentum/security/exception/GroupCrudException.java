package net.amentum.security.exception;
import lombok.Getter;
import lombok.Setter;
import net.amentum.common.GenericException;

public class GroupCrudException extends GenericException {

    private static final long serialVersionUID = 8163130912989859399L;
    private final ExceptionServiceCode MODULE_CODE = ExceptionServiceCode.GROUP;

    @Setter
    @Getter
    private String layer;
    @Setter
    @Getter
    private String action;

    public GroupCrudException(Exception ex,String message,String layer,String action){
        super(ex,message);
        this.layer = layer;
        this.action = action;
    }

    public GroupCrudException(String message,String layer,String action){
        super(message);
        this.layer = layer;
        this.action = action;
    }

    @Override
    public String getExceptionCode() {
        return layer + MODULE_CODE + action;
    }
}