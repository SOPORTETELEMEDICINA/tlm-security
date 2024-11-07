package net.amentum.security.service;

import net.amentum.security.views.AppConfigurationView;

import java.util.Optional;

public interface AppConfigurationService {

    Optional<AppConfigurationView> getConfigurationById(Long id);
}
