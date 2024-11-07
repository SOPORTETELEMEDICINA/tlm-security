package net.amentum.security.views;

import java.io.Serializable;

public class AppConfigurationView implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idCliente;

    private String cliente;

    private String telefono;
    private String telefonoEmergencia;
    private String urlAgenda;
    private String urlvideollamadas;
    private String urlchat;
    private String urlmail;
    private String urlprivacidad;
    private String urlsms;
    private String campo1;
    private String campo2;
    private String campo3;
    private String campo4;

    // Getters and setters

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefonoEmergencia() {
        return telefonoEmergencia;
    }

    public void setTelefonoEmergencia(String telefonoEmergencia) {
        this.telefonoEmergencia = telefonoEmergencia;
    }

    public String getUrlAgenda() {
        return urlAgenda;
    }

    public void setUrlAgenda(String urlAgenda) {
        this.urlAgenda = urlAgenda;
    }

    public String getUrlvideollamadas() {
        return urlvideollamadas;
    }

    public void setUrlvideollamadas(String urlvideollamadas) {
        this.urlvideollamadas = urlvideollamadas;
    }

    public String getUrlchat() {
        return urlchat;
    }

    public void setUrlchat(String urlchat) {
        this.urlchat = urlchat;
    }

    public String getUrlmail() {
        return urlmail;
    }

    public void setUrlmail(String urlmail) {
        this.urlmail = urlmail;
    }

    public String getUrlprivacidad() {
        return urlprivacidad;
    }

    public void setUrlprivacidad(String urlprivacidad) {
        this.urlprivacidad = urlprivacidad;
    }

    public String getUrlsms() {
        return urlsms;
    }

    public void setUrlsms(String urlsms) {
        this.urlsms = urlsms;
    }

    public String getCampo1() {
        return campo1;
    }

    public void setCampo1(String campo1) {
        this.campo1 = campo1;
    }

    public String getCampo2() {
        return campo2;
    }

    public void setCampo2(String campo2) {
        this.campo2 = campo2;
    }

    public String getCampo3() {
        return campo3;
    }

    public void setCampo3(String campo3) {
        this.campo3 = campo3;
    }

    public String getCampo4() {
        return campo4;
    }

    public void setCampo4(String campo4) {
        this.campo4 = campo4;
    }
}
