package net.amentum.security.views;

import java.io.Serializable;
import java.util.Date;

public class UsuariosCanalizadosView implements Serializable {

    private Long idUsuarioEmisor;
    private Long idUsuarioReceptor;
    private Long idUsuarioPaciente;
    private Date fechaInicial;
    private Date fechaFinal;
    private Long usuariosCanalizadosId;
    private String nombrePaciente;
    private String nombreEmisor;
    private String nombreReceptor;


    public Long getUsuariosCanalizadosId() {
        return usuariosCanalizadosId;
    }

    public void setUsuariosCanalizadosId(Long usuariosCanalizadosId) {
        this.usuariosCanalizadosId = usuariosCanalizadosId;
    }

    public Long getIdUsuarioEmisor() {
        return idUsuarioEmisor;
    }

    public void setIdUsuarioEmisor(Long idUsuarioEmisor) {
        this.idUsuarioEmisor = idUsuarioEmisor;
    }

    public Long getIdUsuarioReceptor() {
        return idUsuarioReceptor;
    }

    public void setIdUsuarioReceptor(Long idUsuarioReceptor) {
        this.idUsuarioReceptor = idUsuarioReceptor;
    }

    public Long getIdUsuarioPaciente() {
        return idUsuarioPaciente;
    }

    public void setIdUsuarioPaciente(Long idUsuarioPaciente) {
        this.idUsuarioPaciente = idUsuarioPaciente;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getNombreEmisor() {
        return nombreEmisor;
    }

    public void setNombreEmisor(String nombreEmisor) {
        this.nombreEmisor = nombreEmisor;
    }

    public String getNombreReceptor() {
        return nombreReceptor;
    }

    public void setNombreReceptor(String nombreReceptor) {
        this.nombreReceptor = nombreReceptor;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    @Override
    public String toString() {
        return "UsuariosCanalizados{" +
                "usuariosCanalizadosId=" + usuariosCanalizadosId +
                ", idUsuarioEmisor=" + idUsuarioEmisor +
                ", idUsuarioReceptor='" + idUsuarioReceptor + '\'' +
                ", idUsuarioPaciente='" + idUsuarioPaciente + '\'' +
                ", fechaInicial='" + fechaInicial + '\'' +
                ", fechaFinal=" + fechaFinal +
                ", nombrePaciente=" + nombrePaciente +
                ", nombreEmisor=" + nombreEmisor +
                ", nombreReceptor=" + nombreReceptor +
                '}';
    }
}
