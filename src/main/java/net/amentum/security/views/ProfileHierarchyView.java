package net.amentum.security.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author marellano
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileHierarchyView implements Serializable {

    private Long idPerfil;
    private String namePerfil;
    private Boolean active;
}
