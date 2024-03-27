package net.amentum;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.amentum.security.views.ChangePasswordRequestView;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = {SecurityApplication.class})

public class RecoverPasswordTestSuite {

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


    @Test
    public void sendRecoverPasswordEmail() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/users/recoverPassword?username=vcruz"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void sendNewPassword() throws Exception{
        ChangePasswordRequestView view = new ChangePasswordRequestView();
        /*view.setHash("d459c1349f48661752725c0015a165504e541788");//usar hash recibido en correo*/
        view.setPassword("073fc82271614cbf712f74b1881c5ba584246c36d3fb00f39d4ef9f443359a44");
        view.setUsername("vcruz");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/recoverPassword")
                .contentType(jsonType)
                .content(objectMapper.writeValueAsString(view)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
