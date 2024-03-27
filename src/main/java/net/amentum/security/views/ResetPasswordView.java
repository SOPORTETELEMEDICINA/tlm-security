package net.amentum.security.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordView implements Serializable {
    private Long idUserApp;
    @NotEmpty(message = "Debe agregar un password")
    private String password;
    @NotEmpty(message = "Debe agregar un hash")
    private String newPassword;
}
