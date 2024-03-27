package net.amentum.security.service.impl;

import net.amentum.common.GenericException;
import net.amentum.common.TimeUtils;
import net.amentum.security.exception.ExceptionServiceCode;
import net.amentum.security.exception.ExtraFieldException;
import net.amentum.security.model.ExtraField;
import net.amentum.security.model.Profile;
import net.amentum.security.persistence.ExtraFieldRepository;
import net.amentum.security.persistence.ProfileRepository;
import net.amentum.security.service.ExtraFieldService;
import net.amentum.security.views.ExtraFieldView;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.*;

import static net.amentum.common.TimeUtils.LONG_DATE;
import static net.amentum.common.TimeUtils.parseDate;


@Service
@Transactional(readOnly = false)
public class ExtraFieldServiceImpl implements ExtraFieldService {

    private final Logger logger = LoggerFactory.getLogger(ExtraFieldServiceImpl.class);

    private ExtraFieldRepository fieldRepository;
    private ProfileRepository profileRepository;

    @Autowired
    public void setFieldRepository(ExtraFieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    @Autowired
    public void setProfileRepository(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    private final Map<String,Object> colOrderNames = new HashMap<>();
    {
        colOrderNames.put("extraFieldId","extraFieldId");
        colOrderNames.put("key","key");
        colOrderNames.put("legend","legend");
        colOrderNames.put("createdDate","createdDate");
        colOrderNames.put("fieldType","fieldType");
        colOrderNames.put("profileName","profile.profileName");
    }

    @Override
    @Transactional(readOnly = false,rollbackFor = ExtraFieldException.class)
    public void addNewField(ExtraFieldView view) throws ExtraFieldException {
        try{
            Specifications<ExtraField> countFields = Specifications.where((root, query, cb) -> {
                Join<ExtraField,Profile> joinProfile = root.join("profile");
                return cb.and(
                        cb.equal(root.get("key"),view.getKey()),
                        cb.equal(joinProfile.get("profileId"),view.getProfileId()),
                        cb.equal(root.get("active"),Boolean.TRUE)
                );
            });
            if(fieldRepository.count(countFields)>0){
                throw new ExtraFieldException("Ya existe un campo con el mismo identificador para el perfil", GenericException.LAYER_SERVICE,GenericException.ACTION_VALIDATE);
            }
            fieldRepository.save(fromView(view));
        }catch (ExtraFieldException fe){
            throw fe;
        }catch (DataAccessException dae){
            ExtraFieldException extraFieldException = new ExtraFieldException("Error al guardar campo extra",GenericException.LAYER_DAO,GenericException.ACTION_INSERT);
            logger.error(ExceptionServiceCode.FEX+"- Error al guardar campo extra  - {} - CODE: {} ",view,extraFieldException.getExceptionCode(),dae);
            throw extraFieldException;
        }catch (Exception ex){
            ExtraFieldException extraFieldException = new ExtraFieldException("Error al guardar campo extra",GenericException.LAYER_SERVICE,GenericException.ACTION_INSERT);
            logger.error(ExceptionServiceCode.FEX+"- Error al guardar campo extra  - {} - CODE: {} ",view,extraFieldException.getExceptionCode(),ex);
            throw extraFieldException;
        }
    }

    @Override
    @Transactional(readOnly = false,rollbackFor = ExtraFieldException.class)
    public void updateField(ExtraFieldView view) throws ExtraFieldException {
        try{
            if(!fieldRepository.exists(view.getExtraFieldId()))
                throw  new ExtraFieldException("No se encuentra campo a editar",GenericException.LAYER_SERVICE,GenericException.ACTION_VALIDATE);
            fieldRepository.save(fromView(view));
        }catch (ExtraFieldException e){
            throw e;
        }catch (DataAccessException dae){
            ExtraFieldException extraFieldException = new ExtraFieldException("Error al editar campo extra",GenericException.LAYER_DAO,GenericException.ACTION_UPDATE);
            logger.error(ExceptionServiceCode.FEX+"- Error al editar campo extra  - {} - CODE: {} ",view,extraFieldException.getExceptionCode(),dae);
            throw extraFieldException;
        }catch (Exception ex){
            ExtraFieldException extraFieldException = new ExtraFieldException("Error al editar campo extra",GenericException.LAYER_DAO,GenericException.ACTION_UPDATE);
            logger.error(ExceptionServiceCode.FEX+"- Error al editar campo extra  - {} - CODE: {} ",view,extraFieldException.getExceptionCode(),ex);
            throw extraFieldException;
        }
    }

    @Override
    @Transactional(readOnly = false,rollbackFor = ExtraFieldException.class)
    public void deleteField(Long fieldId) throws ExtraFieldException {
        try{
            if(!fieldRepository.exists(fieldId))
                throw  new ExtraFieldException("No se encuentra campo a eliminar",GenericException.LAYER_SERVICE,GenericException.ACTION_VALIDATE);
            fieldRepository.delete(fieldId);
        }catch (ExtraFieldException e){
            throw e;
        }catch (DataAccessException dae){
            ExtraFieldException extraFieldException = new ExtraFieldException("Error al eliminar campo extra",GenericException.LAYER_DAO,GenericException.ACTION_DELETE);
            logger.error(ExceptionServiceCode.FEX+"- Error al eliminar campo extra  - {} - CODE: {} ",fieldId,extraFieldException.getExceptionCode(),dae);
            throw extraFieldException;
        }catch (ConstraintViolationException e){
            ExtraFieldException extraFieldException = new ExtraFieldException("No es posible eliminar el campo extra",GenericException.LAYER_DAO,GenericException.ACTION_DELETE);
            logger.error(ExceptionServiceCode.FEX+"- Error al eliminar campo extra  - {} - CODE: {} ",fieldId,extraFieldException.getExceptionCode(),e);
            throw extraFieldException;
        }catch (Exception ex){
            ExtraFieldException extraFieldException = new ExtraFieldException("Error al eliminar campo extra",GenericException.LAYER_DAO,GenericException.ACTION_DELETE);
            logger.error(ExceptionServiceCode.FEX+"- Error al eliminar campo extra  - {} - CODE: {} ",fieldId,extraFieldException.getExceptionCode(),ex);
            throw extraFieldException;
        }
    }

    @Override
    @Transactional(readOnly = false,rollbackFor = ExtraFieldException.class)
    public ExtraFieldView findField(Long fieldId) throws ExtraFieldException {
        try{
            if(!fieldRepository.exists(fieldId))
                throw  new ExtraFieldException("No se encuentra campo a seleccionar",GenericException.LAYER_SERVICE,GenericException.ACTION_VALIDATE);
            return fromEntity(fieldRepository.findOne(fieldId));
        }catch (ExtraFieldException e){
            throw e;
        }catch (DataAccessException dae){
            ExtraFieldException extraFieldException = new ExtraFieldException("Error al seleccionar campo extra",GenericException.LAYER_DAO,GenericException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.FEX+"- Error al seleccionar campo extra  - {} - CODE: {} ",fieldId,extraFieldException.getExceptionCode(),dae);
            throw extraFieldException;
        }catch (Exception ex){
            ExtraFieldException extraFieldException = new ExtraFieldException("Error al seleccionar campo extra",GenericException.LAYER_DAO,GenericException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.FEX+"- Error al seleccionar campo extra  - {} - CODE: {} ",fieldId,extraFieldException.getExceptionCode(),ex);
            throw extraFieldException;
        }
    }

    @Override
    public List<ExtraFieldView> findFields(Long profileId, Boolean active) throws ExtraFieldException {
        try{
            List<ExtraField> fields = new ArrayList<>();
            List<ExtraFieldView> views = new ArrayList<>();
            if(profileId==null && active==null){
                fields = fieldRepository.findAll();
            }else{
                Specification<ExtraField> restrictions = Specifications.where(
                        (root, query, cb) -> {
                            Join<ExtraField,Profile> joinProfile = root.join("profile");
                            Predicate p = null;
                            if(profileId!=null){
                                p = cb.equal(joinProfile.get("profileId"),profileId);
                            }

                            if(active!=null){
                                if(p==null)
                                    p = cb.equal(root.get("active"),active);
                                else
                                    p = cb.and(p,cb.equal(root.get("active"),active));
                            }
                            return p;
                        }
                );
                fields = fieldRepository.findAll(restrictions);
            }
            fields.forEach(e->views.add(fromEntity(e)));
            return views;
        }catch (DataAccessException dae){
            ExtraFieldException extraFieldException = new ExtraFieldException("Error al seleccionar campos extra",GenericException.LAYER_DAO,GenericException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.FEX+"- Error al seleccionar campos extra  - {} - {} - CODE: {} ",profileId,active,extraFieldException.getExceptionCode(),dae);
            throw extraFieldException;
        }catch (Exception ex){
            ExtraFieldException extraFieldException = new ExtraFieldException("Error al seleccionar campos extra",GenericException.LAYER_DAO,GenericException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.FEX+"- Error al seleccionar campos extra  - {} - {} - CODE: {} ",profileId,active,extraFieldException.getExceptionCode(),ex);
            throw extraFieldException;
        }
    }

    @Override
    public Page<ExtraFieldView> findPage(String search, Long profileId, String legend, String date1, String date2, Boolean general, String columnOrder, String order, Integer page, Integer size) throws ExtraFieldException {
        try{
            Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("extraFieldId"));
            if(columnOrder!=null && order!=null){
                if(order.equalsIgnoreCase("asc")){
                    sort = new Sort(Sort.Direction.ASC,(String)colOrderNames.get(columnOrder));
                } else {
                    sort =  new Sort(Sort.Direction.DESC, (String)colOrderNames.get(columnOrder));
                }
            }
            PageRequest request = new PageRequest(page, size, sort);
            Specifications<ExtraField> fieldSpec = null;
            if(general){
                final String pattern = "%"+search.toLowerCase()+"%";
                fieldSpec = Specifications.where(
                        (root, query, cb) -> {
                            Join<ExtraField,Profile> joinProfile = root.join("profile");
                            return cb.or(
                                    cb.like(cb.lower(root.get("key")),pattern),
                                    cb.like(cb.lower(root.get("legend")),pattern),
                                    cb.like(cb.lower(joinProfile.get("profileName")),pattern)
                            );
                        }
                );
            }else{
                if(profileId!=null || legend!=null || date1!=null || date2!=null){
                    fieldSpec = Specifications.where((root, query, cb) -> {
                        Join<ExtraField,Profile> joinProfile = root.join("profile");
                        Predicate p = null;
                        if(profileId!=null)
                            p = cb.equal(joinProfile.get("profileId"),profileId);
                        if(legend!=null)
                            p = p!=null ? cb.and(p,cb.like(root.get("legend"),"%"+legend.toLowerCase()+"%")) : cb.like(root.get("legend"),"%"+legend.toLowerCase()+"%");
                        if(date1!=null && !date1.isEmpty()){
                            try{
                                Date startDate = parseDate(date1 + " 00:00:00", LONG_DATE);
                                p = p!=null ? cb.and(p,cb.greaterThanOrEqualTo(root.get("createdDate"),startDate)) : cb.greaterThanOrEqualTo(root.get("createdDate"),startDate);
                            }catch (Exception ex){
                                logger.warn("Error al parsear fecha: {}",date1);
                            }
                        }

                        if(date2!=null && !date2.isEmpty()){
                            try{
                                Date endDateDate = parseDate(date1 + " 23:26:59", LONG_DATE);
                                p = p!=null ? cb.and(p,cb.lessThanOrEqualTo(root.get("createdDate"),endDateDate)) : cb.greaterThanOrEqualTo(root.get("createdDate"),endDateDate);
                            }catch (Exception ex){
                                logger.warn("Error al parsear fecha: {}",date1);
                            }
                        }

                        return p;
                    });
                }
            }

            Page<ExtraField> fieldPage = null;

            if(fieldSpec==null)
                fieldPage = fieldRepository.findAll(request);
            else
                fieldPage = fieldRepository.findAll(fieldSpec,request);

            List<ExtraFieldView> views = new ArrayList<>();
            fieldPage.getContent().forEach(tf->{
                views.add(fromEntity(tf));
            });
            return new PageImpl<ExtraFieldView>(views,request,fieldPage.getTotalElements());
        }catch (DataAccessException dae){
            return null;
        }catch (Exception ex){
            return null;
        }
    }

    private ExtraField fromView(ExtraFieldView view){
        ExtraField field = new ExtraField();
        field.setActive(view.getActive());
        if(view.getCreatedDate()!=null)
            field.setCreatedDate(view.getCreatedDate());
        field.setExtraFieldId(view.getExtraFieldId());
        field.setFieldType(view.getFieldType());
        field.setFieldValidation(view.getFieldValidation());
        field.setKey(view.getKey());
        field.setLegend(view.getLegend());
        field.setProfile(profileRepository.findOne(view.getProfileId()));
        field.setValidationMessage(view.getValidationMessage());
        return field;
    }


    private ExtraFieldView fromEntity(ExtraField entity){
        ExtraFieldView view = new ExtraFieldView();
        view.setActive(entity.getActive());
        view.setCreatedDate(entity.getCreatedDate());
        view.setExtraFieldId(entity.getExtraFieldId());
        view.setFieldType(entity.getFieldType());
        view.setFieldValidation(entity.getFieldValidation());
        view.setKey(entity.getKey());
        view.setLegend(entity.getLegend());
        view.setProfileId(entity.getProfile().getProfileId());
        view.setProfileName(entity.getProfile().getProfileName());
        view.setValidationMessage(entity.getValidationMessage());
        return view;
    }
}
