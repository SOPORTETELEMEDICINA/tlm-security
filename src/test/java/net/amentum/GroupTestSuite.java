package net.amentum;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.amentum.security.model.Group;
import net.amentum.security.model.TicketCategory;
import net.amentum.security.model.TicketType;
import net.amentum.security.views.GroupView;
import net.amentum.security.views.TicketCategoryView;
import net.amentum.security.views.TicketTypeView;
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

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by dev06 on 17/03/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = {SecurityApplication.class})
public class GroupTestSuite  {

    public MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private final MediaType jsonType = new MediaType(MediaType.APPLICATION_JSON.getType(),MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf-8"));

    private String createdGroupName = "";

    @Before
    public  void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * Prueba para agregar un nuevo grupo, el resultado es un BAD_REQUEST ya que no se estan enviando datos correctos
     * */
    @Test
    public void testCreateGroup() throws Exception{
        Group group = new Group();
        mockMvc.perform(
                MockMvcRequestBuilders.post("/groups")
                .contentType(jsonType)
                .content(objectMapper.writeValueAsString(group))
        )
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Prueba para grupo correcto
     * */
    @Test
    public void testCorrectCreateGroup() throws Exception{
        GroupView group = new GroupView();
        group.setActive(Boolean.TRUE);
        group.setGroupName("Grupoo NUEVO ");

        List<TicketTypeView> ticketType = new ArrayList<>();

        TicketTypeView t1 = new TicketTypeView();
        t1.setIdTicketType(41l);
        t1.setTypeTicket("Call Center");

        List<TicketCategoryView> ticketCategory = new ArrayList<>();

        TicketCategoryView c1 = new TicketCategoryView();
        c1.setCategoryName("Call Center");
        c1.setIdTicketCategory(45l);
        ticketCategory.add(c1);

        t1.setTicketCategoryView(ticketCategory);

        TicketTypeView t2 = new TicketTypeView();
        t2.setIdTicketType(28l);
        t2.setTypeTicket("Help Desk");

        List<TicketCategoryView> ticketCategory2 = new ArrayList<>();

        TicketCategoryView tc1 = new TicketCategoryView();
        tc1.setIdTicketCategory(94l);
        tc1.setCategoryName("Redes");
        ticketCategory2.add(tc1);

        TicketCategoryView tc2 = new TicketCategoryView();
        tc2.setIdTicketCategory(104l);
        tc2.setCategoryName("Servidores");
        ticketCategory2.add(tc2);

        TicketCategoryView tc3 = new TicketCategoryView();
        tc3.setIdTicketCategory(25l);
        tc3.setCategoryName("Actividad Especial");
        ticketCategory2.add(tc3);

        TicketCategoryView tc4 = new TicketCategoryView();
        tc4.setIdTicketCategory(28l);
        tc4.setCategoryName("PC's");
        ticketCategory2.add(tc4);

        t2.setTicketCategoryView(ticketCategory2);

        ticketType.add(t1);
        ticketType.add(t2);
        group.setTicketTypeView(ticketType);


        mockMvc.perform(
                MockMvcRequestBuilders.post("/groups")
                        .contentType(jsonType)
                        .content(objectMapper.writeValueAsString(group))
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }


    /**
     * Prueba para probar que no se repita el nombre del grupo
     * */
    @Test
    public void testCreatedGroupName() throws Exception{
        Group group = new Group();
        group.setActive(Boolean.TRUE);
        group.setGroupName("b6783ff0-43ef-4113-958a-69576bc33e1f");//utilizar valor de base de datos, nombre de grupo creado previamente
        mockMvc.perform(MockMvcRequestBuilders.post("/groups")
                .contentType(jsonType)
                .content(objectMapper.writeValueAsString(group)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andDo(MockMvcResultHandlers.print());
    }


    /**
     * Prueba para editar un grupo de forma correcta
     * */
    @Test
    public void testEditGroupName() throws Exception{
        GroupView group = new GroupView();
        group.setActive(Boolean.TRUE);
        group.setGroupName("Grupoo NUEVO ");

        List<TicketTypeView> ticketType = new ArrayList<>();

        TicketTypeView t1 = new TicketTypeView();
        t1.setIdTicketType(41l);
        t1.setTypeTicket("Call Center");

        List<TicketCategoryView> ticketCategory = new ArrayList<>();

        TicketCategoryView c1 = new TicketCategoryView();
        c1.setCategoryName("Alo Site");
        c1.setIdTicketCategory(46l);
        ticketCategory.add(c1);

        TicketCategoryView c2 = new TicketCategoryView();
        c2.setCategoryName("Call Center");
        c2.setIdTicketCategory(45l);
        ticketCategory.add(c2);

        t1.setTicketCategoryView(ticketCategory);

        TicketTypeView t2 = new TicketTypeView();
        t2.setIdTicketType(28l);
        t2.setTypeTicket("Help Desk");

        List<TicketCategoryView> ticketCategory2 = new ArrayList<>();

        TicketCategoryView tc1 = new TicketCategoryView();
        tc1.setIdTicketCategory(94l);
        tc1.setCategoryName("Redes");
        ticketCategory2.add(tc1);

        TicketCategoryView tc2 = new TicketCategoryView();
        tc2.setIdTicketCategory(104l);
        tc2.setCategoryName("Servidores");
        ticketCategory2.add(tc2);

        TicketCategoryView tc3 = new TicketCategoryView();
        tc3.setIdTicketCategory(25l);
        tc3.setCategoryName("Actividad Especial");
        ticketCategory2.add(tc3);

        TicketCategoryView tc4 = new TicketCategoryView();
        tc4.setIdTicketCategory(28l);
        tc4.setCategoryName("PC's");
        ticketCategory2.add(tc4);

        t2.setTicketCategoryView(ticketCategory2);

        ticketType.add(t1);
        //ticketType.add(t2);
        group.setTicketTypeView(ticketType);


        mockMvc.perform(MockMvcRequestBuilders.put("/groups/{groupId}",7L) //utilizar valor de base de datos, el ID de un grupo creado previamente
                .contentType(jsonType)
                .content(objectMapper.writeValueAsString(group)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Prueba para editar grupo, la respuesta debe ser erronea ya que se envia un grupo que no existe en base de datos
     * */
    @Test
    public void testEditGroupNameError() throws Exception{
        Group group = new Group();
        group.setActive(Boolean.TRUE);
        group.setGroupName("Software Users");
        mockMvc.perform(MockMvcRequestBuilders.put("/groups/{groupId}",1000L) //asegurarse que el ID no existe en BD para que pase la prueba
                .contentType(jsonType)
                .content(objectMapper.writeValueAsString(group)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andDo(MockMvcResultHandlers.print());
    }


    /**
     * Prueba para eliminar grupo, utilizar un ID que no exista en bases de datos, para que la prueba sea exitosa
     * */
    @Test
    public void deleteGroupError() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/groups/{groupId}",1000L))
        .andExpect(MockMvcResultMatchers.status().isInternalServerError())
        .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Prueba para elimianr grupo, utilizar un ID que exista en BD y que no tenga relaciones con la tabla de usuarios {@link net.amentum.security.model.UserHasGroup}
     * */
    @Test
    public void deleteGroupSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/groups/{groupId}",7L))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    /**
     * Prueba para obtener un grupo especifico utilizando el ID
     * */
    @Test
    public void getGroup() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/groups/{groupId}",8L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Prueba para obtener lista de grupos utilizando filtros para saber si estan activos
     * */
    @Test
    public void getGroups() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/groups?active=true"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.get("/groups?active=false"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.get("/groups?name=a"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Obtiene lista de grupos sin paginar
     * */
    @Test
    public void findAllGroups() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/groups/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
