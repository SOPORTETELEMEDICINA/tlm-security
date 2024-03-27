package net.amentum.security.rest;

import net.amentum.common.BaseController;
import net.amentum.security.exception.ExceptionServiceCode;
import net.amentum.security.exception.OauthClientDetailsException;
import net.amentum.security.model.OauthClientDetails;
import net.amentum.security.service.OauthClientDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author dev06
 */
@RequestMapping("oauth/details")
@RestController
public class OauthClientRest extends BaseController{

    private final Logger logger = LoggerFactory.getLogger(OauthClientRest.class);

    private OauthClientDetailService clientDetailService;

    @Autowired
    public void setClientDetailService(OauthClientDetailService clientDetailService) {
        this.clientDetailService = clientDetailService;
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addClientDetail(@RequestBody @Validated OauthClientDetails oauthClientDetails) throws OauthClientDetailsException{
        logger.info(ExceptionServiceCode.OAUTH+" - Agregar nueva autorización: {}",oauthClientDetails);
        clientDetailService.addOauthClientDetail(oauthClientDetails);
    }


    @RequestMapping(value = "{id}",method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateClientDetail(@PathVariable()Long id,@RequestBody @Validated OauthClientDetails oauthClientDetails) throws OauthClientDetailsException{
        logger.info(ExceptionServiceCode.OAUTH+" - Editar autorización: {}",oauthClientDetails);
        oauthClientDetails.setId(id);
        clientDetailService.editOauthClientDetail(oauthClientDetails);
    }



    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public OauthClientDetails getClientDetail(@PathVariable()Long id) throws OauthClientDetailsException{
        logger.info(ExceptionServiceCode.OAUTH+" - Obtener autorización: {}",id);
        return clientDetailService.finOauthClientDetails(id);
    }


    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<OauthClientDetails> getPage(@RequestParam(required = false)String name, @RequestParam(required = false)Integer page, @RequestParam(required=false)Integer size,@RequestParam(required = false)
            String orderColumn,@RequestParam(required = false)String orderType) throws OauthClientDetailsException{
        logger.info(ExceptionServiceCode.GROUP+"- Obtener listado de autorizaciones: {} - page {} - size: {} - orderColumn: {} - orderType: {}",name,page,size,orderColumn,orderType);
        if(page==null)
            page = 0;
        if(size==null)
            size = 10;
        return clientDetailService.findGroups(name != null ? name : "",page,size,orderColumn,orderType);
    }


    @RequestMapping(value = "{id}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteClientDetail(@PathVariable()Long id) throws OauthClientDetailsException{
        logger.info(ExceptionServiceCode.OAUTH+" - Eliminar autorización: {}",id);
        clientDetailService.deleteClientDetail(id);
    }
}
