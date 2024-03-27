package net.amentum.security.converter;

import net.amentum.security.model.*;
import net.amentum.security.persistence.GroupHasCategoryRepository;
import net.amentum.security.persistence.TicketCategoryRepository;
import net.amentum.security.persistence.TicketTypeRepository;
import net.amentum.security.views.GroupView;
import net.amentum.security.views.TicketCategoryView;
import net.amentum.security.views.TicketTypeView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class GroupConverter {

    private TicketCategoryRepository ticketCategoryRepository;

    private TicketTypeRepository ticketTypeRepository;

    private GroupHasCategoryRepository groupHasCategoryRepository;

    @Autowired
    public void setGroupHasCategoryRepository(GroupHasCategoryRepository groupHasCategoryRepository) {
        this.groupHasCategoryRepository = groupHasCategoryRepository;
    }

    @Autowired
    public void setTicketTypeRepository(TicketTypeRepository ticketTypeRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
    }

    @Autowired
    public void setTicketCategoryRepository(TicketCategoryRepository ticketCategoryRepository) {
        this.ticketCategoryRepository = ticketCategoryRepository;
    }

    private Logger logger = LoggerFactory.getLogger(GroupConverter.class);

    public Group convertToEntity(GroupView view, Group g){
        if(g == null){
            g = new Group();
        }

        g.setActive(view.getActive());
        g.setGroupName(view.getGroupName());
        g.setImagen(view.getImagen()); // GGR20200612 imagen de grupo

        List<GroupHasCategory> groupHasCategories = new ArrayList<>();
        List<GroupHasTypeTicket> groupHasTypeTickets = new ArrayList<>();
        for (TicketTypeView ttView: view.getTicketTypeView()) {
            TicketType ticketType = ticketTypeRepository.findOne(ttView.getIdTicketType());
            if (ticketType == null) {
                ticketType = new TicketType();
                ticketType.setIdTicketType(ttView.getIdTicketType());
                ticketType.setTypeTicket(ttView.getTypeTicket());
                ticketTypeRepository.save(ticketType);
            }
            GroupHasTypeTicket hasTypeTicket = new GroupHasTypeTicket();
            hasTypeTicket.setTicketType(ticketType);
            hasTypeTicket.setGroup(g);
            groupHasTypeTickets.add(hasTypeTicket);

            if (ttView.getTicketCategoryView() != null) { // GGR20200612 A partir de que no es requerido
                    for (TicketCategoryView tcView : ttView.getTicketCategoryView()) {
                        TicketCategory ticketCategory = ticketCategoryRepository.findOne(tcView.getIdTicketCategory());
                        if (ticketCategory == null) {
                            ticketCategory = new TicketCategory();
                            ticketCategory.setIdTicketCategory(tcView.getIdTicketCategory());
                            ticketCategory.setCategoryName(tcView.getCategoryName());
                            ticketCategory.setTypeTicket(ticketType);
                            ticketCategoryRepository.save(ticketCategory);
                        }
                        GroupHasCategory hasCategory = new GroupHasCategory();
                        hasCategory.setTicketCategory(ticketCategory);
                        hasCategory.setGroup(g);
                        groupHasCategories.add(hasCategory);
                    }
                    g.setCategoryList(groupHasCategories);
            }
        }
        g.setTypeTicketList(groupHasTypeTickets);

/*        TicketType ticketType = ticketTypeRepository.findOne(view.getTicketTypeView().getIdTicketType());
        if(ticketType == null ){
            ticketType = new TicketType();
            ticketType.setTypeTicket(view.getTicketTypeView().getTypeTicket());
            ticketType.setIdTicketType(view.getTicketTypeView().getIdTicketType());
            //ticketTypeRepository.save(ticketType);
        }
        g.setTypeTicket(ticketType);

        List<GroupHasCategory> groupHasCategories = new ArrayList<>();
        for (TicketCategoryView tcView : view.getTicketCategoryView()) {

            TicketCategory tc = ticketCategoryRepository.findOne(tcView.getIdTicketCategory());
            if(tc == null){
                tc = new TicketCategory();
                tc.setCategoryName(tcView.getCategoryName());
                tc.setIdTicketCategory(tcView.getIdTicketCategory());
                ticketCategoryRepository.save(tc);
            }
            GroupHasCategory hasCategory = new GroupHasCategory();
            hasCategory.setGroup(g);
            hasCategory.setTicketCategory(tc);
            groupHasCategories.add(hasCategory);
        }
        g.setCategoryList(groupHasCategories);*/

        return g;
    }

    public GroupView convertToView(Group group){
        GroupView view = new GroupView();
        view.setGroupName(group.getGroupName());
        view.setActive(group.getActive());
        view.setGroupId(group.getGroupId());
        view.setCreatedDate(group.getCreatedDate());
        view.setImagen(group.getImagen()); // GGR20200612 Regreso también imagen del grupo

        List<TicketTypeView> typeViewList = new ArrayList<>();

        if(group.getTypeTicketList() != null && group.getTypeTicketList().size() > 0 ){

            for (GroupHasTypeTicket tt : group.getTypeTicketList() ) {
                TicketType  ticketType = tt.getTicketType();
                TicketTypeView typeView = new TicketTypeView();
                typeView.setTypeTicket(ticketType.getTypeTicket());
                typeView.setIdTicketType(ticketType.getIdTicketType());

                List<TicketCategoryView> categoryViewList = new ArrayList<>();
                if(group.getCategoryList() != null && group.getCategoryList().size() > 0){
                    for (GroupHasCategory category : group.getCategoryList() ) {
                        TicketCategory ticketCategory = category.getTicketCategory();

                        if(ticketCategory.getTypeTicket() != null && ticketCategory.getTypeTicket().getIdTicketType() == ticketType.getIdTicketType()){
                            TicketCategoryView categoryView = new TicketCategoryView();
                            categoryView.setCategoryName(ticketCategory.getCategoryName());
                            categoryView.setIdTicketCategory(ticketCategory.getIdTicketCategory());

                            categoryViewList.add(categoryView);
                        }
                        // typeView.setTicketCategoryView(categoryViewList); // Sre02072020 Se fija abajo
                    }
                }
                typeView.setTicketCategoryView(categoryViewList); // Sre02072020 Para que no de NPE
                typeViewList.add(typeView);
            }
        }
        view.setTicketTypeView(typeViewList);

/*        if(group.getTypeTicket() != null){
            TicketTypeView typeView = new TicketTypeView();
            typeView.setTypeTicket(group.getTypeTicket().getTypeTicket());
            typeView.setIdTicketType(group.getTypeTicket().getIdTicketType());
            view.setTicketTypeView(typeView);
        }

        if(group.getCategoryList() != null){
            List<TicketCategoryView> viewList = new ArrayList<>();
            for (GroupHasCategory ghc: group.getCategoryList()) {
                TicketCategory ticketCategory = ghc.getTicketCategory();
                TicketCategoryView categoryView = new TicketCategoryView();
                categoryView.setIdTicketCategory(ticketCategory.getIdTicketCategory());
                categoryView.setCategoryName(ticketCategory.getCategoryName());
                viewList.add(categoryView);
            }
            view.setTicketCategoryView(viewList);
        }*/

        return view;
    }
}
