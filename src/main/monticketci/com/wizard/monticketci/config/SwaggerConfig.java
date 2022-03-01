/**
 * 
 */
package com.wizard.monticketci.config;


import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger.web.TagsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.*;
import static springfox.documentation.schema.AlternateTypeRules.*;


/**
 * @author augustesoro
 * @create  fev 22, 2021
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";
    
    @Autowired
    private TypeResolver typeResolver;
                              
	    /*@Bean
	    public Docket api() { 
	        return new Docket(DocumentationType.SWAGGER_2)  
	          .select()                                  
	          .apis(RequestHandlerSelectors.any())              
	          .paths(PathSelectors.any())                          
	          .build()
	          .apiInfo(apiInfo());
	          .securityContexts(Lists.newArrayList(securityContext()))
	           .securitySchemes(Lists.newArrayList(apiKey()));
	    
	}
	    
	    private ApiKey apiKey() {
	        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
	    }

	    private ApiInfo apiInfo() {
	        return new ApiInfo(
	          "API REST", 
	          "Description API.", 
	          "API MONTICKETCI", 
	          "Terms of service", 
	          new Contact("Auguste SORO", "www.augustesoro.com", "augustesoro@outlook.com"), 
	          "License of API", "API license URL", Collections.emptyList());
	    }
	    
	    private SecurityContext securityContext() {
	        return SecurityContext.builder()
	            .securityReferences(defaultAuth())
	            .forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
	            .build();
	    }

	    List<SecurityReference> defaultAuth() {
	        AuthorizationScope authorizationScope
	            = new AuthorizationScope("global", "accessEverything");
	        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
	        authorizationScopes[0] = authorizationScope;
	        return Lists.newArrayList(
	            new SecurityReference("JWT", authorizationScopes));
	    }*/
	    
	    
	    
	    // NEW
	    @Bean
	    public Docket petApi() {
	      return new Docket(DocumentationType.SWAGGER_2)
	          .select() 
	          .apis(RequestHandlerSelectors.any()) 
	          .paths(PathSelectors.any()) 
	          .build() 
	          .pathMapping("/") 
	          .directModelSubstitute(LocalDate.class, String.class) 
	          .genericModelSubstitutes(ResponseEntity.class)
	          .alternateTypeRules(
	              newRule(typeResolver.resolve(DeferredResult.class,
	                  typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
	                  typeResolver.resolve(WildcardType.class))) 
	          .useDefaultResponseMessages(false) 
	          //.securitySchemes(singletonList(apiKey())) 
	          //.securityContexts(singletonList(securityContext())) 

	           .securitySchemes(Lists.newArrayList(apiKey()))
		       .securityContexts(Lists.newArrayList(securityContext()))

	          .enableUrlTemplating(true)
	          .apiInfo(apiInfo());
	          /*.globalRequestParameters(
	              singletonList(new springfox.documentation.builders.RequestParameterBuilder()
	                  .name("someGlobalParameter")
	                  .description("Description of someGlobalParameter")
	                  .in(ParameterType.QUERY)
	                  .required(true)
	                  .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
	                  .build()));*/
	          //.tags(new Tag("Pet Service", "All apis relating to pets")); 
	    }
	    
	    private ApiInfo apiInfo() {
	        return new ApiInfo(
	          "Documentation API", 
	          "Liste des APIs de l'application.", 
	          "API MONTICKETCI", 
	          "Terms of service", 
	          new Contact("Auguste SORO", "www.augustesoro.com", "augustesoro@outlook.com"), 
	          "License of API", "API license URL", Collections.emptyList());
	    }

	    private ApiKey apiKey() {
	      return new ApiKey("JWT", AUTHORIZATION_HEADER, "header"); 
	    }

	    private SecurityContext securityContext() {
	      return SecurityContext.builder()
	          .securityReferences(defaultAuth())
	          .forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN)) 
	          .build();
	    }

	    
	    List<SecurityReference> defaultAuth() {
	        AuthorizationScope authorizationScope
	            = new AuthorizationScope("global", "accessEverything");
	        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
	        authorizationScopes[0] = authorizationScope;
	        return Lists.newArrayList(
	            new SecurityReference("JWT", authorizationScopes));
	    }

	    /*@Bean
	    SecurityConfiguration security() {
	      return SecurityConfigurationBuilder.builder() 
	          .clientId("test-app-client-id")
	          .clientSecret("test-app-client-secret")
	          .realm("test-app-realm")
	          .appName("test-app")
	          .scopeSeparator(",")
	          .additionalQueryStringParams(null)
	          .useBasicAuthenticationWithAccessCodeGrant(false)
	          .enableCsrfSupport(false)
	          .build();
	    }

	    @Bean
	    UiConfiguration uiConfig() {
	      return UiConfigurationBuilder.builder() 
	          .deepLinking(true)
	          .displayOperationId(false)
	          .defaultModelsExpandDepth(1)
	          .defaultModelExpandDepth(1)
	          .defaultModelRendering(ModelRendering.EXAMPLE)
	          .displayRequestDuration(false)
	          .docExpansion(DocExpansion.NONE)
	          .filter(false)
	          .maxDisplayedTags(null)
	          .operationsSorter(OperationsSorter.ALPHA)
	          .showExtensions(false)
	          .showCommonExtensions(false)
	          .tagsSorter(TagsSorter.ALPHA)
	          .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
	          .validatorUrl(null)
	          .build();
	    }*/
	    

	    
}
