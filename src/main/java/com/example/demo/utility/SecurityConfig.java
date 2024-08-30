package com.example.demo.utility;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, LicenseCheckFilter licenseCheckFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)  // Consider enabling CSRF in production
                .addFilterBefore(licenseCheckFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/materialTracking/register").permitAll()
                                .anyRequest().authenticated()
                ).formLogin(
                        form -> form
                                .loginPage("/login")  // Custom login page
                                .loginProcessingUrl("/login")  // URL for form submit
                                .defaultSuccessUrl("/homePage", true)  // Redirect on success
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .logoutSuccessUrl("/login?logout")  // Redirect on logout
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .permitAll()
                );
        return http.build();
    }

    @SuppressWarnings("deprecation")
    @Bean
    public NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

//    @Bean
//    public PasswordEncoder encoder() {
//        return new BCryptPasswordEncoder();
//    }
}
