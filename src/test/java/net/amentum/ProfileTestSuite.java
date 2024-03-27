package net.amentum;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.amentum.security.views.PermissionView;
import net.amentum.security.views.ProfileView;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author dev06
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = {SecurityApplication.class})
public class ProfileTestSuite  {

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
     *Prueba para generar un nuevo perfil
     * */
    @Test
    public void createProfile() throws Exception{
        ProfileView profileView = new ProfileView();
        profileView.setProfileName("Perfil de pruebas");

        List<PermissionView> permissionViews = new ArrayList<>();

        PermissionView p1 = new PermissionView();
        p1.setPermissionId(7L);
        permissionViews.add(p1);

        PermissionView p2 = new PermissionView();
        p2.setPermissionId(8L);
        permissionViews.add(p2);


        PermissionView p3 = new PermissionView();
        p3.setPermissionId(16L);
        permissionViews.add(p3);

        profileView.setProfilePermissions(permissionViews);

        mockMvc.perform(MockMvcRequestBuilders.post("/profiles")
            .contentType(jsonType)
            .content(objectMapper.writeValueAsString(profileView)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Prueb para editar un perfil
     * */
    @Test
    public void editProfile() throws  Exception{
        ProfileView profileView = new ProfileView();
        profileView.setProfileName("Perfil de pruebas");

        List<PermissionView> permissionViews = new ArrayList<>();
        PermissionView p2 = new PermissionView();
        p2.setPermissionId(8L);
        permissionViews.add(p2);


        PermissionView p3 = new PermissionView();
        p3.setPermissionId(16L);
        permissionViews.add(p3);

        profileView.setProfilePermissions(permissionViews);

        mockMvc.perform(MockMvcRequestBuilders.put("/profiles/{profileId}",4)
                .contentType(jsonType)
                .content(objectMapper.writeValueAsString(profileView)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Prueba para eliminar perfil, si se envia un ID inexixtente descomentar las lineas cuando se recibe error 500
     * */
    @Test
    public void deleteProfile() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/profiles/{profileId}",20))
        .andExpect(MockMvcResultMatchers.status().isOk());

        /*
        mockMvc.perform(MockMvcRequestBuilders.delete("/profiles/{profileId}",20))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andDo(MockMvcResultHandlers.print());*/
    }

    /**
     * Método para probar el servicio que obtiene arbol de permisos
     * */
    @Test
    public void getTree() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/profiles/tree"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Método para probar el servicio que obtiene el detalle del perfil, junto con sus permisos,
     * se debe utilizar le ID de un perfil que exista en base de datos
     * */
    @Test
    public void getProfile() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/profiles/{profileId}",4))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Método para obtener lista de perfiles de forma paginada
     * */
    @Test
    public void getProfiles() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/profiles").param("name","")
                .param("page","0").param("size","3").param("orderColumn","profileId").param("orderType","desc"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Método para obtener lista de perfiles sin paginado
     * */
    @Test
    public void findAll() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/profiles/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


}
