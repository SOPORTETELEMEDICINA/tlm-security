package net.amentum.security.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShiftHourView implements Serializable {

    private Long idHour;
    @Temporal(TemporalType.TIME)
    @NotNull(message = "La hora de inicio es requerida.")
    private Date startTime;
    @Temporal(TemporalType.TIME)
    @NotNull(message = "La hora final es requerida.")
    private Date endTime;
    @NotNull(message = "El dia requerido.")
    private Long day;



}
