package net.amentum.security.service;

import net.amentum.security.exception.HierarchyException;
import net.amentum.security.views.HierarchyRequestView;
import net.amentum.security.views.ProfileHierarchyView;

import java.util.List;

public interface HierarchyService {

    /**
     * Método para agregar jerarquía
     * @param hierarchyView view para agregar la jerarquia son requeridos
     * @throws HierarchyException
     * <br/>.1.- Si ocurre algún error  agregar jerarquia en la base de datos.
     * **/
    void addOrUpdateHierarchy(HierarchyRequestView hierarchyView) throws HierarchyException;

    /**
     * Método para obtener jerarquía
     * @param idProfileBoss obtener los perfiles asignados al jefe
     * @throws HierarchyException
     * <br/>.1.- Si ocurre algún error al obtener jerarquia en la base de datos.
     * **/
    List<ProfileHierarchyView> getHierarchy(Long idProfileBoss) throws HierarchyException;
}
