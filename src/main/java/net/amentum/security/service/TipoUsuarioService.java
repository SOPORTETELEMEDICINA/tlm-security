package net.amentum.security.service;

import net.amentum.security.exception.TipoUsuarioException;
import net.amentum.security.views.TipoUsuarioView;

import java.util.List;

public interface TipoUsuarioService {

   List<TipoUsuarioView> findAll(Boolean visible) throws TipoUsuarioException;

}
