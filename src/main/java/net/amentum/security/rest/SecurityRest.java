package net.amentum.security.rest;

import io.swagger.annotations.Api;
import net.amentum.common.BaseController;
import net.amentum.common.GenericException;
import net.amentum.security.service.PasswordRequestService;
import net.amentum.security.views.ChangePasswordRequestView;
import net.amentum.security.views.RecoverPasswordRequestView;
import net.amentum.security.views.UserAppPageView;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("users")
@Api(description = "Servicios de seguridad")
public class SecurityRest extends BaseController{


    private TokenStore tokenStore;

    private PasswordRequestService passwordRequestService;

    private final Logger logger = Logger.getLogger(SecurityRest.class);

    @Autowired
    public void setPasswordRequestService(PasswordRequestService passwordRequestService) {
        this.passwordRequestService = passwordRequestService;
    }

    @Autowired
    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @RequestMapping(method= RequestMethod.POST,value="secure/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletRequest request) throws GenericException{
        String authHeader = request.getHeader("Authorization");
        logger.info("Remove token from session: "+authHeader);
        if (authHeader != null) {
                String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            OAuth2RefreshToken refresToken = tokenStore.readRefreshToken(accessToken.getValue());
            if(refresToken!=null)
                tokenStore.removeRefreshToken(refresToken);
            if(accessToken!=null)
                tokenStore.removeAccessToken(accessToken);
        }
    }

    @RequestMapping(value="recoverPassword",method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public UserAppPageView recoverPassword(@RequestParam(required = true)String curp) throws GenericException{
        return passwordRequestService.sendPasswordRequestByCurp(curp);
    }

    @RequestMapping(value="hashPass",method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String hashPass(@RequestParam(required = true)String user,@RequestParam(required = true)String pass) throws GenericException{
        return passwordRequestService.hashPass(user,pass);
    }


    @RequestMapping(value="changePassword",method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@Validated @RequestBody ChangePasswordRequestView requestView) throws GenericException{
        passwordRequestService.changePasswordRequest(requestView);
    }

    @RequestMapping(value="recoverPassword",method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void validatePassword(@Validated @RequestBody RecoverPasswordRequestView requestView) throws GenericException{
        passwordRequestService.validatePasswordRequest(requestView);
    }
}
