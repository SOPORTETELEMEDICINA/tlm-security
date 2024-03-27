package net.amentum.security.service.impl;

import net.amentum.common.GenericException;
import net.amentum.security.exception.*;
import net.amentum.security.model.*;
import net.amentum.security.persistence.UserAppRepository;
import net.amentum.security.persistence.UsuariosCanalizadosRepository;
import net.amentum.security.service.UsuariosCanalizadosService;
import net.amentum.security.views.GroupView;
import net.amentum.security.views.UserAppView;
import net.amentum.security.views.UserExtraInfoView;
import net.amentum.security.views.UsuariosCanalizadosView;
import org.apache.commons.codec.binary.Base64;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Ggarcia GGR20200626
 */

@Service
@Transactional(readOnly = true)
public class UsuariosCanalizadosServiceImpl implements UsuariosCanalizadosService {
    private final Logger logger = LoggerFactory.getLogger(UsuariosCanalizadosServiceImpl.class);

    private final Map<String,Object> colOrderNames = new HashMap<>();
    {
        colOrderNames.put("usuariosCanalizadosId","usuariosCanalizadosId");
    }

    private UsuariosCanalizadosRepository usuariosCanalizadosRepository;
    private UserAppRepository userAppRepository;

    @Autowired
    public void setUsuariosCanalizadosRepository(UsuariosCanalizadosRepository usuariosCanalizadosRepository) {
        this.usuariosCanalizadosRepository = usuariosCanalizadosRepository;
    }

    @Autowired
    public void setUserAppRepository(UserAppRepository userAppRepository) {
        this.userAppRepository = userAppRepository;
    }

    public EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    @Transactional(readOnly = false,rollbackFor = {UsuariosCanalizadosException.class})
    public UsuariosCanalizadosView createUsuariosCanalizados(UsuariosCanalizadosView usuariosCanalizadosView) throws UsuariosCanalizadosException {
        try {
            UsuariosCanalizados usuariosCanalizados = convertViewToEntity(usuariosCanalizadosView, null);
            logger.info(" - Insertar nuevo usuariosCanalizados: {}", usuariosCanalizados);
            Long idCanalizacion = usuariosCanalizadosRepository.existeCanalizacion(
                    usuariosCanalizados.getUsuarioEmisor().getUserAppId(), usuariosCanalizados.getUsuarioReceptor().getUserAppId(),
                    usuariosCanalizados.getUsuarioPaciente().getUserAppId());
            if (idCanalizacion != null) {
                throw new UsuariosCanalizadosException("Ya existe canalizacion",GroupException.LAYER_DAO, GenericException.ACTION_VALIDATE);
            }
            usuariosCanalizadosRepository.save(usuariosCanalizados);
            return convertEntityToView(usuariosCanalizados);
        } catch (DataIntegrityViolationException e){
            UsuariosCanalizadosException usuariosCanalizadosException = new UsuariosCanalizadosException("Error al insertar grupo, el nombre del grupo ya esta en la base de datos",GroupException.LAYER_DAO,GroupException.ACTION_INSERT);
            usuariosCanalizadosException.addError("El nombre: ya se encuentra en uso");
            logger.error(ExceptionServiceCode.USER+"- Error al insertar usuario_canalizado : {} - CODE: {}", usuariosCanalizadosView, usuariosCanalizadosException.getExceptionCode(),e);
            throw usuariosCanalizadosException;
        }catch (Exception ex){
            UsuariosCanalizadosException usuariosCanalizadosException = new UsuariosCanalizadosException(ex,"Error al insertar grupo", UsuariosCanalizadosException.LAYER_SERVICE,GroupException.ACTION_INSERT);
            logger.error(ExceptionServiceCode.GROUP+"- Error al insertar un usuario_canalizado: {} - CODE: {}",usuariosCanalizadosView, usuariosCanalizadosException.getExceptionCode(),ex);
            throw usuariosCanalizadosException;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {UsuariosCanalizadosException.class})
    @Override
    public void insertaUsuariosCanalizados(Date fechaInicial, Date fechaFinal, Long idEmisor, Long idReceptor, Long idPaciente) throws UsuariosCanalizadosException {
        try {
            Session session = (Session) entityManager.getDelegate();
            session.doWork(new Work() {
                @Override
                public void execute(Connection connectionToUse) throws SQLException {
                    boolean commitMode = connectionToUse.getAutoCommit();
                    try {
                        connectionToUse.setAutoCommit(false);
                        String queryIns = "insert into usuarios_canalizados (fecha_final, fecha_inicial, id_usuario_emisor, id_usuario_paciente, id_usuario_receptor) values (?, ?, ?, ?, ?)";
                        PreparedStatement pstmt = connectionToUse.prepareStatement(queryIns);
                        pstmt.setDate(1, new java.sql.Date(fechaFinal.getTime()));
                        pstmt.setDate(2, new java.sql.Date(fechaInicial.getTime()));
                        pstmt.setLong(3, idEmisor);
                        pstmt.setLong(4, idReceptor);
                        pstmt.setLong(5, idPaciente);
                        int resIns = pstmt.executeUpdate();
                        connectionToUse.commit();
                        logger.info("Se insertaron {} usuarios_canalizados", resIns);
                    } finally {
                        // Restore commit mode
                        connectionToUse.setAutoCommit(commitMode);
                    }
                }
            });

        } catch (Exception ex) {
            UsuariosCanalizadosException uce = new UsuariosCanalizadosException("Un error en insersion usuarios_canalizados", UsuariosCanalizadosException.LAYER_DAO, UsuariosCanalizadosException.ACTION_SELECT);
            uce.addError("Ocurrio un error in insertar usuarios canalizados");
            logger.error("===>>Erro al insertar usaurios canalizados - CODE: {} ", uce.getExceptionCode(), ex);
            throw uce;
        }
    }

    @Override
    public Page<UsuariosCanalizadosView> findUsuariosCanalizados(Integer page, Integer size, String columnOrder, String orderType, Long idUsuarioEmisor) throws UsuariosCanalizadosException {
        try {
            logger.info("===>>>findUsuariosCanalizados():  page {} - size {} - orderColumn {} - orderType {} - idUsuariosEmisor {}", page, size, columnOrder, orderType, idUsuarioEmisor);
            List<UsuariosCanalizadosView> usuariosView = new ArrayList<>();
            Page<UsuariosCanalizados> usuariosList = null;
            Sort sort = new Sort(Sort.Direction.ASC,(String)colOrderNames.get("usuariosCanalizadosId"));
            if(columnOrder!=null && orderType!=null){
                if(orderType.equalsIgnoreCase("asc"))
                    sort = new Sort(Sort.Direction.ASC,(String)colOrderNames.get(columnOrder));
                else
                    sort = new Sort(Sort.Direction.DESC,(String)colOrderNames.get(columnOrder));
            }
            PageRequest request = new PageRequest(page,size,sort);
            /*TODO: mejorar a que pueda hacerse...  usuarios_canalizados uc join user_app ap on uc.id_usuario_emisor = ap.id_usuer_app
               where ap.id_user_app = idUsuarioEmisor. No sé hacerlo con hiberante/jpa */
            List<Long> idsLong = new ArrayList<>();
            List<BigInteger> losIds = usuariosCanalizadosRepository.findIdByEmisor(idUsuarioEmisor);
            losIds.forEach(unId -> {
                idsLong.add(unId.longValue());
            });
            if(idsLong.isEmpty()) {
                idsLong.add(new Long(0));
            }

            //usuariosList = usuariosCanalizadosRepository.findAll(request);
            usuariosList = usuariosCanalizadosRepository.findAllById(idsLong, request);
            usuariosList.getContent().forEach(group -> {
                usuariosView.add(convertEntityToView(group));
            });
            PageImpl<UsuariosCanalizadosView> pageUsuariosCanalizados = new PageImpl<UsuariosCanalizadosView>(usuariosView, request, usuariosList.getTotalElements());
            return pageUsuariosCanalizados;
        } catch(Exception ex) {
            UsuariosCanalizadosException uce = new UsuariosCanalizadosException("Ocurrio un error al seleccionar lista de usuarios_canalizados", GenericException.LAYER_SERVICE,GenericException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.USER + "- Errror al tratar de seleccionar la lista de usuarios canalizados" ,uce.getExceptionCode(),ex);
            throw uce;
        }
    }

    @Override
    @Transactional(readOnly = false,rollbackFor = ExtraFieldException.class)
    public void deleteUsuariosCanalizados(Long usuariosCanalizadosId) throws UsuariosCanalizadosException {
        logger.info("===>>>deleteUsuariosCanalizados() borro ID: {}", usuariosCanalizadosId);
        try {
            if (!usuariosCanalizadosRepository.exists(usuariosCanalizadosId))
                throw new UsuariosCanalizadosException("No se encuentra campo a eliminar", GenericException.LAYER_SERVICE, GenericException.ACTION_VALIDATE);
            usuariosCanalizadosRepository.delete(usuariosCanalizadosId);

        } catch (UsuariosCanalizadosException uce) {
            throw uce;
        } catch (DataAccessException dae) {
            UsuariosCanalizadosException uce = new UsuariosCanalizadosException("Error al eliminar usuario_canalizado ",GenericException.LAYER_DAO,GenericException.ACTION_DELETE);
            logger.error(ExceptionServiceCode.USER+"- Error al eliminar campo extra  - {} - CODE: {} ", usuariosCanalizadosId, uce.getExceptionCode(), dae);
            throw uce;
        } catch (ConstraintViolationException e) {
            UsuariosCanalizadosException uce = new UsuariosCanalizadosException("Error al eliminar usuario_canalizado ",GenericException.LAYER_DAO,GenericException.ACTION_DELETE);
            logger.error(ExceptionServiceCode.USER+"- Error al eliminar campo extra  - {} - CODE: {} ", usuariosCanalizadosId, uce.getExceptionCode(), e);
            throw uce;
        } catch (Exception ex) {
            UsuariosCanalizadosException uce = new UsuariosCanalizadosException("Error al eliminar usuario_canalizado ",GenericException.LAYER_DAO,GenericException.ACTION_DELETE);
            logger.error(ExceptionServiceCode.USER+"- Error al eliminar campo extra  - {} - CODE: {} ", usuariosCanalizadosId, uce.getExceptionCode(), ex);
            throw uce;
        }
    }

    //Para buscar todos los pacientes que tiene el canalizados el receptor
    @Override
    public List<Long> findListByUser(Long idUserApp) throws UsuariosCanalizadosException {
        logger.info("===>>>findListByUser() con ID receptor: {}", idUserApp);
        try {
            List<Long> idsLong = new ArrayList<>();
            List<BigInteger> losIds = usuariosCanalizadosRepository.findListByReceptor(idUserApp);
            logger.info("===>>>findListByUser() ids del query valen: {}", losIds);
            losIds.forEach(unId -> {
                idsLong.add(unId.longValue());
            });
            logger.info("===>>>findListByUser() tengo canalizados: {}", idsLong);
            return idsLong;
        } catch (Exception ex) {
            UsuariosCanalizadosException uce = new UsuariosCanalizadosException("Ocurrio un error al pedir lista de usuarios_pacientes", UserAppException.LAYER_DAO, UserAppException.ACTION_SELECT);
            logger.error("Error al pedir lista de usuarios_pacientes - CODE: {} - username: {}", uce.getExceptionCode(), idUserApp, ex);
            throw uce;
        }
    }

    @Override
    public List<Long> findByPaciente(Long idPaciente) throws UsuariosCanalizadosException {
        logger.info("===>>>findByPaciente() con ID receptor: {}", idPaciente);
        try {
            List<Long> idsLong = new ArrayList<>();
            List<BigInteger> ids = usuariosCanalizadosRepository.findListByPaciente(idPaciente);
            logger.info(" id de la consulta {}", ids);
            ids.forEach(bigInteger -> idsLong.add(bigInteger.longValue()));
            logger.info(" id para retornar {}", idsLong);
            return idsLong;
        } catch (Exception ex) {
            UsuariosCanalizadosException uce = new UsuariosCanalizadosException("Ocurrio un error al verificar existencia de usuarios_pacientes", UserAppException.LAYER_DAO, UserAppException.ACTION_SELECT);
            logger.error("Error al pedir existencia de usuarios_pacientes - CODE: {} - username: {}", uce.getExceptionCode(), idPaciente, ex);
            throw uce;
        }
    }


    // Heplers
    private UsuariosCanalizadosView convertEntityToView(UsuariosCanalizados usuariosCanalizados) {
        UsuariosCanalizadosView view = new UsuariosCanalizadosView();
        view.setUsuariosCanalizadosId(usuariosCanalizados.getUsuariosCanalizadosId());
        view.setIdUsuarioEmisor(usuariosCanalizados.getUsuarioEmisor().getUserAppId());
        view.setIdUsuarioReceptor(usuariosCanalizados.getUsuarioReceptor().getUserAppId());
        view.setIdUsuarioPaciente(usuariosCanalizados.getUsuarioPaciente().getUserAppId());
        view.setFechaInicial(usuariosCanalizados.getFechaInicial());
        view.setFechaFinal(usuariosCanalizados.getFechaFinal());

        view.setNombrePaciente(usuariosCanalizados.getUsuarioPaciente().getName());
        view.setNombreEmisor(usuariosCanalizados.getUsuarioEmisor().getName());
        view.setNombreReceptor(usuariosCanalizados.getUsuarioReceptor().getName());

        return view;
    }


    private UsuariosCanalizados convertViewToEntity(UsuariosCanalizadosView usuariosCanalizadosView, UsuariosCanalizados usuariosCanalizados) {
        if (usuariosCanalizados == null) {
            usuariosCanalizados = new UsuariosCanalizados();
        }

        usuariosCanalizados.setFechaFinal(usuariosCanalizadosView.getFechaFinal());
        usuariosCanalizados.setFechaInicial(usuariosCanalizadosView.getFechaInicial());
        usuariosCanalizados.setUsuarioEmisor(userAppRepository.findOne(usuariosCanalizadosView.getIdUsuarioEmisor()));
        usuariosCanalizados.setUsuarioReceptor(userAppRepository.findOne(usuariosCanalizadosView.getIdUsuarioReceptor()));
        usuariosCanalizados.setUsuarioPaciente(userAppRepository.findOne(usuariosCanalizadosView.getIdUsuarioPaciente()));

        logger.info("return usuariosCanalizados: {}", usuariosCanalizados);
        return usuariosCanalizados;

    }



}
