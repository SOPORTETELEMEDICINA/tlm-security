package net.amentum.security.rest;


import net.amentum.common.BaseController;
import net.amentum.security.exception.WorkShiftException;
import net.amentum.security.service.WorkShiftService;
import net.amentum.security.views.WorkShiftView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("work/shifts")
public class WorkShiftRest extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(WorkShiftRest.class);

    private WorkShiftService workShiftService;

    @Autowired
    public void setWorkShiftService(WorkShiftService workShiftService) {
        this.workShiftService = workShiftService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewWorkShift(@RequestBody @Valid WorkShiftView workShiftView) throws WorkShiftException {
        try{
            logger.info("Guardar nuevo turno laboral: {} ",workShiftView);
            workShiftService.addNewWorkShift(workShiftView);
        }catch (WorkShiftException workShift) {
            throw workShift;
        }catch (Exception ex) {
            WorkShiftException wse = new WorkShiftException("No fue posible insertar el turno de laboral", WorkShiftException.LAYER_CONTROLLER, WorkShiftException.ACTION_INSERT);
            logger.error("Error al insertar el turno de laboral - CODE: {} - ",wse.getExceptionCode(),ex);
            throw wse;
        }
    }

    @RequestMapping(value = "{idWorkShift}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void editWorkShift(@PathVariable()Long idWorkShift, @RequestBody @Valid WorkShiftView workShiftView) throws WorkShiftException {
        try{
            logger.info("Editar turno laboral - id: {} - info: {}",idWorkShift, workShiftView);
            workShiftView.setIdWorkShift(idWorkShift);
            workShiftService.editWorkShift(workShiftView);
        }catch (WorkShiftException workShift) {
            throw workShift;
        }catch (Exception ex) {
            WorkShiftException wse = new WorkShiftException("No fue posible modificar el turno laboral", WorkShiftException.LAYER_CONTROLLER, WorkShiftException.ACTION_UPDATE);
            logger.error("Error al insertar el turno de laboral - CODE: {} - ",wse.getExceptionCode(),ex);
            throw wse;
        }
    }

    @RequestMapping(value = "{idWorkShift}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteWorkShift(@PathVariable()Long idWorkShift) throws WorkShiftException {
        try{
            logger.info("Eliminar turno laboral - id: {}",idWorkShift);
            workShiftService.deleteWorkShift(idWorkShift);
        }catch (WorkShiftException workShift) {
            throw workShift;
        }catch (Exception ex) {
            WorkShiftException wse = new WorkShiftException("No fue posible eliminar el turno laboral", WorkShiftException.LAYER_CONTROLLER, WorkShiftException.ACTION_DELETE);
            logger.error("Error al eliminar el turno de laboral - CODE: {} - ",wse.getExceptionCode(),ex);
            throw wse;
        }
    }

    @RequestMapping(value = "{idWorkShift}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public WorkShiftView getWorkShiftById(@PathVariable()Long idWorkShift) throws WorkShiftException  {
        try{
            logger.info("Obtenr turno laboral - id: {}",idWorkShift);
            return workShiftService.getWorkShift(idWorkShift);
        }catch (WorkShiftException workShift) {
            throw workShift;
        }catch (Exception ex) {
            WorkShiftException wse = new WorkShiftException("No fue posible obtener el turno laboral", WorkShiftException.LAYER_CONTROLLER, WorkShiftException.ACTION_SELECT);
            logger.error("Error al obtener el turno de laboral - CODE: {} - ",wse.getExceptionCode(),ex);
            throw wse;
        }
    }

   // @PreAuthorize(value = "hasRole('ROLE_GET_ALL_WORK_SHIFT')")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<WorkShiftView> getWorkShift() throws WorkShiftException  {
        try{
            logger.info("Obtenr todos los turnos laborales");
            return workShiftService.getAllWorkShift();
        }catch (WorkShiftException workShift) {
            throw workShift;
        }catch (Exception ex) {
            WorkShiftException wse = new WorkShiftException("No fue posible obtener los turnos laborales", WorkShiftException.LAYER_CONTROLLER, WorkShiftException.ACTION_SELECT);
            logger.error("Error al obtener los turnos laborales - CODE: {} - ",wse.getExceptionCode(),ex);
            throw wse;
        }
    }


    @RequestMapping(value = "page", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<WorkShiftView> getWorkShiftPage(@RequestParam(required = false)Boolean active, @RequestParam(required = false)String name,
                                                @RequestParam(required = false)Integer page, @RequestParam(required=false)Integer size,
                                                @RequestParam(required = false) String orderColumn, @RequestParam(required = false)String orderType) throws WorkShiftException{
        logger.info("- Obtener listado de turnos laborales paginable: {} - page {} - size: {} - orderColumn: {} - orderType: {} - active: {}",name,page,size,orderColumn,orderType,active);
        if(page==null)
            page = 0;
        if(size==null)
            size = 10;
        if(orderType == null && orderType.isEmpty()){
            orderType = "asc";
        }
        return workShiftService.getWorkShiftPage(active,name != null ? name : "",page,size,orderColumn,orderType);
    }
}
