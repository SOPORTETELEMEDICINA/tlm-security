package net.amentum.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Victor de la Cruz
 * Class to map group_ table from database
 */
@Entity
@Table(name="group_")
public class Group implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -2275840290241689025L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_group")
    private Long groupId;

    @NotEmpty(message = "Ingrese el nombre del grupo")
    @Column(unique = true)
    private String groupName;

    @NotNull(message = "El campo no debe ser nulo")
    private Boolean active = Boolean.TRUE;

    // GGR20200610 imagen del grupo campo nuevo
    private String imagen;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate= new Date();

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "pk.group")
    @JsonIgnore
    private List<UserHasGroup> groupList=new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "pk.group", cascade = {CascadeType.ALL})
    @JsonIgnore
    private List<GroupHasCategory> categoryList=new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "pk.group", cascade = {CascadeType.ALL})
    @JsonIgnore
    private List<GroupHasTypeTicket> typeTicketList=new ArrayList<>();


    /**
     * Constructor using parameters
     * @param groupName the name for group
     * @param active attribute to know if this group is active or not
     * @param imagen // GGR20200610 imagen del grupo campo nuevo
     * @param createdDate attribute to know the creation date of this group  */
    public Group(String groupName, Boolean active, String imagen, Date createdDate) {
        this.groupName = groupName;
        this.active = active;
        this.imagen = imagen;
        this.createdDate = createdDate;
    }

    /**
     * Default constructor
     * */
    public Group() {}

    public List<UserHasGroup> getGroupList() {return groupList;}

    public void setGroupList(List<UserHasGroup> groupList) {this.groupList = groupList;}

    public Long getGroupId() {return groupId;}

    public void setGroupId(Long groupId) {this.groupId = groupId;}

    public String getGroupName() {return groupName;}

    public void setGroupName(String groupName) {this.groupName = groupName;}

    public Boolean getActive() {return active;}

    public void setActive(Boolean active) {this.active = active;}

    public String getImagen() {return imagen;}

    public void setImagen(String imagen) {this.imagen = imagen;}

    public Date getCreatedDate() {return createdDate;}

    public void setCreatedDate(Date createdDate) {this.createdDate = createdDate;}


    public List<GroupHasCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<GroupHasCategory> categoryList) {
        this.categoryList = categoryList;
    }

    public List<GroupHasTypeTicket> getTypeTicketList() {
        return typeTicketList;
    }

    public void setTypeTicketList(List<GroupHasTypeTicket> typeTicketList) {
        this.typeTicketList = typeTicketList;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", active=" + active +
                ", imagen=" + imagen +
                ", createdDate=" + createdDate +
                ", groupList=" + groupList +
                ", categoryList=" + categoryList +
                ", typeTicketList=" + typeTicketList +
                '}';
    }
}
