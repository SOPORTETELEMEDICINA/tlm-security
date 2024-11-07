package net.amentum.security.service;

import net.amentum.security.exception.GroupCrudException;
import net.amentum.security.views.GroupCrudView;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface GroupCrudService {

    void addGroupCrud(GroupCrudView groupCrud) throws GroupCrudException;

    void editGroupCrud(GroupCrudView groupCrud) throws GroupCrudException;

    void deleteGroupCrud(Long groupCrudId) throws GroupCrudException;

    Page<GroupCrudView> findGroupCruds(Integer gid, Integer page, Integer size, String columnOrder, String orderType) throws GroupCrudException;

    List<GroupCrudView> findAll() throws GroupCrudException;

    String findImageGroupCrud (@NotNull Integer idGroup, @NotNull String color) throws GroupCrudException;

    GroupCrudView findGroupCrud(Long groupCrudId) throws  GroupCrudException;

}
