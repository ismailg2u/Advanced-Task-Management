package dev.definex.finalproject.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserDetailsService userDetailsService,
                          JwtAuthenticationFilter jwtAuthenticationFilter,
                      CustomAuthenticationProvider customAuthenticationProvider, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/task/**").hasAnyRole("Project_Group_Manager","Team_Member", "Team_Leader", "Project_Manager","ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/task/**").hasAnyRole("Team_Leader", "Project_Manager","Project_Group_Manager","ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/task/**").hasAnyRole("Team_Leader", "Project_Manager","Team_Member","ADMIN","Project_Group_Manager")
                        .requestMatchers(HttpMethod.GET, "/api/project/**").hasAnyRole("Team_Leader", "Project_Manager","ADMIN","Project_Group_Manager")
                        .requestMatchers(HttpMethod.POST, "/api/project/**").hasAnyRole("Project_Group_Manager","ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/project/**").hasAnyRole("Project_Group_Manager","ADMIN","Project_Manager")
                        .requestMatchers(HttpMethod.GET, "/api/user/**").hasAnyRole("Project_Manager","ADMIN","Project_Group_Manager")
                        .requestMatchers(HttpMethod.POST, "/api/user/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/user/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/department/**").hasAnyRole("Project_Group_Manager","ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/department/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/department/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(customAuthenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }


}
