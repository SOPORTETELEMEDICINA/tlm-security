package net.amentum.security.configuration;

import lombok.extern.slf4j.Slf4j;
import net.amentum.security.model.RowStatus;
import net.amentum.security.model.UserApp;
import net.amentum.security.persistence.UserAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service("userDetailsService")
@Slf4j
public class UsuarioService implements UserDetailsService {

    @Autowired
    UserAppRepository repository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserApp byUserName = repository.findByUsername(username);
            UserApp byUserEmail = repository.findByEmail(username);
            UserApp byUserPhone = repository.findByTelefono(username);
            if(byUserName == null && byUserEmail == null && byUserPhone == null)
                throw new UsernameNotFoundException(username);
            ArrayList<GrantedAuthority> roles = new ArrayList<>();
            if(byUserName != null && byUserName.getStatus().equals(RowStatus.ACTIVO)) {
                byUserName.getPermissionList().forEach(userHasPermission -> roles.add(new SimpleGrantedAuthority(userHasPermission.getUserHasPermissionId().getModulePermission().getCodeModulePermission())));
                return new User(byUserName.getUsername(), byUserName.getPassword(), roles);
            } else if(byUserEmail != null && byUserEmail.getStatus().equals(RowStatus.ACTIVO)) {
                byUserEmail.getPermissionList().forEach(userHasPermission -> roles.add(new SimpleGrantedAuthority(userHasPermission.getUserHasPermissionId().getModulePermission().getCodeModulePermission())));
                return new User(byUserEmail.getUsername(), byUserEmail.getPassword2(), roles);
            } else if(byUserPhone != null && byUserPhone.getStatus().equals(RowStatus.ACTIVO)) {
                byUserPhone.getPermissionList().forEach(userHasPermission -> roles.add(new SimpleGrantedAuthority(userHasPermission.getUserHasPermissionId().getModulePermission().getCodeModulePermission())));
                return new User(byUserPhone.getUsername(), byUserPhone.getPassword3(), roles);
            } else
                throw new UsernameNotFoundException(username);
        } catch (Exception e) {
            log.error(e.toString());
            throw new UsernameNotFoundException(username);
        }
    }

}
