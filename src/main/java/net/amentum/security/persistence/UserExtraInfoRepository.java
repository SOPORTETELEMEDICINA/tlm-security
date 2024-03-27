package net.amentum.security.persistence;

import net.amentum.security.model.RowStatus;
import net.amentum.security.model.UserExtraInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author marellano on 26/04/17.
 */
@Repository
public interface UserExtraInfoRepository extends JpaRepository<UserExtraInfo, Long>{

    /**
     * Método para modificar el status y la fecha de acuerdo al id del usuario.
     * @param idUserExtraInfo identificador único del cliente.
     * @param status estatus nuevo del cliente.
     * @param date fecha en timestamp en que se modifico el cliente.
     * */
    @Query("UPDATE UserExtraInfo u SET u.status =:status, u.modifiedDate =:date where u.userExtraInfoId =:idUserExtraInfo")
    @Modifying
    public void updateStatus(@Param("status") RowStatus status, @Param("idUserExtraInfo") Long idUserExtraInfo,
                             @Param("date") Date date) throws Exception;
}
