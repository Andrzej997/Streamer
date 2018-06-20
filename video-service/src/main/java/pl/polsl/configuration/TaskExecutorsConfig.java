package pl.polsl.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import pl.polsl.service.StoreTaskExecutor;
import pl.polsl.service.TranscodeVideoTaskExecutor;
import pl.polsl.service.impl.StoreTaskExecutorImpl;
import pl.polsl.service.impl.TranscodeVideoTaskExecutorImpl;

@Configuration
@EnableAsync
public class TaskExecutorsConfig {

    @Bean
    @Primary
    public TranscodeVideoTaskExecutor transcodeVideoTaskExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new TranscodeVideoTaskExecutorImpl();
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.setQueueCapacity(20);
        return (TranscodeVideoTaskExecutor) taskExecutor;
    }

    @Bean
    @Primary
    public StoreTaskExecutor storeTaskExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new StoreTaskExecutorImpl();
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.setQueueCapacity(5);
        return (StoreTaskExecutor) taskExecutor;
    }
}
