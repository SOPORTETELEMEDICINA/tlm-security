package net.amentum;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2
// Sre10022020 Quito JMS:
//@EnableBinding(Processor.class)
public class SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

	@Value("${security.oauth2.client.client-id}")
	private String urlTokenId;
	@Value("${security.oauth2.client.client-secret}")
	private String secretToken;
	@Value("${security.token.endpoint}")
	private String endPoint;
	@Value("${security.oauth2.resource.tokenInfoUri}")
	private String urlTokenEndPoint;
	public static final String securitySchemaOAuth2 = "oauth2";
	public static final String authorizationScopeGlobal = "read write trust";
	public static final String authorizationScopeGlobalDesc = "accessEverything";

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_12)
				.apiInfo(getApiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("net.amentum.security"))
				.paths(PathSelectors.any())
				.build()
				.securitySchemes(Collections.singletonList(securitySchema()))
				.securityContexts(Collections.singletonList(securityContext()));
	}

	public ApiInfo getApiInfo() {
		return new ApiInfoBuilder()
				.title("Security Service API")
				.description("Servicios de seguridad: módulos, grupos,perfiles, usuarios, oauth")
				.contact("Amentum IT Services")
				.licenseUrl("http://www.amentum.net").build();
	}

	private OAuth securitySchema() {
		List<AuthorizationScope> authorizationScopeList = new ArrayList<>();
		authorizationScopeList.add(new AuthorizationScope("global", "access all"));
		List<GrantType> grantTypes = new ArrayList<>();
		TokenRequestEndpoint tokenRequestEndpoint = new TokenRequestEndpoint(endPoint, urlTokenId, secretToken);
		TokenEndpoint tokenEndpoint = new TokenEndpoint(urlTokenEndPoint, "access_token");
		AuthorizationCodeGrant authorizationCodeGrant = new AuthorizationCodeGrant(tokenRequestEndpoint, tokenEndpoint);
		grantTypes.add(authorizationCodeGrant);
		return new OAuth("oauth", authorizationScopeList, grantTypes);
	}

	private SecurityContext securityContext(){
		return SecurityContext.builder().securityReferences(defaultAuth())
				.forPaths(PathSelectors.any()).build();
	}

	private List<SecurityReference> defaultAuth(){
		final AuthorizationScope authorizationScope =
				new AuthorizationScope(authorizationScopeGlobal, authorizationScopeGlobalDesc);
		final AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Collections
				.singletonList(new SecurityReference(securitySchemaOAuth2, authorizationScopes));
	}



}
