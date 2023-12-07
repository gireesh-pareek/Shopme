package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public UserDetailsService userDetailsService(){
        return new ShopmeUserDetailsService();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return  authenticationProvider;
    }
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/users/**", "/settings/**").hasAuthority("Admin")
                        .requestMatchers("/categories/**", "/brands/**", "/articles/**", "/menus/**").hasAnyAuthority("Admin", "Editor")
                        .requestMatchers("/products/**").hasAnyAuthority("Admin", "Salesperson", "Editor", "Shipper")
                        .requestMatchers("/questions/**", "/reviews/**").hasAnyAuthority("Admin", "Assistant")
                        .requestMatchers("/customers/**", "/shipping/**", "/report/**").hasAnyAuthority("Admin", "Salesperson")
                        .requestMatchers("/orders/**").hasAnyAuthority("Admin", "Salesperson", "Shipper")
                        .requestMatchers("/images/**", "/js/**", "/webjars/**").permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .usernameParameter("email")
                        .permitAll()
                )
                .logout(logout -> logout
                        .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe
                        .key("asjdfgasdhfkhdckwhedfoi_152E432136")
                        .tokenValiditySeconds(7*24*60*60)
                        .userDetailsService(userDetailsService())
                );

        return http.build();
    }
}
