package com.ufcg.psoft.scrumboard.config;

import com.ufcg.psoft.scrumboard.repository.TaskRepository;
import com.ufcg.psoft.scrumboard.repository.UserRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class AppConfig {
    
	@Bean
    public TaskRepository taskRepository() {
        return new TaskRepository();
    }
    @Bean
    public UserRepository userRepository(){ return  new UserRepository();}
}