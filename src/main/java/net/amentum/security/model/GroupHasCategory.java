package net.amentum.security.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="group_has_category")
@AssociationOverrides({
        @AssociationOverride(name = "pk.ticketCategory",joinColumns =  @JoinColumn(name="id_ticket_category")),
        @AssociationOverride(name= "pk.group",joinColumns = @JoinColumn(name = "id_group"))
})
public class GroupHasCategory implements Serializable {

    private static final long serialVersionUID = 5718944504686168607L;

    @EmbeddedId
    private GroupHasCategoryId pk = new GroupHasCategoryId();

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();

    public GroupHasCategoryId getPk() {
        return pk;
    }

    public void setPk(GroupHasCategoryId pk) {
        this.pk = pk;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Transient
    public TicketCategory getTicketCategory(){
        return getPk().getTicketCategory();
    }

    public void setTicketCategory(TicketCategory ticketCategory){
        getPk().setTicketCategory(ticketCategory);
    }

    @Transient
    public Group getGroup(){
        return getPk().getGroup();
    }

    public void setGroup(Group group){
        getPk().setGroup(group);
    }

}
