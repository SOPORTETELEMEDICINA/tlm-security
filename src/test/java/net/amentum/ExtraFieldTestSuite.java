package net.amentum;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.amentum.security.views.ExtraFieldView;
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
import java.util.Date;


/**
 * @author Victor
 * **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = {SecurityApplication.class})
public class ExtraFieldTestSuite  {


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
     * Prueba para agregar un nuevo campo
     * **/
    @Test
    public void addField() throws Exception{
        ExtraFieldView extraFieldView = new ExtraFieldView();
        extraFieldView.setExtraFieldId(null);
        extraFieldView.setProfileId(1L);
        extraFieldView.setActive(Boolean.TRUE);
        extraFieldView.setCreatedDate(new Date());
        extraFieldView.setFieldType("Number");
        extraFieldView.setFieldValidation("[0-9]{5}");
        extraFieldView.setKey("phoneNumber2");
        extraFieldView.setLegend("Número Local");
        extraFieldView.setValidationMessage("Ingrese número correcto");
        mockMvc.perform(MockMvcRequestBuilders.post("/fields")
                .contentType(jsonType)
                .content(objectMapper.writeValueAsString(
                        extraFieldView
                )))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void addField500() throws Exception{
        ExtraFieldView extraFieldView = new ExtraFieldView();
        extraFieldView.setExtraFieldId(null);
        extraFieldView.setProfileId(1L);
        extraFieldView.setActive(Boolean.TRUE);
        extraFieldView.setCreatedDate(new Date());
        extraFieldView.setFieldType("Number");
        extraFieldView.setFieldValidation("[0-9]{5}");
        extraFieldView.setKey("phoneNumber--");
        extraFieldView.setLegend("Número Local");
        extraFieldView.setValidationMessage("Ingrese número correcto --");
        mockMvc.perform(MockMvcRequestBuilders.post("/fields")
                .contentType(jsonType)
                .content(objectMapper.writeValueAsString(
                        extraFieldView
                )))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void editField() throws Exception{
        ExtraFieldView extraFieldView = new ExtraFieldView();
        extraFieldView.setExtraFieldId(null);
        extraFieldView.setProfileId(1L);
        extraFieldView.setActive(Boolean.TRUE);
        extraFieldView.setCreatedDate(new Date());
        extraFieldView.setFieldType("Number");
        extraFieldView.setFieldValidation("[0-9]{5}");
        extraFieldView.setKey("phoneNumber");
        extraFieldView.setLegend("Número Local Editado");
        extraFieldView.setValidationMessage("Ingrese número correcto");
        mockMvc.perform(MockMvcRequestBuilders.put("/fields/{fieldId}",1L)
                .contentType(jsonType)
                .content(objectMapper.writeValueAsString(
                        extraFieldView
                )))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteField() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/fields/{fieldId}",2L)
                .contentType(jsonType))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getField() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/fields/{fieldId}",1L)
                .contentType(jsonType))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getFieldsByProfile() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/fields/byProfileOrActive?profileId=1&active=true")
                .contentType(jsonType))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getPage() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/fields?page=0&size=10")
                .contentType(jsonType))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
