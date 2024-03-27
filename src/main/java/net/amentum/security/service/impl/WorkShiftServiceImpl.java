package net.amentum.security.service.impl;

import net.amentum.security.Utils;
import net.amentum.security.converter.WorkShiftConverter;
import net.amentum.security.exception.ExceptionServiceCode;
import net.amentum.security.exception.WorkShiftException;
import net.amentum.security.model.ShiftHour;
import net.amentum.security.model.UserApp;
import net.amentum.security.model.WorkShift;
import net.amentum.security.persistence.ShiftHourRepository;
import net.amentum.security.persistence.UserAppRepository;
import net.amentum.security.persistence.WorkShiftRepository;
import net.amentum.security.service.WorkShiftService;
import net.amentum.security.views.ShiftHourView;
import net.amentum.security.views.WorkShiftView;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class WorkShiftServiceImpl implements WorkShiftService {

    private final Logger logger = LoggerFactory.getLogger(WorkShiftServiceImpl.class);

    private WorkShiftRepository workShiftRepository;

    private WorkShiftConverter workShiftConverter;

    private ShiftHourRepository shiftHourRepository;

    private UserAppRepository userAppRepository;

    @Autowired
    public void setUserAppRepository(UserAppRepository userAppRepository) {
        this.userAppRepository = userAppRepository;
    }

    @Autowired
    public void setShiftHourRepository(ShiftHourRepository shiftHourRepository) {
        this.shiftHourRepository = shiftHourRepository;
    }

    @Autowired
    public void setWorkShiftConverter(WorkShiftConverter workShiftConverter) {
        this.workShiftConverter = workShiftConverter;
    }

    @Autowired
    public void setWorkShiftRepository(WorkShiftRepository workShiftRepository) {
        this.workShiftRepository = workShiftRepository;
    }

    private final Map<String,Object> colOrderNames = new HashMap<>();
    {
        colOrderNames.put("idWorkShift","idWorkShift");
        colOrderNames.put("name","name");
        colOrderNames.put("status","status");
    }

    @Transactional(readOnly = false, rollbackFor = {WorkShiftException.class})
    public void addNewWorkShift(WorkShiftView workShiftView) throws WorkShiftException {
        try{
            WorkShift workShift = workShiftConverter.workShiftConverterToEntity(workShiftView, null);
            logger.info("Guardar en base de datos: "+workShift);
            workShiftRepository.save(workShift);
        } catch (Exception ex) {
            WorkShiftException wse = new WorkShiftException("No fue posible insertar el turno laboral", WorkShiftException.LAYER_DAO, WorkShiftException.ACTION_INSERT);
            wse.addError("Ocurrio un error al insertar el turno laboral: {}");
            logger.error("Error al insertar nuevo turno laboral - CODE: {} - {}",wse.getExceptionCode(),workShiftView,ex);
            throw ex;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {WorkShiftException.class})
    public void editWorkShift(WorkShiftView workShiftView) throws WorkShiftException {
        try{
            Specification<WorkShift> workShiftSpec = Specifications.where(
                    (root, query, cb) -> {
                        return cb.and(
                                cb.notEqual(root.get("idWorkShift"),workShiftView.getIdWorkShift()),
                                cb.equal(root.get("name"),workShiftView.getName()));
                    }
            );
            Long countWork= workShiftRepository.count(Specifications.where(workShiftSpec));

            if(countWork > 0){
                WorkShiftException tfe = new WorkShiftException(new Exception("Existe otro turno con el nombre: "+ workShiftView.getName()),"Existe otro turno con el nombre: "+ workShiftView.getName(),WorkShiftException.LAYER_DAO,WorkShiftException.ACTION_VALIDATE);
                tfe.addError("Existe otro turno con el nombre: "+ workShiftView.getName());
                throw tfe;
            }
            WorkShift workShift = workShiftRepository.findOne(workShiftView.getIdWorkShift());
            if(workShift == null){
                WorkShiftException tfe = new WorkShiftException(new Exception("Campo no encontrado"),"No se encuentra el id en la base de datos",WorkShiftException.LAYER_DAO,WorkShiftException.ACTION_VALIDATE);
                tfe.addError("No se encontro el campo: "+workShiftView.getIdWorkShift()+" en la base de datos");
                throw tfe;
            }
            workShift.getShiftHours().stream().forEach(h->{
                h.setWorkShift(null);
                shiftHourRepository.delete(h);
            });
            workShift.setShiftHours(new ArrayList<>());
            WorkShift workShift2 = workShiftConverter.workShiftConverterToEntity(workShiftView, workShift);

            workShiftRepository.save(workShift2);
        } catch (Exception ex) {
            WorkShiftException wse = new WorkShiftException("No fue posible modificar el turno laboral", WorkShiftException.LAYER_DAO, WorkShiftException.ACTION_UPDATE);
            wse.addError("Ocurrio un error al insertar el turno laboral: {}");
            logger.error("Error al insertar nuevo turno laboral - CODE: {} - {}",wse.getExceptionCode(),workShiftView,ex);
            throw ex;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {WorkShiftException.class})
    public void deleteWorkShift(Long idWorkShift) throws WorkShiftException{
        try{
            WorkShift workShift = workShiftRepository.findOne(idWorkShift);
            if(workShift == null){
                WorkShiftException tfe = new WorkShiftException(new Exception("Campo no encontrado"),"No se encuentra el id en la base de datos",WorkShiftException.LAYER_DAO,WorkShiftException.ACTION_VALIDATE);
                tfe.addError("No se encontro el campo: "+idWorkShift+" en la base de datos");
                throw tfe;
            }
            Specification<UserApp> userAppSpec = Specifications.where(
                    (root, query, cb) -> {
                      Join<UserApp,WorkShift> join = root.join("workShift");
                        return cb.and(
                                cb.equal(join.get("idWorkShift"),idWorkShift));
                    }
            );
            Long countUsersUseIdWork = userAppRepository.count(Specifications.where(userAppSpec));;
            logger.info("count: {}", countUsersUseIdWork);
            if(countUsersUseIdWork > 0){
                WorkShiftException tfe = new WorkShiftException(new Exception("El turno con id: "+idWorkShift+", se encuentra en uso"),"El turno con id: "+idWorkShift+", se encuentra en uso",WorkShiftException.LAYER_DAO,WorkShiftException.ACTION_VALIDATE);
                tfe.addError("El turno con id: "+idWorkShift+", se encuentra en uso");
                throw tfe;
            }
            workShiftRepository.delete(idWorkShift);
        } catch (WorkShiftException ex) {
            WorkShiftException wse = new WorkShiftException("No fue posible eliminar el turno laboral", WorkShiftException.LAYER_DAO, WorkShiftException.ACTION_DELETE);
            wse.addError("Ocurrio un error al eliminar el turno laboral: {}");
            logger.error("Error al eliminar el turno laboral - CODE: {} - {}",wse.getExceptionCode(),idWorkShift,ex);
            throw ex;
        } catch (Exception ex) {
            WorkShiftException wse = new WorkShiftException("No fue posible eliminar el turno laboral", WorkShiftException.LAYER_DAO, WorkShiftException.ACTION_DELETE);
            wse.addError("Ocurrio un error al eliminar el turno laboral: {}");
            logger.error("Error al eliminar el turno laboral - CODE: {} - {}",wse.getExceptionCode(),idWorkShift,ex);
            throw ex;
        }
    }

    @Override
    public WorkShiftView getWorkShift(Long idWorkShift) throws WorkShiftException {
        try{
            WorkShift workShift = workShiftRepository.findOne(idWorkShift);
            if(workShift == null){
                WorkShiftException tfe = new WorkShiftException(new Exception("Campo no encontrado"),"No se encuentra el id en la base de datos",WorkShiftException.LAYER_DAO,WorkShiftException.ACTION_VALIDATE);
                tfe.addError("No se encontro el campo: "+idWorkShift+" en la base de datos");
                throw tfe;
            }
            return workShiftConverter.workShiftConverterToView(workShift, Boolean.TRUE);
        } catch (WorkShiftException ex) {
            WorkShiftException wse = new WorkShiftException("No fue posible eliminar el turno laboral", WorkShiftException.LAYER_DAO, WorkShiftException.ACTION_SELECT);
            wse.addError("Ocurrio un error al eliminar el turno laboral: {}");
            logger.error("Error al eliminar el turno laboral - CODE: {} - {}",wse.getExceptionCode(),idWorkShift,ex);
            throw ex;
        } catch (Exception ex) {
            WorkShiftException wse = new WorkShiftException("No fue posible eliminar el turno laboral", WorkShiftException.LAYER_DAO, WorkShiftException.ACTION_SELECT);
            wse.addError("Ocurrio un error al eliminar el turno laboral: {}");
            logger.error("Error al eliminar el turno laboral - CODE: {} - {}",wse.getExceptionCode(),idWorkShift,ex);
            throw ex;
        }
    }

    @Override
    public List<WorkShiftView> getAllWorkShift() throws WorkShiftException {
        try{
            List<WorkShiftView> viewList = new ArrayList<>();
            for (WorkShift ws: workShiftRepository.findAll() ) {
                viewList.add(workShiftConverter.workShiftConverterToView(ws, Boolean.TRUE));
            }
            return viewList;
        } catch (Exception ex) {
            WorkShiftException wse = new WorkShiftException("No fue posible obtener los turnos laborales", WorkShiftException.LAYER_DAO, WorkShiftException.ACTION_SELECT);
            wse.addError("Ocurrio un error al obtener los turnos laborales");
            logger.error("Error al  obtener los turnos laborales - CODE: {} - {}",wse.getExceptionCode(),ex);
            throw ex;
        }
    }

    @Override
    public Page<WorkShiftView> getWorkShiftPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType)  throws WorkShiftException{
        try{
            logger.info("- Obtener listado de turnos laborales paginable: {} - page {} - size: {} - orderColumn: {} - orderType: {} - active: {}",name,page,size,orderColumn,orderType,active);
            List<WorkShiftView> workShiftViewList = new ArrayList<>();
            Page<WorkShift> workShiftPage = null;
            Sort sort = new Sort(Sort.Direction.ASC,(String)colOrderNames.get("idWorkShift"));

            if(orderColumn!=null && orderType!=null){
                if(orderType.equalsIgnoreCase("asc")){
                    sort = new Sort(Sort.Direction.ASC,(String)colOrderNames.get(orderColumn));
                }else {
                    sort = new Sort(Sort.Direction.DESC,(String)colOrderNames.get(orderColumn));
                }

            }
            PageRequest request = new PageRequest(page,size,sort);
            final String patternSearch = "%"+name+"%";
            Specifications<WorkShift> spec = Specifications.where(
                    (root, query, cb) -> {
                        Predicate tc=null;
                        if(name != null && !name.isEmpty()){
                            tc = (tc != null ? cb.and(tc, cb.like(cb.lower(root.get("name")),patternSearch)) : cb.like(cb.lower(root.get("name")),patternSearch));
                        }
                        if(active != null){
                            tc = (tc != null ? cb.and(tc, cb.equal(root.get("status"), active)) : cb.equal(root.get("status"), active));
                        }
                        return tc;
                    }
            );

            if(spec == null){
                workShiftPage = workShiftRepository.findAll(request);
            } else {
                workShiftPage = workShiftRepository.findAll(spec,request);
            }

            workShiftPage.getContent().forEach(workShift -> {
                workShiftViewList.add(workShiftConverter.workShiftConverterToView(workShift, Boolean.FALSE));
            });
            PageImpl<WorkShiftView> shiftViewPage = new PageImpl<WorkShiftView>(workShiftViewList, request, workShiftPage.getTotalElements());
            return shiftViewPage;
        }catch (Exception ex){
            WorkShiftException workShiftException = new WorkShiftException("Ocurrio un error al seleccionar lista de turnos laborales paginable",WorkShiftException.LAYER_SERVICE,WorkShiftException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP+"- Error al tratar de seleccionar lista de turnos laborales paginable - CODE: {}",workShiftException.getExceptionCode(),ex);
            throw  workShiftException;
        }
    }
}
