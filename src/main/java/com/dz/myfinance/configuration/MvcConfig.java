package com.dz.myfinance.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Конфигурация для настройки маршрутизации в Spring MVC приложении.
 * Этот класс реализует интерфейс WebMvcConfigurer, что позволяет нам настраивать различные аспекты веб-представлений.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * В данном случае мы добавляем только один контроллер для страницы входа (/login).
     *
     * @param registry объект ViewControllerRegistry для управления маршрутами
     */
    public void addViewControllers(ViewControllerRegistry registry) {
        // Добавляем контроллер для страницы входа
        registry.addViewController("/login").setViewName("login");

        // Мы создаем отдельный контроллер для страницы входа, чтобы иметь полный контроль над ее отображением и поведением.
        // Это особенно полезно, если вам нужно добавить дополнительную логику или стили к странице входа.
    }
}