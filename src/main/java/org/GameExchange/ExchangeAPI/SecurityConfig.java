package org.GameExchange.ExchangeAPI;


import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig {
    

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.addFilterBefore(new AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        httpSecurity.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.GET, "/*").permitAll()
                .requestMatchers(HttpMethod.POST, "/*").permitAll()
                .requestMatchers(HttpMethod.PUT, "*/*").permitAll()
                .requestMatchers(HttpMethod.PUT, "/Game").authenticated()
                .requestMatchers(HttpMethod.GET, "/Game").authenticated()
                .requestMatchers(HttpMethod.POST, "/User/Register").permitAll()
                .requestMatchers(HttpMethod.POST, "/Game").authenticated()
                .requestMatchers(HttpMethod.PUT, "/Game/{Id}").authenticated()
                //.requestMatchers("/error/**").permitAll()

                .anyRequest().authenticated()).httpBasic(Customizer.withDefaults());
                
        httpSecurity.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("all"));
            config.setAllowedOriginPatterns(List.of("*"));
            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
            config.setAllowedHeaders(List.of("*"));
            config.setExposedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
            config.setAllowCredentials(true); // if using auth headers, set to true
            return config;
        }));

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}