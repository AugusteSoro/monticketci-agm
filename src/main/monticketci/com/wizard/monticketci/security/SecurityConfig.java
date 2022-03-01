package com.wizard.monticketci.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {

		/*
		 * web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui",
		 * "/swagger-resources", "/configuration/security", "/swagger-ui.html",
		 * "/webjars/**");
		 */

		web.ignoring().antMatchers("/", "/resources/**", "/static/**", "/configuration/**", "/swagger-ui/**",
				"/swagger-resources/**", "/api-docs", "/api-docs/**", "/v2/api-docs/**", "/*.html", "/**/*.html",
				"/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.gif", "/**/*.svg", "/**/*.ico", "/**/*.ttf",
				"/**/*.woff", "/**/*.otf", "/swagger-resources/**", "/webjars/**");
		
        

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// .antMatchers(
		// HttpMethod.POST,"/parameter/food/uploadfoodpicture").permitAll()

		http.cors().and().csrf().disable().authorizeRequests().antMatchers("/utilisateur/token").permitAll()
				.antMatchers(HttpMethod.POST, "/utilisateur/login").permitAll()
				.antMatchers(HttpMethod.POST, "/admin/login").permitAll()
				.antMatchers(HttpMethod.POST, "/caissier/login").permitAll()
				.antMatchers(HttpMethod.POST, "/superviseur/login").permitAll()
				.antMatchers(HttpMethod.POST, "/superviseur").permitAll()
				.antMatchers(HttpMethod.POST, "/utilisateur").permitAll()
				.antMatchers(HttpMethod.GET, "/localisation").permitAll()
				.antMatchers(HttpMethod.GET, "/event").permitAll()
				.antMatchers(HttpMethod.GET, "/event/limit3").permitAll()
				.antMatchers(HttpMethod.GET, "/event/downloadpicture/**").permitAll()
				.antMatchers(HttpMethod.GET, "/typeevent*").permitAll()
				.antMatchers(HttpMethod.GET, "/typeevent/downloadpicture/**").permitAll()
				.antMatchers("/ipn/payment").permitAll()
				.antMatchers(HttpMethod.POST, "/utilisateur/refreshtoken").permitAll()
				.antMatchers("/actuator").permitAll()
				.antMatchers("/actuator/**").permitAll()
				.antMatchers(HttpMethod.POST, "/email").permitAll()

				.antMatchers("/**").authenticated()

				

				.anyRequest().authenticated().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// http.addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class);
		http.addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

		/*
		 * http.cors().and().csrf().disable();
		 * http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.
		 * STATELESS); http.authorizeRequests().antMatchers(HttpMethod.OPTIONS,
		 * "/**").permitAll();
		 * http.authorizeRequests().antMatchers("/yolin/parameter/**").permitAll();
		 * http.authorizeRequests().antMatchers("/yolin/session/**").permitAll();
		 * http.authorizeRequests().anyRequest().authenticated();
		 * http.addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class);
		 * http.addFilterBefore(new JWTAuthorizationFilter(),
		 * UsernamePasswordAuthenticationFilter.class);
		 */

	}

	/*
	 * @Bean CorsConfigurationSource corsConfigurationSources() { CorsConfiguration
	 * configuration = new CorsConfiguration();
	 * configuration.setAllowedOrigins(Arrays.asList("*"));
	 * configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "GET"));
	 * UrlBasedCorsConfigurationSource source = new
	 * UrlBasedCorsConfigurationSource(); source.registerCorsConfiguration("/**",
	 * configuration); return source; }
	 */
	/*
	 * @Bean public CorsConfigurationSource corsConfigurationSource() { final
	 * CorsConfiguration configuration = new CorsConfiguration();
	 * configuration.setAllowedOrigins(ImmutableList.of("*"));
	 * configuration.setAllowedMethods(ImmutableList.of("HEAD", "GET", "POST",
	 * "PUT", "DELETE", "PATCH")); // setAllowCredentials(true) is important,
	 * otherwise: // The value of the 'Access-Control-Allow-Origin' header in the
	 * response must not be the wildcard '*' when the request's credentials mode is
	 * 'include'. configuration.setAllowCredentials(true); // setAllowedHeaders is
	 * important! Without it, OPTIONS preflight request // will fail with 403
	 * Invalid CORS request
	 * configuration.setAllowedHeaders(ImmutableList.of("Authorization",
	 * "Cache-Control", "Content-Type")); final UrlBasedCorsConfigurationSource
	 * source = new UrlBasedCorsConfigurationSource();
	 * source.registerCorsConfiguration("/**", configuration); return source; }
	 */

}
