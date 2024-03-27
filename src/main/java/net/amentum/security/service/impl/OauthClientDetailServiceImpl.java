package net.amentum.security.service.impl;

import net.amentum.security.Utils;
import net.amentum.security.exception.ExceptionServiceCode;
import net.amentum.security.exception.OauthClientDetailsException;
import net.amentum.security.model.OauthClientDetails;
import net.amentum.security.persistence.OauthClientDetailsRepository;
import net.amentum.security.service.OauthClientDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dev06
 */
@Service
public class OauthClientDetailServiceImpl implements OauthClientDetailService{

    private final Logger logger = LoggerFactory.getLogger(OauthClientDetailServiceImpl.class);

    private OauthClientDetailsRepository oauthClientDetailsRepository;

    private final Map<String,Object> colOrderNames = new HashMap<>();
    {
        colOrderNames.put("id","id");
        colOrderNames.put("clientId","clientId");
        colOrderNames.put("resourceIds","resourceIds");
        colOrderNames.put("scope","scope");
        colOrderNames.put("authorizedGrantTypes","authorizedGrantTypes");
        colOrderNames.put("webServerRedirectUri","webServerRedirectUri");
        colOrderNames.put("authorities","authorities");
        colOrderNames.put("accessTokenValidity","accessTokenValidity");
        colOrderNames.put("refreshTokenValidity","refreshTokenValidity");
        colOrderNames.put("additionaInformation","additionaInformation");
        colOrderNames.put("autoaprove","autoaprove");
        colOrderNames.put("clientSecret","clientSecret");
    }

    @Autowired
    public void setOauthClientDetailsRepository(OauthClientDetailsRepository oauthClientDetailsRepository) {
        this.oauthClientDetailsRepository = oauthClientDetailsRepository;
    }

    @Override
    @Transactional(readOnly = false,rollbackFor = {OauthClientDetailsException.class})
    public void addOauthClientDetail(OauthClientDetails oauthClientDetails) throws OauthClientDetailsException {
        try{
            oauthClientDetailsRepository.save(oauthClientDetails);
        }catch (Exception ex){
            OauthClientDetailsException detailsException = new OauthClientDetailsException("Error al insertar en base de datos",OauthClientDetailsException.LAYER_DAO,OauthClientDetailsException.ACTION_INSERT);
            logger.error(ExceptionServiceCode.OAUTH+" - Error al insertar oaut_client_detail - CODE : {} - OAUTH: {}",detailsException.getExceptionCode(),oauthClientDetails,ex);
            throw  detailsException;
        }
    }

    @Override
    @Transactional(readOnly = false,rollbackFor = {OauthClientDetailsException.class})
    public void editOauthClientDetail(OauthClientDetails clientDetails) throws OauthClientDetailsException {
        try{
            if(!oauthClientDetailsRepository.exists(clientDetails.getId()))
                throw new OauthClientDetailsException("No se encuentra la autorización en base de datos",OauthClientDetailsException.LAYER_SERVICE,OauthClientDetailsException.ACTION_VALIDATE);
            oauthClientDetailsRepository.save(clientDetails);
        }catch (OauthClientDetailsException e){
            throw e;
        }catch (Exception ex){
            OauthClientDetailsException detailsException = new OauthClientDetailsException("Error al editar en base de datos",OauthClientDetailsException.LAYER_DAO,OauthClientDetailsException.ACTION_UPDATE);
            logger.error(ExceptionServiceCode.OAUTH+" - Error al editar oauth_client_detail - CODE : {} - OAUTH: {}",detailsException.getExceptionCode(),clientDetails,ex);
            throw  detailsException;
        }
    }

    @Override
    public OauthClientDetails finOauthClientDetails(Long id) throws OauthClientDetailsException {
        try{
            if(!oauthClientDetailsRepository.exists(id))
                throw new OauthClientDetailsException("No se encuentra la autorización en base de datos",OauthClientDetailsException.LAYER_SERVICE,OauthClientDetailsException.ACTION_VALIDATE);
            return oauthClientDetailsRepository.findOne(id);
        }catch (OauthClientDetailsException e){
            throw e;
        }catch (Exception ex){
            OauthClientDetailsException detailsException = new OauthClientDetailsException("Error al seleccionar detalles",OauthClientDetailsException.LAYER_DAO,OauthClientDetailsException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.OAUTH+" - Error al seleccionar oauth_client_detail - CODE : {} - ID: {}",detailsException.getExceptionCode(),id,ex);
            throw  detailsException;
        }
    }

    @Override
    public Page<OauthClientDetails> findGroups(String name, Integer page, Integer size, String columnOrder, String orderType) throws OauthClientDetailsException {
        try{
            Sort sort = new Sort(Sort.Direction.ASC,(String)colOrderNames.get("id"));

            if(columnOrder!=null && orderType!=null){
                if(orderType.equalsIgnoreCase("asc"))
                    sort = new Sort(Sort.Direction.ASC,(String)colOrderNames.get(columnOrder));
                else
                    sort = new Sort(Sort.Direction.DESC,(String)colOrderNames.get(columnOrder));
            }
            PageRequest request = new PageRequest(page,size,sort);
            return oauthClientDetailsRepository.findByName(Utils.getPatternLike(name),Utils.getPatternLike(name),request);
        }catch (Exception ex){
            OauthClientDetailsException detailsException = new OauthClientDetailsException("Error al seleccionar lista",OauthClientDetailsException.LAYER_DAO,OauthClientDetailsException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.OAUTH+" - Error al seleccionar lista oauth_client_detail - CODE : {} ",detailsException.getExceptionCode(),ex);
            throw  detailsException;
        }
    }

    @Override
    @Transactional(readOnly = false,rollbackFor = {OauthClientDetailsException.class})
    public void deleteClientDetail(Long id) throws OauthClientDetailsException {
        try{
            if(!oauthClientDetailsRepository.exists(id))
                throw new OauthClientDetailsException("La autorización que desea eliminar no se encuentra en base de datos",OauthClientDetailsException.LAYER_SERVICE,OauthClientDetailsException.ACTION_VALIDATE);
            oauthClientDetailsRepository.delete(id);
        }catch (OauthClientDetailsException o){
            throw  o;
        }catch (DataIntegrityViolationException dta){
            OauthClientDetailsException ex = new OauthClientDetailsException("No es posible eliminar la autorización de la base de datos",OauthClientDetailsException.LAYER_DAO,OauthClientDetailsException.ACTION_DELETE);
            logger.error("Error al eliminar oauth_client_detail - CODE: {} -ID: {}",ex.getExceptionCode(),id,dta);
            throw  ex;
        }catch (Exception ex){
            OauthClientDetailsException e = new OauthClientDetailsException("Ocurrio un error al eliminar la autorización de la base de datos",OauthClientDetailsException.LAYER_DAO,OauthClientDetailsException.ACTION_DELETE);
            logger.error("Error al eliminar oauth_client_detail - CODE: {} -ID: {}",e.getExceptionCode(),id,ex);
            throw  e;
        }
    }
}
