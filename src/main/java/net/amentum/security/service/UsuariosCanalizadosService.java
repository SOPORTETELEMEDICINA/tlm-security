package net.amentum.security.service;

import net.amentum.security.exception.UsuariosCanalizadosException;
import net.amentum.security.model.UsuariosCanalizados;
import net.amentum.security.views.UsuariosCanalizadosView;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface UsuariosCanalizadosService {

    UsuariosCanalizadosView createUsuariosCanalizados(UsuariosCanalizadosView usuariosCanalizadosView) throws UsuariosCanalizadosException;

    void insertaUsuariosCanalizados(Date fechaInicial, Date fechaFinal, Long idEmisor, Long idReceptor, Long idPaciente) throws UsuariosCanalizadosException;

    Page<UsuariosCanalizadosView> findUsuariosCanalizados(Integer page,Integer size,String columnOrder,String orderType, Long idUsuarioEmisor) throws UsuariosCanalizadosException;

    void deleteUsuariosCanalizados(Long usuariosCanalizadosId) throws UsuariosCanalizadosException;

    List<Long>findListByUser(Long idUserApp) throws UsuariosCanalizadosException;

    List<Long> findByPaciente(Long idPaciente) throws UsuariosCanalizadosException;

}
