package net.amentum.security.converter;

import net.amentum.security.model.AppConfiguration;
import net.amentum.security.views.AppConfigurationView;

public class AppConfigurationConverter {

    public static AppConfigurationView toView(AppConfiguration appConfiguration) {
        AppConfigurationView view = new AppConfigurationView();
        view.setIdCliente(appConfiguration.getIdCliente());
        view.setCliente(appConfiguration.getCliente());
        view.setTelefono(appConfiguration.getTelefono());
        view.setTelefonoEmergencia(appConfiguration.getTelefonoEmergencia());
        view.setUrlAgenda(appConfiguration.getUrlAgenda());
        view.setUrlvideollamadas(appConfiguration.getUrlvideollamadas());
        view.setUrlchat(appConfiguration.getUrlchat());
        view.setUrlmail(appConfiguration.getUrlmail());
        view.setUrlprivacidad(appConfiguration.getUrlprivacidad());
        view.setUrlsms(appConfiguration.getUrlsms());
        view.setCampo1(appConfiguration.getCampo1());
        view.setCampo2(appConfiguration.getCampo2());
        view.setCampo3(appConfiguration.getCampo3());
        view.setCampo4(appConfiguration.getCampo4());
        return view;
    }

    public static AppConfiguration toEntity(AppConfigurationView view) {
        AppConfiguration appConfiguration = new AppConfiguration();
        appConfiguration.setIdCliente(view.getIdCliente());
        appConfiguration.setCliente(view.getCliente());
        appConfiguration.setTelefono(view.getTelefono());
        appConfiguration.setTelefonoEmergencia(view.getTelefonoEmergencia());
        appConfiguration.setUrlAgenda(view.getUrlAgenda());
        appConfiguration.setUrlvideollamadas(view.getUrlvideollamadas());
        appConfiguration.setUrlchat(view.getUrlchat());
        appConfiguration.setUrlmail(view.getUrlmail());
        appConfiguration.setUrlprivacidad(view.getUrlprivacidad());
        appConfiguration.setUrlsms(view.getUrlsms());
        appConfiguration.setCampo1(view.getCampo1());
        appConfiguration.setCampo2(view.getCampo2());
        appConfiguration.setCampo3(view.getCampo3());
        appConfiguration.setCampo4(view.getCampo4());
        return appConfiguration;
    }
}
