package com.dz.myfinance.configuration;

import com.dz.myfinance.repositories.UserRepository;
import com.dz.myfinance.services.CustomUserDetailsService;
import com.dz.myfinance.services.UserService;
import com.dz.myfinance.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(jwtUtil, customUserDetailsService);
    }

    @Autowired
    private UserRepository userRepository;

    /**
     * Конфигурирует фильтр безопасности для HTTP-запросов.
     *
     * @param http объект HttpSecurity для настройки безопасности
     * @return SecurityFilterChain экземпляр фильтра безопасности
     * @throws Exception если возникнет ошибка при конфигурации
     */


    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login**").permitAll()
                        .requestMatchers("/api/hello").permitAll() // Все запросы с /api требуют аутентификации
                        .anyRequest().authenticated()
                )

                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                .addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain mvcSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        // Разрешаем доступ к статическим ресурсам (стили, скрипты, утилиты и иконки) для пользователей и администраторов
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/style/**", "/js/**", "/util/**", "/icon/**").permitAll()//.hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/register**", "/login**").permitAll()
                        .requestMatchers("/administrator**").hasAnyRole( "EMPLOYEE","ADMIN")
                        // Требуем аутентификации для всех остальных запросов
                        .anyRequest().authenticated()
                )
                // Настройка формы входа
                .formLogin((form) -> form
                        .loginPage("/login") // URL страницы входа
                        .permitAll() // Позволяет доступ к этой странице всем пользователям
                        .defaultSuccessUrl("/", true) // URL по умолчанию после успешного входа
                )
                // Настройка выхода из системы
                .logout((logout) -> logout.permitAll())
                // Настройка CSRF-протокола для предотвращения атак типа Cross-Site Request Forgery

                .csrf(csrf -> csrf
                        .csrfTokenRepository(cookieCsrfTokenRepository())
                )
                // Обработчик исключений безопасности
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler((req, res, exc) -> {
                            // Отправляем ошибку 403 Forbidden при доступе запрещенных ресурсов
                            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden 123");
                        })
                )
                // Добавляем фильтр CORS перед фильтром сохранения контекста безопасности
                .addFilterBefore(corsFilter(), SecurityContextPersistenceFilter.class);


        return http.build();
    }


    /**
     * Создает и возвращает UserDetailsService для аутентификации пользователей.
     *
     * @return экземпляр UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService(userRepository, bCryptPasswordEncoder());
    }

    /**
     * Создает и возвращает DaoAuthenticationProvider для управления аутентификацией.
     *
     * @return экземпляр DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(bCryptPasswordEncoder()); // Используем bcrypt для шифрования паролей
        authProvider.setUserDetailsService(userDetailsService()); // Устанавливаем UserDetailsService для проверки учетных данных
        return authProvider;
    }

    /**
     * Создает и возвращает BCryptPasswordEncoder для шифрования паролей.
     *
     * @return экземпляр BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Создает и возвращает CsrfTokenRepository для управления CSRF-токенами.
     *
     * @return экземпляр CookieCsrfTokenRepository
     */
    @Bean
    public CsrfTokenRepository cookieCsrfTokenRepository() {
        CookieCsrfTokenRepository repository = new CookieCsrfTokenRepository();
        repository.setCookieName("_csrf"); // Имя куки для хранения CSRF-токена
        repository.setCookieHttpOnly(true); // Куки доступны только через HTTP-запросы
        return repository;
    }

    /**
     * Создает и возвращает CorsFilter для настройки CORS (Cross-Origin Resource Sharing).
     *
     * @return экземпляр CorsFilter
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // Разрешенные источники
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Разрешенные методы HTTP-запросов
        configuration.setAllowCredentials(true); // Позволяет отправлять учетные данные в запросах
        configuration.setMaxAge(3600L); // Время жизни куки CORS

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return new CorsFilter(source);
    }
}
