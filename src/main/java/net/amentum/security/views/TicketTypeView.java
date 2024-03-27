package net.amentum.security.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
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
public class TicketTypeView implements Serializable {

    @NotNull(message = "El id del tipo de ticket es requerido.")
    private Long idTicketType;
    @NotEmpty(message = "El nombre del tipo de ticket es requerido.")
    private String typeTicket;

    @Valid
    @NotEmpty(message = "Debe ingresar al menos un ticket category.")
    private List<TicketCategoryView> ticketCategoryView;
}
