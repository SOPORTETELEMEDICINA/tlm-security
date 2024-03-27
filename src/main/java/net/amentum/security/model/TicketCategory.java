package net.amentum.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table
public class TicketCategory implements Serializable {

    @Id
    private Long idTicketCategory;
    private String categoryName;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH})
    private TicketType typeTicket;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "pk.ticketCategory",cascade = {CascadeType.ALL})
    private List<GroupHasCategory> categoryList= new ArrayList<>();
}
