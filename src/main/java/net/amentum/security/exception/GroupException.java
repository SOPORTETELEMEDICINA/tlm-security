package net.amentum.security.exception;

import net.amentum.common.GenericException;

/**
 * Created by dev06 on 16/03/17.
 */
public class GroupException extends GenericException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8163130912989859399L;


	private final ExceptionServiceCode MODULE_CODE = ExceptionServiceCode.GROUP;


    private String layer;

    private String action;

    public GroupException(Exception ex,String message,String layer,String action){
        super(ex,message);
        this.layer = layer;
        this.action = action;
    }

    public GroupException(String message,String layer,String action){
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
