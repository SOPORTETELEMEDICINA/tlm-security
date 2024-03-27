package net.amentum;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.amentum.security.views.ShiftHourView;
import net.amentum.security.views.WorkShiftView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  @author marellano
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = {SecurityApplication.class})
public class WorkShiftTestSuite {
    public MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private final MediaType jsonType = new MediaType(MediaType.APPLICATION_JSON.getType(),MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf-8"));

    @Before
    public  void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * Prueba para generar un nuevo turno laboral
     * */
    @Test
    public void createWorkShift() throws Exception{

        String time1 = "14:00:01";
        String time11 = "18:30:15";

        String time2 = "01:15:10";
        String time22 = "19:00:00";

        DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        Date date = sdf.parse(time1);
        Date date1 = sdf.parse(time11);
        Date date2 = sdf.parse(time2);
        Date date22 = sdf.parse(time22);

        WorkShiftView view = new WorkShiftView();
        view.setName("Prueba 5");
        view.setStatus(true);

        List<ShiftHourView> hourViewList = new ArrayList<>();
        ShiftHourView hour1 = new ShiftHourView();
        hour1.setDay(4L);
        hour1.setEndTime(date);
        hour1.setStartTime(date1);
        hourViewList.add(hour1);

        ShiftHourView hour2 = new ShiftHourView();
        hour2.setDay(5L);
        hour2.setEndTime(date);
        hour2.setStartTime(date1);
        hourViewList.add(hour2);

        ShiftHourView hour3 = new ShiftHourView();
        hour3.setDay(6L);
        hour3.setEndTime(date);
        hour3.setStartTime(date1);
        hourViewList.add(hour3);

        ShiftHourView hour4 = new ShiftHourView();
        hour4.setDay(0L);
        hour4.setEndTime(date);
        hour4.setStartTime(date1);
        hourViewList.add(hour4);

        view.setShiftHourViews(hourViewList);
        mockMvc.perform(MockMvcRequestBuilders.post("/work/shifts")
                .contentType(jsonType)
                .content(objectMapper.writeValueAsString(view)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Prueba para editar un turno laboral
     * */
    @Test
    public void editWorkShift() throws Exception{

        String time1 = "07:00:01";
        String time11 = "01:30:15";

        String time2 = "14:00:01";
        String time22 = "18:30:15";


        DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        Date date = sdf.parse(time1);
        Date date1 = sdf.parse(time11);
        Date date2 = sdf.parse(time2);
        Date date22 = sdf.parse(time22);

        WorkShiftView view = new WorkShiftView();
        view.setName("Se modifico Prueba 5");
        view.setStatus(true);

        List<ShiftHourView> hourViewList = new ArrayList<>();
        ShiftHourView hour1 = new ShiftHourView();
        hour1.setDay(1L);
        hour1.setEndTime(date);
        hour1.setStartTime(date1);
        hourViewList.add(hour1);

        ShiftHourView hour2 = new ShiftHourView();
        hour2.setDay(3L);
        hour2.setEndTime(date);
        hour2.setStartTime(date1);
        hourViewList.add(hour2);

        ShiftHourView hour3 = new ShiftHourView();
        hour3.setDay(4L);
        hour3.setEndTime(date2);
        hour3.setStartTime(date22);
        hourViewList.add(hour3);

        view.setShiftHourViews(hourViewList);
        mockMvc.perform(MockMvcRequestBuilders.put("/work/shifts/{idWorkShift}", 6)
                .contentType(jsonType)
                .content(objectMapper.writeValueAsString(view)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    /**
     * Prueba para eliminar un turno laboral
     * */
    @Test
    public void deleteWorkShift() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/work/shifts/{idWorkShift}", 3)
                .contentType(jsonType))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Prueba para obtener un turno laboral
     * */
    @Test
    public void getWorkShiftById() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/work/shifts/{idWorkShift}", 6)
                .contentType(jsonType))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    /**
     * Prueba para obtener todos los turnos  laborales
     * */
    @Test
    public void getWorkShift() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/work/shifts" )
                .contentType(jsonType))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Prueba para obtener todos los turnos  laborales paginable
     * */
    @Test
    public void getWorkShiftPage() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/work/shifts/page?active=&name=prueba&page=&size=&orderColumn=name&orderType=desc" )
                .contentType(jsonType))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
