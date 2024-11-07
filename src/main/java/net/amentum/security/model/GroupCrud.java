package net.amentum.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "group_crud")
public class GroupCrud implements Serializable {
    private static final long serialVersionUID = -2275840290241689025L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long idGroupCrud;

    @NotNull(message = "Ingrese el id del grupo")
    @Column(name = "id_group")
    Integer idGroup;

    @NotNull(message = "Ingrese la imagen")
    @Column(name = "image")
    String image;

    @NotNull(message = "Ingrese el color")
    @Column(name = "color")
    String color;
}
