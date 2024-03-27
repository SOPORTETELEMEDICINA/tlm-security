package net.amentum.security.configuration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;


/**
 * @author dev06
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityConfiguration extends ResourceServerConfigurerAdapter {

    /**
     * Configuración de mappings
     * */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**").authorizeRequests().
                antMatchers("/oauth/token**","/oauth/authorize", "/users/recoverPassword**",
                        "/v2/api-docs**","/info","/sockets**", "users/findImageByUsername**",
                        "/new-user/link**", "/new-user/find**")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/users**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/users/rollback/**").permitAll()
        .anyRequest().authenticated().and().formLogin().loginPage("/login").failureForwardUrl("/login?error").permitAll().and().csrf().disable()
        .httpBasic().disable();
    }

}
