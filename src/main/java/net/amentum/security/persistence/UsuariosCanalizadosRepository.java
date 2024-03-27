package net.amentum.security.persistence;

import com.sun.org.apache.xpath.internal.operations.Bool;
import net.amentum.security.model.UsuariosCanalizados;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Ggarcia GGR20200626
 */
@Repository
public interface UsuariosCanalizadosRepository extends JpaRepository<UsuariosCanalizados, Long> {

    @Query(value = "select id_usuarios_canalizados from usuarios_canalizados where id_usuario_emisor = :idUsuarioEmisor", nativeQuery = true)
    List<BigInteger> findIdByEmisor(@Param("idUsuarioEmisor") Long idUsuarioEmisor) throws Exception;

    @Query(value = "SELECT u FROM UsuariosCanalizados u WHERE u.usuariosCanalizadosId in (:idUsuariosCanalizados)")
    Page<UsuariosCanalizados> findAllById(@Param("idUsuariosCanalizados") List<Long> idUsuariosCanalizados, Pageable pageable) throws Exception;

    @Query(value="select distinct(id_usuarios_canalizados) from usuarios_canalizados where id_usuario_emisor = :idEmisor and id_usuario_receptor = :idReceptor and id_usuario_paciente = :idPaciente limit 1", nativeQuery = true)
    Long existeCanalizacion(@Param("idEmisor") Long idEmisor, @Param("idReceptor") Long idReceptor, @Param("idPaciente") Long idPaciente) throws Exception;

    @Query(value="select distinct(id_usuario_paciente) from usuarios_canalizados where id_usuario_receptor = :idUsuarioReceptor", nativeQuery = true)
    List<BigInteger> findListByReceptor(@Param("idUsuarioReceptor") Long idUsuarioReceptor) throws Exception;

    @Query(value="select distinct(id_usuario_paciente) from usuarios_canalizados " +
            "where id_usuario_paciente = :idPaciente", nativeQuery = true)
    List<BigInteger> findListByPaciente(@Param("idPaciente") Long idPaciente) throws Exception;

}
