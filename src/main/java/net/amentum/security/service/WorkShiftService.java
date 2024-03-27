package net.amentum.security.service;

import net.amentum.security.exception.WorkShiftException;
import net.amentum.security.views.WorkShiftView;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author marellano.
 */
public interface WorkShiftService {

    /**
     * Método para agregar un nuevo turno laboral a la base de datos, la vista se convierte a la entidad antes de guardarla
     * @param workShiftView vista a ser guardada en la base de datos.
     * @throws WorkShiftException
     * <br/>.1.- Si ocurre algún error  guardar en base de datos
     * **/
    void addNewWorkShift(WorkShiftView workShiftView) throws WorkShiftException;

    /**
     * Método para modificar un turno laboral en la base de datos, la vista se convierte a la entidad antes de modificarla
     * @param workShiftView vista a ser modificada en la base de datos.
     * @throws WorkShiftException
     * <br/>.1.- Si ocurre algún error al modificar en base de datos
     * **/
    void editWorkShift(WorkShiftView workShiftView) throws WorkShiftException;

    /**
     * Método para eliminar el turno laboral en la base de datos
     * @param idWorkShift id del turno laboral para eliminar en la base de datos
     * @throws WorkShiftException
     * <br/>.1.- Si ocurre algún error al eliminar en base de datos
     * **/
    void deleteWorkShift(Long idWorkShift) throws WorkShiftException;

    /**
     * Método para obtener el turno laboral de la base de datos
     * @param idWorkShift id del turno laboral
     * @throws WorkShiftException
     * <br/>.1.- Si ocurre algún error al obtener datos  en base de datos
     * **/
    WorkShiftView getWorkShift(Long idWorkShift) throws WorkShiftException;

    /**
     * Método para obtener todos los turnos laborales de la base de datos
     * @throws WorkShiftException
     * <br/>.1.- Si ocurre algún error al obtener datos en base de datos
     * **/
    List<WorkShiftView> getAllWorkShift() throws WorkShiftException;

    /**
     * Método para obtener todos los turnos laborales de la base de datos paginable
     * @throws WorkShiftException
     * <br/>.1.- Si ocurre algún error al obtener datos en base de datos paginable
     * **/
    Page<WorkShiftView> getWorkShiftPage(Boolean active, String name,Integer page,Integer size,String orderColumn,String orderType)  throws WorkShiftException;
}
