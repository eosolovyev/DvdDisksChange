package org.project.configs;

import org.hibernate.SessionFactory;
import org.project.utils.PropertiesForConfigs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Driver;

@Configuration
@EnableTransactionManagement
@ComponentScan("org.project")
public class HibernateConfig {

    private final String driverClassName = "com.mysql.cj.jdbc.Driver";
    private final String url = "jdbc:mysql://localhost:3306/dvddiskproject?serverTimezone=UTC";
    private final String user = "root";
    private final String password = "";

    @Bean(name = "dataSource")
    public DataSource dataSource() {

        try {
            SimpleDriverDataSource source = new SimpleDriverDataSource();
            Class<? extends Driver> aClass = (Class<? extends Driver>) Class.forName(driverClassName);
            source.setDriverClass(aClass);
            source.setUrl(url);
            source.setUsername(user);
            source.setPassword(password);
            return source;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }


    @Bean(name = "sessionFactory")
    public SessionFactory sessionFactory() throws IOException {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        sessionFactoryBean.setPackagesToScan("org.project");
        sessionFactoryBean.setHibernateProperties(PropertiesForConfigs.hibernateProperties());
        sessionFactoryBean.afterPropertiesSet();
        return sessionFactoryBean.getObject();
    }


}