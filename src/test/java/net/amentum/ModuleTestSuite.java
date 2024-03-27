package net.amentum;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.amentum.security.model.Module;
import net.amentum.security.model.ModulePermission;
import net.amentum.security.views.ModuleView;
import net.amentum.security.views.PermissionView;
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
import java.util.UUID;

/**
 * Created by dev06 on 24/04/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = {SecurityApplication.class})
public class ModuleTestSuite {

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
     * Permite crear un nuevo modulo a través de la interfaz REST, el json equivalente es
     * {
     *     "moduleName":"Nombre de modulo",
     *     "permissions": [
     *          {
     *              "namePermission":"Nombre del permiso",
     *              "codePermission":"Codigo"
     *          }
     *     ]
     * }
     * */
    @Test
    public void testCreateModule() throws Exception{
        ModuleView module = new ModuleView();
        //random uuid previene que se cree el mismo nombre de modulo
        module.setModuleName(UUID.randomUUID().toString());
        List<PermissionView> permissions = new ArrayList<>();
        PermissionView md = new PermissionView();
        md.setNamePermission("Crear usuarios");
        md.setCodePermission("ADD_NEW_USER");
        permissions.add(md);
        module.setModulePermissions(permissions);
        mockMvc.perform(MockMvcRequestBuilders.post("/modules")
                .contentType(jsonType)
                .content(objectMapper.writeValueAsString(module)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    /**
     * Permite editar un módulo
     * {
     *     "moduleName":"Nombre de modulo",
     *     "permissions": [
     *          {
     *              "namePermission":"Nombre del permiso",
     *              "codePermission":"Codigo",
     *              "permissionId":18
     *          }
     *     ]
     * }
     * */
    @Test
    public void testEditModule() throws Exception{
        ModuleView module = new ModuleView();
        //random uuid previene que se cree el mismo nombre de modulo
        module.setModuleName("Edición de módulo");
        List<PermissionView> permissions = new ArrayList<>();
        PermissionView md = new PermissionView();
        md.setPermissionId(18L);
        md.setNamePermission("Crear usuarios");
        md.setCodePermission("ADD_NEW_USER");
        permissions.add(md);
        module.setModulePermissions(permissions);
        mockMvc.perform(MockMvcRequestBuilders.put("/modules/{moduleId}",12)
                .contentType(jsonType)
                .content(objectMapper.writeValueAsString(module)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    /**
     * Método para probar la elimincación de un módulo en base de datos, se debe utilizar un aprametro que exista en base de datos para realizar la operación de manera exitosa
     * */
    @Test
    public void deleteModule() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/modules/{moduleId}",12))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Método para probar la elimincación de un módulo en base de datos,
     * este médodo ejemplifica un error esperado qya que el ID que se envía no existe en bas e de datos
     * */
    @Test
    public void deleteModuleError() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/modules/{moduleId}",12))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Método para obtener lista de módulos de forma paginada
     * */
    @Test
    public void getPaginatedModuleList() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/modules").param("name","")
            .param("page","0").param("size","3").param("orderColumn","moduleId").param("orderType","desc"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Método para obtener el detalle de un módulo
     * utilizar un ID existente en base de datos
     * */
    @Test
    public void getSingleModule() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/modules/{module-id}",4))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print());
    }

}
