package net.amentum.security.views;

import java.io.Serializable;
import java.util.Date;

public class UsuariosCanalizadosPageView implements Serializable {

    private Long idUsuarioEmisor;
    private Long idUsuarioReceptor;
    private Long idUsuarioPaciente;
    private Date fechaInicial;
    private Date fechaFinal;
    private Long usuariosCanalizadosId;

    private String nombrePaciente;

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public Long getUsuariosCanalizadosId() {
        return usuariosCanalizadosId;
    }

    public void setUsuariosCanalizadosId(Long usuariosCanalizadosId) {
        this.usuariosCanalizadosId = usuariosCanalizadosId;
    }

    public UsuariosCanalizadosPageView() {

    }

    public UsuariosCanalizadosPageView(Long usuariosCanalizadosId, Long idUsuarioEmisor, Long idUsuarioReceptor, Long idUsuarioPaciente, Date fechaInicial, Date fechaFinal) {
        this.usuariosCanalizadosId = usuariosCanalizadosId;
        this.idUsuarioEmisor = idUsuarioEmisor;
        this.idUsuarioReceptor = idUsuarioReceptor;
        this.idUsuarioPaciente = idUsuarioPaciente;
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
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

    @Override
    public String toString() {
        return "UserAppView{" +
                "usuariosCanalizadosId=" + usuariosCanalizadosId +
                "idUsuarioEmisor=" + idUsuarioEmisor +
                ", idUsuarioReceptor='" + idUsuarioReceptor + '\'' +
                ", idUsuarioPaciente='" + idUsuarioPaciente + '\'' +
                ", fechaInicial='" + fechaInicial + '\'' +
                ", fechaFinal=" + fechaFinal +
                '}';
    }
}
