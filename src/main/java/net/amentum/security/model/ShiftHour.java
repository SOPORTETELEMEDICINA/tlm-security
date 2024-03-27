package net.amentum.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shift_hour")
public class ShiftHour implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idShiftHour;
    @Temporal(TemporalType.TIME)
    @NotNull(message = "Es requerido la hora inicio.")
    private Date startTime;
    @Temporal(TemporalType.TIME)
    @NotNull(message = "Es requerido la hora final.")
    private Date endTime;
    @NotNull(message = "Es requerido el dia.")
    private Long day;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_work_shift")
    private WorkShift workShift;

    @Override
    public String toString() {
        return "ShiftHour{" +
                "idShiftHour=" + idShiftHour +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", day=" + day +
               // ", workShift=" + workShift +
                '}';
    }
}
