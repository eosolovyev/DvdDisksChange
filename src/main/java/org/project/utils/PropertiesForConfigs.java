package org.project.utils;

import java.util.Properties;

/**
 * При первом запуске использовать в hibernate.hbm2ddl.auto значение create, чтобы создалось все в БД, в дальнейшем меняять на validate, чтобы не сохранялось еще раз
 */
public class PropertiesForConfigs {
    public static Properties hibernateProperties() {
        Properties hibernateProp = new Properties();
        //Устанавливает диалект, в данном случае MySql
        hibernateProp.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        //При запуске проекта что делает, в данном случае скрипты запускает
        hibernateProp.put("hibernate.hbm2ddl.auto", "create");//create при перво запуске, остальные validate
        //В данном случае весь sql будет показан в логах
        hibernateProp.put("hibernate.show_sql", true);
        return hibernateProp;
    }
}