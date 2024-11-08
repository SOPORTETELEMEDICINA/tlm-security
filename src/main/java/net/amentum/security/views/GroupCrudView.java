package net.amentum.security.views;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupCrudView implements Serializable {
    Long idGroupCrud;
    Long idGroup;
    String image;
    String color;

    protected boolean canEqual(Object other) {
        return other instanceof GroupCrudView;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof GroupCrudView)) {
            return false;
        } else {
            GroupCrudView other = (GroupCrudView)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label59: {
                    Object this$idGroupCrud = this.getIdGroupCrud();
                    Object other$idGroupCrud = other.getIdGroupCrud();
                    if (this$idGroupCrud == null) {
                        if (other$idGroupCrud == null) {
                            break label59;
                        }
                    } else if (this$idGroupCrud.equals(other$idGroupCrud)) {
                        break label59;
                    }

                    return false;
                }

                Object this$idGroup = this.getIdGroup();
                Object other$idGroup = other.getIdGroup();
                if (this$idGroup == null) {
                    if (other$idGroup != null) {
                        return false;
                    }
                } else if (!this$idGroup.equals(other$idGroup)) {
                    return false;
                }

                Object this$image = this.getImage();
                Object other$image = other.getImage();
                if (this$image == null) {
                    if (other$image != null) {
                        return false;
                    }
                } else if (!this$image.equals(other$image)) {
                    return false;
                }

                Object this$color = this.getColor();
                Object other$color = other.getColor();
                if (this$color == null) {
                    if (other$color != null) {
                        return false;
                    }
                } else if (!this$color.equals(other$color)) {
                    return false;
                }

                return true;
            }
        }
    }

    public int hashCode() {
        var PRIME = true;
        int result = 1;
        Object $idGroupCrud = this.getIdGroupCrud();
        result = result * 59 + ($idGroupCrud == null ? 43 : $idGroupCrud.hashCode());
        Object $idGroup = this.getIdGroup();
        result = result * 59 + ($idGroup == null ? 43 : $idGroup.hashCode());
        Object $image = this.getImage();
        result = result * 59 + ($image == null ? 43 : $image.hashCode());
        Object $color = this.getColor();
        result = result * 59 + ($color == null ? 43 : $color.hashCode());
        return result;
    }

    public String toString() {
        return "GroupCrudView{" +
                "idGroupCrud=" + idGroupCrud +
                ", idGroup='" + idGroup + '\'' +
                ", image='" + image + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
