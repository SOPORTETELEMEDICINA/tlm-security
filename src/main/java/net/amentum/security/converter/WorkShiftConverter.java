package net.amentum.security.converter;

import net.amentum.security.model.ShiftHour;
import net.amentum.security.model.WorkShift;
import net.amentum.security.views.ShiftHourView;
import net.amentum.security.views.WorkShiftView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class WorkShiftConverter {

    private Logger logger = LoggerFactory.getLogger(WorkShiftConverter.class);

    public WorkShift workShiftConverterToEntity(WorkShiftView view, WorkShift workShift){
        logger.info(" - - workShift: {}", workShift);
        Timestamp date = new Timestamp(System.currentTimeMillis());
        if(workShift == null){
            workShift = new WorkShift();
            workShift.setCreatedDate(date);
        }
        workShift.setModifiedDate(date);
        workShift.setName(view.getName());
        workShift.setStatus(view.getStatus());

        if(!view.getShiftHourViews().isEmpty() && view.getShiftHourViews() != null){
            for (ShiftHourView shv: view.getShiftHourViews()) {
                ShiftHour sh = new ShiftHour();
                sh.setDay(shv.getDay());
                sh.setEndTime(shv.getEndTime());
                sh.setStartTime(shv.getStartTime());
                sh.setWorkShift(workShift);
                workShift.getShiftHours().add(sh);
            }
        }
        logger.info(" * * * workShift: {}", workShift);
        return workShift;
    }

    public WorkShiftView workShiftConverterToView(WorkShift work, Boolean all){
        WorkShiftView view = new WorkShiftView();
        view.setIdWorkShift(work.getIdWorkShift());
        view.setStatus(work.getStatus());
        view.setName(work.getName());
        view.setModifiedDate(work.getModifiedDate());
        view.setCreatedDate(work.getCreatedDate());
        if(all){
            List<ShiftHourView> hourViews = new ArrayList<>();
            for (ShiftHour s:work.getShiftHours()) {
                ShiftHourView sView = new ShiftHourView();
                sView.setStartTime(s.getStartTime());
                sView.setEndTime(s.getEndTime());
                sView.setDay(s.getDay());
                sView.setIdHour(s.getIdShiftHour());
                hourViews.add(sView);
            }
            view.setShiftHourViews(hourViews);
        }

        return view;
    }
}
