package net.amentum.security.configuration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TokenEndpointRetryInterceptor {

	@Around("execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.*(..))")
	public Object execute (ProceedingJoinPoint aJoinPoint) throws Throwable {
		try {
			return aJoinPoint.proceed();
		} catch (DuplicateKeyException e) {
			throw new IllegalStateException("1504CNTREQ");
		}
	}

}
