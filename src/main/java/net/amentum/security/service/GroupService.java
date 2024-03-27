package net.amentum.security.service;

import net.amentum.security.exception.GroupException;
import net.amentum.security.model.Group;
import net.amentum.security.views.GroupView;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Victor de la Cruz
 * Interface para administrar grupos, permite: crear, editar, obtener y eliminar grupos
 */
public interface GroupService {

    /**
     * Método para crear nuevo grupo
     * @param group nuevo grupo a guardar en base de datos
     * @throws GroupException <br/> 1.- Si ya existe un nuevo grupo con el mismo nombre <br/> 2..- si no es posible guardar el grupo en la base de datos
     * */
    void addGroup(GroupView group) throws GroupException;

    /**
     * Método para editar un grupo existente en base de datos
     * @param group el grupo a ediatr en base de datos
     * @throws GroupException <br/> 1.- Si no es posible editar el grupo <br/> 2.- Si se trata de ditar un grupo que no existe en base de datos
     * */
    void editGroup(GroupView group) throws GroupException;

    /**
     * Método para eliminar un grupo en la base de datos
     * @param groupId el ID único del grupo en base de datos
     * @throws GroupException <br/> 1.- Si el grupo está siendo utilizado por algún usuario <br/> 2.- Si el grupo que se intenta eliminar no se encuentra en base de datos
     * */
    void deleteGroup(Long groupId) throws GroupException;

    /**
     * Método para obtener los detalles de un grupo
     * @param groupId  el ID único del grupo
     * @return el Grupo obtenido en base de datos
     * @throws  GroupException <br/> 1.- Si no es posible realizar la selección del grupo en la base de datos
     * */
    GroupView findGroup(Long groupId) throws  GroupException;

    /**
     * Método para obtener una lista de  Grupos
     * @param active attributo para filtrar los grupos, si es nulo se obtienen todos los grupos
     * @return Lista de grupos seleccionados en base de datos, de forma paginada
     * @throws GroupException <br/> 1.- Si ocurre algún error en la seleccion de los grupos
     * */
    Page<GroupView> findGroups(Boolean active,String name,Integer page,Integer size,String columnOrder,String orderType) throws GroupException;

    /**
     *Método para obtener la lista completa de grupos que existen en la base de datos
     * @param active parametro opcional para obtener grupos activos o inactivos
     * @throws GroupException si ocurre algun error al obtener la lista de grupos
     * */
    List<GroupView> findAll(Boolean active) throws GroupException;

    /*
    // GGR20200610 Obtener imagen del grupo principal (puede mejorarse)
    String findImageGroup (@NotNull Long idUserApp) throws GroupException;

    // GGR20200612 Obtener nombre del grupo principal (puede mejorarse)
    String findNameMainGroup(@NotNull Long idUserApp) throws GroupException;

    // GGR20200612 Obtener id del grupo principal (puede mejorarse)
    Long findIdMainGroup(@NotNull Long idUserApp) throws GroupException;
     */

}
