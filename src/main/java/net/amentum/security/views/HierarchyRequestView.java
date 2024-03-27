package net.amentum.security.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author marellano
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HierarchyRequestView implements Serializable {

    @NotNull(message = "Es requerido el jefe")
    private Long idBoss;
    @NotEmpty(message = "Es requerido los perfiles asignados")
    private List<Long> idProfiles;
}
