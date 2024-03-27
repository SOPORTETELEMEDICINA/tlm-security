package net.amentum;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.amentum.security.model.OauthClientDetails;
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
import java.util.UUID;

/**
 * @author dev06
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = {SecurityApplication.class})
public class OauthClientDetailTestSuite {

    public MockMvc mockMvc;



    private WebApplicationContext webApplicationContext;


    private ObjectMapper objectMapper;

    private final MediaType jsonType = new MediaType(MediaType.APPLICATION_JSON.getType(),MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf-8"));

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setWebApplicationContext(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }

    @Before
    public  void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * Prueba para agregar nuevo cliente oauth
     * */
    @Test
    public void addClientDetails() throws Exception{
        OauthClientDetails clientDetails = new OauthClientDetails();
        clientDetails.setAccessTokenValidity(30);
        clientDetails.setAdditionaInformation("");
        clientDetails.setAuthorities("ROLE_CLIENT,ROLE_TRUSTED_CLIENT");
        clientDetails.setAuthorizedGrantTypes("password,authorization_code,refresh_token,implicit,redirect");
        clientDetails.setAutoaprove("");
        clientDetails.setClientId("web-application-access");
        clientDetails.setClientSecret(UUID.randomUUID().toString());
        clientDetails.setRefreshTokenValidity(360);
        clientDetails.setResourceIds("");
        clientDetails.setScope("read,wirte,trust");
        clientDetails.setWebServerRedirectUri("/web");

        mockMvc.perform(MockMvcRequestBuilders.post("/oauth/details")
            .contentType(jsonType)
            .content(objectMapper.writeValueAsString(clientDetails)))
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    /**
     * Prueba para editar una autorización, se debe utilizar un ID qiue exista en BD
     * */
    @Test
    public void editClientDetails() throws Exception{
        OauthClientDetails clientDetails = new OauthClientDetails();
        clientDetails.setAccessTokenValidity(30);
        clientDetails.setAdditionaInformation("Información adicional");
        clientDetails.setAuthorities("ROLE_CLIENT,ROLE_TRUSTED_CLIENT");
        clientDetails.setAuthorizedGrantTypes("password,authorization_code,refresh_token,implicit,redirect");
        clientDetails.setAutoaprove("");
        clientDetails.setClientId("web-application-access");
        clientDetails.setClientSecret(UUID.randomUUID().toString());
        clientDetails.setRefreshTokenValidity(360);
        clientDetails.setResourceIds("");
        clientDetails.setScope("read,wirte,trust");
        clientDetails.setWebServerRedirectUri("/web");

        mockMvc.perform(MockMvcRequestBuilders.put("/oauth/details/{id}",1)
                .contentType(jsonType)
                .content(objectMapper.writeValueAsString(clientDetails)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Prueba para obtener detalles de autorización oauth
     * */
    @Test
    public void getClientDetails() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/oauth/details/{id}",1))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Prueba para obtener listado de autorización oauth
     * */
    @Test
    public void getClientDetailsPage() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/oauth/details"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Prueba para elimininar una autorización, se debe usar un ID valido
     * */
    @Test
    public void deleteClientDetailsPage() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/oauth/details/{id}",1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }



}
