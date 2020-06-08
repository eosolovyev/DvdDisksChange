package org.project.configs;

import org.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration //Класс-конфиг
@EnableWebSecurity //Включает секбюрити
@EnableGlobalMethodSecurity(prePostEnabled = true)//Тоже включает секьюрити
@ComponentScan(basePackages = "org.project")
//Сканирует компоненты(чтобы найти UserService, т.к. он был помечен как @Service, т.е. является бином в спринге))
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired//Автотически спринг сам внедряет бин
    private UserService userService;


    @Autowired //Парольщик внедряется спрингом, его реализаця показана чуть ниже
    private PasswordEncoder passwordEncoder;

    //Бин PasswordEncoder
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new MessageDigestPasswordEncoder("MD5");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/registration").permitAll() //Разрешает всем переходить на данные url
                .anyRequest().authenticated() //Другим требует аутентификация
                .and()
                .formLogin()
                .loginPage("/login") //Ставит какой юрл отвечает за логин
                .permitAll() //Разрешает его всем
                .and()
                .logout() //И разрешает всем логаут
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //В конфигурацию добавляет UserDetailsService, чтобы спринг знал, откуда ему искать пользователей,
        //в нашем случае UserService является UserDetailsService, т.к. имплементирует его
        //и ставит кодировщика паролей
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }
}