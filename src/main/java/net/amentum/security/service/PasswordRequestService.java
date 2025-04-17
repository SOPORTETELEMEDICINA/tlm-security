package net.amentum.security.service;


import net.amentum.security.exception.RecoverPasswordException;
import net.amentum.security.views.ChangePasswordRequestView;
import net.amentum.security.views.RecoverPasswordRequestView;
import net.amentum.security.views.UserAppPageView;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Victor
 * @version 1.0
 * Interface para recuperar contraseñas
 * */
public interface PasswordRequestService {

    /**
     *Method to generate new pasword request, this method validate a username and sends email to user with a <br>
     *recover link using a hash parameter with timeout
     *@param curp the email who wants to recover his password
     *@throws RecoverPasswordException if the request can't be completed, automatic roll-back
     * */
    UserAppPageView sendPasswordRequestByCurp(String curp) throws RecoverPasswordException;
    String hashPass(String user,String pass) throws RecoverPasswordException;


    /**
     *Method to validate a password request, change
     * */
    void validatePasswordRequest(RecoverPasswordRequestView requestView) throws  RecoverPasswordException;

    void changePasswordRequest(ChangePasswordRequestView requestView) throws  RecoverPasswordException;
}
