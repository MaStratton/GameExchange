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
                //User Endpoints
                .requestMatchers(HttpMethod.POST, "/User/Register").permitAll()
                .requestMatchers(HttpMethod.PUT, "/User").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/User").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/User/ChangePass").authenticated()

                //Game Endpoints
                //Records
                .requestMatchers(HttpMethod.GET, "/Game/Records").authenticated()
                .requestMatchers(HttpMethod.POST, "/Game/Records").authenticated()

                .requestMatchers(HttpMethod.GET, "/Game/Records/{id}").authenticated()
                .requestMatchers(HttpMethod.PUT, "/Game/Records/{id}").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/Game/Records/{id}").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/Game/Records/{id}").authenticated()

                //Games
                .requestMatchers(HttpMethod.GET, "/Game/Games").authenticated()
                .requestMatchers(HttpMethod.GET, "/Game/Games/{id}").authenticated()
                .requestMatchers(HttpMethod.POST, "/Game/Games").authenticated()

                //Publishers
                .requestMatchers(HttpMethod.GET, "/Game/Publishers").authenticated()
                .requestMatchers(HttpMethod.GET, "/Game/Publisher/{id}").authenticated()
                .requestMatchers(HttpMethod.POST, "/Game/Publisher").authenticated()
                
                //Systems
                .requestMatchers(HttpMethod.GET, "/Game/Systems").authenticated()
                .requestMatchers(HttpMethod.GET, "Game/System/{id}").authenticated()    
                .requestMatchers(HttpMethod.POST, "/Game/System").authenticated()

                //Conditions
                .requestMatchers(HttpMethod.GET, "/Game/Conditions").authenticated()
                .requestMatchers(HttpMethod.GET, "/Game/Condition/{id}").authenticated()
                

                //Offer Endpoints
                .requestMatchers(HttpMethod.POST, "/Offer").authenticated()
                .requestMatchers(HttpMethod.GET, "/Offer").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/Offer/{id}/{decision}").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/Offer/{id").authenticated()

                //Others
                .requestMatchers("/error*").permitAll()
                .anyRequest().permitAll()).httpBasic(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable);


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