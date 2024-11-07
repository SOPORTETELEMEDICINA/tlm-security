package net.amentum.security.converter;

import net.amentum.security.model.GroupCrud;
import net.amentum.security.views.GroupCrudView;
import org.springframework.stereotype.Component;

@Component
public class GroupCrudConverter {

    public GroupCrud toEntity(GroupCrudView view) {
        GroupCrud entity = new GroupCrud();
        entity.setIdGroupCrud(view.getIdGroupCrud());
        entity.setIdGroup(view.getIdGroup());
        entity.setImage(view.getImage());
        entity.setColor(view.getColor());
        return entity;
    }

    public GroupCrudView toView(GroupCrud entity) {
        GroupCrudView view = new GroupCrudView();
        view.setIdGroupCrud(entity.getIdGroupCrud());
        view.setIdGroup(entity.getIdGroup());
        view.setImage(entity.getImage());
        view.setColor(entity.getColor());
        return view;
    }
}
