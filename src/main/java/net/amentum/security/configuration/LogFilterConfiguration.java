package net.amentum.security.configuration;

import net.amentum.common.RequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dev06 
 */
@Configuration
public class LogFilterConfiguration {
    @Bean
    public FilterRegistrationBean getFilter(){
        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setFilter(new RequestFilter());
        filter.addUrlPatterns("/*");
        filter.setName("requestFilter");
        return filter;
    }

}
