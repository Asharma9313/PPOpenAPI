package com.pulsepoint.hcp365.trigger;
import com.pulsepoint.commons.security.CustomTokenAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /*@Value("${security.oauth2.resource.token-info-uri}")
    String introspectionUri;

    @Value("${security.oauth2.client.client-id}")
    String clientId;

    @Value("${security.oauth2.client.client-secret}")
    String clientSecret;*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new CustomTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .antMatcher("/trigger**").authorizeRequests()
                .antMatchers("/trigger**").authenticated()
                .and().csrf().disable();
        http.csrf().and().authorizeRequests().anyRequest().authenticated();

        /*http
                .authorizeRequests(authz -> authz
                        .antMatchers(HttpMethod.GET, "/reporting**").authenticated()
                        .antMatchers(HttpMethod.POST, "/reporting**").authenticated()
                        .antMatchers(HttpMethod.PUT, "/reporting**").authenticated()
                        .antMatchers(HttpMethod.OPTIONS, "/reporting**").authenticated()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .opaqueToken(token -> token.introspectionUri(this.introspectionUri)
                                .introspectionClientCredentials(this.clientId, this.clientSecret)));*/
    }

    /*@Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.applyPermitDefaultValues();
        config.addAllowedMethod("GET, POST, PUT, DELETE, OPTIONS, PATCH");
        config.addAllowedHeader("access-control-allow-origin");
        source.registerCorsConfiguration("/**", config);
        return source;
    }*/
    @Bean
    public FilterRegistrationBean<CorsFilter> simpleCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
    /*@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/actuator/**");
    }*/

}
