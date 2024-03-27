package net.amentum.security.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author marellano
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WorkShiftView implements Serializable {

    private Long idWorkShift;
    @NotEmpty(message = "El nombre del turno es requerido.")
    private String name;
    @NotNull(message = "El estatus del turno es requerido.")
    private Boolean status;
    private Date createdDate;
    private Date modifiedDate;

    @Valid
    @NotEmpty(message = "Debe ingresar al menos un dia.")
    private List<ShiftHourView> shiftHourViews = new ArrayList<>();
}
