package net.amentum.security.service;

import net.amentum.security.exception.OauthClientDetailsException;
import net.amentum.security.model.OauthClientDetails;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;

/**
 * @author dev06
 */
public interface OauthClientDetailService {

    /**
     * Método para agregar nueva autorización oauth a la base de datos
     * @param oauthClientDetails la nueva autorización que se va a guardar
     * @throws OauthClientDetailsException si ocurre alguna excepción al guardar el objeto en base de datos
     * */
    void addOauthClientDetail(OauthClientDetails oauthClientDetails) throws OauthClientDetailsException;

    /**
     * Método para editar detalles de autorizacion oatuh de cliente
     * @param clientDetails objeto a editar en base de datos
     * @throws OauthClientDetailsException
     * <br/> 1.- Si el objeto a editar no se encuentra en base de datos
     * <br/> 2.- Si ocurre algpun error al editar el objeto en base de datos
     * */
    void editOauthClientDetail(OauthClientDetails clientDetails) throws OauthClientDetailsException;

    /**
     * Método para obtener detalles de autorización
     * @param id ID único del cual se obtendrn los detalles
     * @return Objeto con la información requerida
     * @throws OauthClientDetailsException
     * <br/> 1.- Si el objeto a seleccionar no se encuentra en base de datos
     * <br/> 2.- Si ocurrre algún error al momento de hacer la selección en la base de datos
     * */
    OauthClientDetails finOauthClientDetails(Long id) throws OauthClientDetailsException;


    /**
     * Método para obtener lista de autorizaciones de froma paginada
     * @param name atributo para filtrar el resultado
     * @param page número de pagina a obtener en la base de datos iniciando en 0 ->
     * @param size número de resultados que se desean por pagina
     * @param columnOrder valor para ordenar el resultado por una columna especifica, opcional - default {id}
     * @param orderType valor para establecer el tipo de ordenamiento ('asc','desc'), opcional - default {'asc'}
     * @throws OauthClientDetailsException cuando no se puede obtener el resultado en la base de datos
     * */
    Page<OauthClientDetails> findGroups(@NotNull String name,@NotNull Integer page, @NotNull  Integer size, String columnOrder, String orderType) throws OauthClientDetailsException;

    /**
     * Método para eliminar una autorización en la base de datos
     * @param id ID único del objeto que se va a eliminar de la base de datos
     * @throws OauthClientDetailsException
     * <br/> 1.- Si no se encuentra la autorización en base de datos
     * <br/> 2.- Si ocurre algún errro al eliminar la autoptización
     * */
    void deleteClientDetail(@NotNull Long id) throws OauthClientDetailsException;
}
