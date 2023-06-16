package br.com.java.thread.async.helper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync // habilita o suporte para chamadas assíncronas
@Configuration
public class AsynConfig {

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3); // define o mínimo de threads a serem utilizadas
        executor.setMaxPoolSize(10); // define o máximo de threads a serem utilizadas
        executor.setQueueCapacity(90); // define a qtde máxima de tarefas que podem ser enfileiradas
        executor.initialize();
        return executor;
    }


}
