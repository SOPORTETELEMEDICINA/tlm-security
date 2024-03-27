package net.amentum.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "usuarios_canalizados")
public class UsuariosCanalizados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_usuarios_canalizados")
    private Long usuariosCanalizadosId;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_usuario_emisor", nullable = false)
    @JsonIgnore
    private UserApp usuarioEmisor;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_usuario_receptor", nullable = false)
    @JsonIgnore
    private UserApp usuarioReceptor;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_usuario_paciente", nullable = false)
    @JsonIgnore
    private UserApp usuarioPaciente;

    private Date fechaInicial = new Date();

    private Date fechaFinal = new Date();

    public Long getUsuariosCanalizadosId() {
        return usuariosCanalizadosId;
    }

    public void setUsuariosCanalizadosId(Long usuariosCanalizadosId) {
        this.usuariosCanalizadosId = usuariosCanalizadosId;
    }

    public UserApp getUsuarioEmisor() {
        return usuarioEmisor;
    }

    public void setUsuarioEmisor(UserApp usuarioEmisor) {
        this.usuarioEmisor = usuarioEmisor;
    }

    public UserApp getUsuarioReceptor() {
        return usuarioReceptor;
    }

    public void setUsuarioReceptor(UserApp usuarioReceptor) {
        this.usuarioReceptor = usuarioReceptor;
    }

    public UserApp getUsuarioPaciente() {
        return usuarioPaciente;
    }

    public void setUsuarioPaciente(UserApp usuarioPaciente) {
        this.usuarioPaciente = usuarioPaciente;
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
        return "UsuariosCanalizados{" +
                "usuariosCanalizadosId=" + usuariosCanalizadosId +
                ", idUsuarioEmisor=" + usuarioEmisor.getUserAppId() +
                ", idUsuarioReceptor='" + usuarioReceptor.getUserAppId() + '\'' +
                ", idUsuarioPaciente='" + usuarioPaciente.getUserAppId() + '\'' +
                ", fechaInicial='" + fechaInicial + '\'' +
                ", fechaFinal=" + fechaFinal +
                '}';
    }
}
