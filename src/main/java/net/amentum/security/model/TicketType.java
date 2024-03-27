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
@Entity
@Table(name = "type_ticket")
public class TicketType implements Serializable {

    @Id
    private Long idTicketType;
    private String typeTicket;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "typeTicket", cascade = CascadeType.DETACH)
    private List<TicketCategory> ticketCategories = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "pk.ticketType",cascade = {CascadeType.ALL})
    private List<GroupHasTypeTicket> typeTicketList= new ArrayList<>();


    @Override
    public String toString() {
        return "TicketType{" +
                "idTicketType=" + idTicketType +
                ", typeTicket='" + typeTicket + '\'' +
               // ", groups=" + groups +
                '}';
    }
}
