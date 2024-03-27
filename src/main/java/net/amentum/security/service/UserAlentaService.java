package net.amentum.security.service;

import net.amentum.security.exception.UserAlentaException;
import net.amentum.security.views.UserAlentaView;

public interface UserAlentaService {

    void insertUserAlenta(UserAlentaView view) throws UserAlentaException;

    UserAlentaView getUserByIdUser(String idUser) throws UserAlentaException;

    UserAlentaView getUserByIdAlenta(String idUserAlenta) throws UserAlentaException;
}
