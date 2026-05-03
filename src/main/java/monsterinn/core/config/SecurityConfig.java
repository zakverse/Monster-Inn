package monsterinn.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                // Izinkan akses tanpa login untuk landing page dan assets
                .requestMatchers("/", "/css/**", "/js/**", "/images/**").permitAll() 
                // Sisanya (Dashboard, dll) WAJIB login
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login") // Route ke halaman login buatan kita
                .defaultSuccessUrl("/dashboard", true) // Jika sukses, lempar ke dashboard
                .permitAll()
            )
            .logout((logout) -> logout.permitAll());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Daftarkan kredensial staf Monster Inn di sini
        UserDetails user = User.builder()
            .username("resepsionis")
            .password(passwordEncoder().encode("monster123"))
            .roles("STAFF")
            .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}