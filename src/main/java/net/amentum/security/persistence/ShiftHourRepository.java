package net.amentum.security.persistence;

import net.amentum.security.exception.WorkShiftException;
import net.amentum.security.model.ShiftHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author  marellano
 */
@Repository
public interface ShiftHourRepository extends JpaRepository<ShiftHour, Long>, JpaSpecificationExecutor {


}
