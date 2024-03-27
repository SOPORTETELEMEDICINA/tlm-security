package net.amentum.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.rmi.MarshalException;
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
@Entity
@Table(name = "work_shift")
public class WorkShift implements Serializable  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idWorkShift;
    @NotNull(message = "Es requerido el nombre.")
    private String name;
    @NotNull(message = "Es requerido el estatus.")
    private Boolean status;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "workShift", cascade = CascadeType.ALL)
    private List<ShiftHour> shiftHours = new ArrayList<>();

}
