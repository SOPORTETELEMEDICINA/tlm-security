package net.amentum.security.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author marellano
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TicketCategoryView implements Serializable {

    @NotNull(message = "Es requerido el id del ticket category.")
    private Long idTicketCategory;
    @NotEmpty(message = "Es requerido el nombre del ticket category.")
    private String categoryName;
}
