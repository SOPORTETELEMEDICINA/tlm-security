package net.amentum.security.exception;

import net.amentum.common.GenericException;

public class UserAlentaException extends GenericException {

    private final ExceptionServiceCode MODULE_CODE = ExceptionServiceCode.USERALENTA;
    private String layer;
    private String action;

    public UserAlentaException(Exception ex,String message,String layer,String action){
        super(ex,message);
        this.layer = layer;
        this.action = action;
    }

    public UserAlentaException(String message,String layer,String action){
        super(message);
        this.layer = layer;
        this.action = action;
    }

    @Override
    public String getExceptionCode() {
        return new StringBuffer(layer).append(MODULE_CODE).append(action).toString();
    }
}
