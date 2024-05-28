package net.amentum.security.rest;

import net.amentum.common.BaseController;
import net.amentum.security.exception.AppConfigurationException;
import net.amentum.security.service.AppConfigurationService;
import net.amentum.security.views.AppConfigurationView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("configurations")
public class AppConfigurationRest extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(AppConfigurationRest.class);

    @Autowired
    private AppConfigurationService appConfigurationService;

    @RequestMapping(value = "IdAppConfiguration/{IdAppConfiguration}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public AppConfigurationView getConfigurationById(@PathVariable("IdAppConfiguration") Long IdAppConfiguration) throws AppConfigurationException {
        try {
            AppConfigurationView configuration = appConfigurationService.getConfigurationById(IdAppConfiguration);
            if (configuration.isPresent()) {
                return configuration.get();
            } else {
                throw new AppConfigurationException("Configuración no encontrada", AppConfigurationException.LAYER_SERVICE, AppConfigurationException.ACTION_SELECT);
            }
        } catch (Exception ex) {
            AppConfigurationException acae = new AppConfigurationException("No fue posible obtener la configuración del cliente", AppConfigurationException.LAYER_SERVICE, AppConfigurationException.ACTION_SELECT);
            acae.addError("Ocurrió un error al obtener la configuración del Cliente: {" + IdAppConfiguration + "}");
            logger.error("Error al obtener la configuración - CODE: {} - {}", acae.getExceptionCode(), IdAppConfiguration, ex);
            throw acae;
        }
    }
}
