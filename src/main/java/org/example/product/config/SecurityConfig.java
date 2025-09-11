import org.example.product.security.JwtAuthFilter;
import org.example.product.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

import static org.example.product.util.ProjectConstants.*;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserServiceImpl userServiceImpl;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserServiceImpl userServiceImpl) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userServiceImpl = userServiceImpl;
    }

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register").permitAll()
                        .requestMatchers("/categories", "/products").hasAnyRole(USER, ADMIN)
                        .requestMatchers(HttpMethod.POST, APICATEGORY).hasRole(ADMIN)
                        .requestMatchers(HttpMethod.PUT, APICATEGORY).hasRole(ADMIN)
                        .requestMatchers(HttpMethod.DELETE, APICATEGORY).hasRole(ADMIN)
                        .requestMatchers(HttpMethod.POST, APIPRODUCT).hasRole(ADMIN)
                        .requestMatchers(HttpMethod.PUT, APIPRODUCT).hasRole(ADMIN)
                        .requestMatchers(HttpMethod.DELETE, APIPRODUCT).hasRole(ADMIN)
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userServiceImpl);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}