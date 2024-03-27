package net.amentum.security.converter;

import net.amentum.security.model.UserAlenta;
import net.amentum.security.views.UserAlentaView;
import org.springframework.stereotype.Component;

@Component
public class UserAlentaConverter {

    public UserAlenta toEntity(UserAlentaView view) {
        UserAlenta entity = new UserAlenta();
        entity.setIdUser(view.getIdUser());
        entity.setIdAlenta(view.getIdAlenta());
        return entity;
    }

    public UserAlentaView toView(UserAlenta entity) {
        UserAlentaView view = new UserAlentaView();
        view.setIdRelation(entity.getIdRelation());
        view.setIdUser(entity.getIdUser());
        view.setIdAlenta(entity.getIdAlenta());
        return view;
    }
}
