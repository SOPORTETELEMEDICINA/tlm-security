package net.amentum.security.service;

import net.amentum.security.exception.NewUserException;
import net.amentum.security.model.NewUser;

public interface NewUsersService {

    String createUserLink(String username, String idMedico, Long idGroup) throws NewUserException;

    Boolean verifyLink(String hash) throws NewUserException;

    NewUser findByHash(String hash) throws NewUserException;

}
