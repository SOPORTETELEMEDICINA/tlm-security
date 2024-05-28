package net.amentum.security.service.impl;

import net.amentum.security.converter.AppConfigurationConverter;
import net.amentum.security.model.AppConfiguration;
import net.amentum.security.service.AppConfigurationService;
import net.amentum.security.views.AppConfigurationView;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AppConfigurationServiceImpl implements AppConfigurationService {

    private final List<AppConfiguration> appConfigurations = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    @Override
    public Optional<AppConfigurationView> getConfigurationById(Long id) {
        return appConfigurations.stream()
                .filter(config -> config.getIdCliente().equals(id))
                .findFirst()
                .map(AppConfigurationConverter::toView);
    }
}
