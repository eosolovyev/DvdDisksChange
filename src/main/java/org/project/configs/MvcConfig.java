package org.project.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc //включает веб mvc
@Configuration// означает что это класс-конфиг
public class MvcConfig implements WebMvcConfigurer {


    //Сюда буду грузить файлы (картинки)
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //Добавляет какая страничка будет показываться при переходе на url /login
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //Где ресурсы будут хранится на сервере и в на компе
        registry.addResourceHandler("/img/**").addResourceLocations("file:///" + uploadPath + "/");
    }


}