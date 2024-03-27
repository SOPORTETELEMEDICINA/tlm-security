package net.amentum.security.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="group_has_type_ticket")
@AssociationOverrides({
        @AssociationOverride(name = "pk.ticketType",joinColumns =  @JoinColumn(name="id_ticket_type")),
        @AssociationOverride(name= "pk.group",joinColumns = @JoinColumn(name = "id_group"))
})
public class GroupHasTypeTicket implements Serializable {

    private static final long serialVersionUID = 5718944504686168607L;

    @EmbeddedId
    private GroupHasTypeTicketId pk = new GroupHasTypeTicketId();

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();

    public GroupHasTypeTicketId getPk() {
        return pk;
    }

    public void setPk(GroupHasTypeTicketId pk) {
        this.pk = pk;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Transient
    public Group getGroup() {
        return getPk().getGroup();
    }

    public void setGroup(Group group) {
        getPk().setGroup(group);
    }

    @Transient
    public TicketType getTicketType() {
        return getPk().getTicketType();
    }

    public void setTicketType(TicketType ticketType) {
        getPk().setTicketType(ticketType);
    }

}
