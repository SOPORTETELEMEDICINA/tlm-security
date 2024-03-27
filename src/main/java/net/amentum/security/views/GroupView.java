package net.amentum.security.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.amentum.security.model.GroupHasCategory;
import net.amentum.security.model.UserHasGroup;
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
public class GroupView implements Serializable {

    private Long groupId;
    @NotEmpty(message = "Ingrese el nombre del grupo")
    private String groupName;
    @NotNull(message = "El campo no debe ser nulo")
    private Boolean active = Boolean.TRUE;
    private Date createdDate= new Date();
    // Sre22052020 El ticket es opcional y no está en el UI de Grupos:
    //@Valid
    //@NotEmpty(message = "Debe ingresar al menos un tipo de ticket.")
    private List<TicketTypeView> ticketTypeView = new ArrayList<>();
    // GGR20200610 imagen del grupo campo nuevo
    private String imagen;


    public String toStringResume() {
        return "GroupView{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", imagen='" + imagen + '\'' +
                '}';
    }
}
