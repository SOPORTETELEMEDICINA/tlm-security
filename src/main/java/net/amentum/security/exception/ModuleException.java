package net.amentum.security.exception;

import net.amentum.common.GenericException;

/**
 * Created by dev06 on 11/04/17.
 */
public class ModuleException extends GenericException {
    private final ExceptionServiceCode MODULE_CODE = ExceptionServiceCode.MODULE;


    private String layer;

    private String action;

    public ModuleException(Exception ex,String message,String layer,String action){
        super(ex,message);
        this.layer = layer;
        this.action = action;
    }

    public ModuleException(String message,String layer,String action){
        super(message);
        this.layer = layer;
        this.action = action;
    }

    public String getLayer() {return layer;}

    public void setLayer(String layer) {this.layer = layer;}

    public String getAction() {return action;}

    public void setAction(String action) {this.action = action;}

    @Override
    public String getExceptionCode() {
        return new StringBuffer(layer).append(MODULE_CODE).append(action).toString();
    }
}
