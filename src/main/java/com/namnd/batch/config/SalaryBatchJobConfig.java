package com.namnd.batch.config;


import com.namnd.batch.listener.JobCompletionListener;
import com.namnd.batch.processor.SalaryBatchDetailProcessor;
import com.namnd.batch.reader.SalaryBatchDetailReader;
import com.namnd.batch.writter.SalaryBatchDetailWriter;
import com.namnd.model.SalaryBatchDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class SalaryBatchJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SalaryBatchDetailProcessor processor;
    private final SalaryBatchDetailWriter writer;
    private final SalaryBatchDetailReader readerConfig;
    private final JobCompletionListener listener;

    @Bean
    public Job salaryJob() {
        return jobBuilderFactory.get("salaryJob")
                .start(processSalaryStep())
                .listener(listener)
                .build();
    }
//
//    @Bean
//    public Step processSalaryStep() {
//        return stepBuilderFactory.get("processSalaryStep")
//                .<SalaryBatchDetail, SalaryBatchDetail>chunk(100)
//                .reader(readerConfig.reader(null))
//                .processor(processor)
//                .writer(writer)
//                .build();
//    }


    @Bean
    public Step processSalaryStep() {
        return stepBuilderFactory.get("processSalaryStep")
                .<SalaryBatchDetail, SalaryBatchDetail>chunk(20)
                .reader(readerConfig.reader(null))
                .processor(processor)
                .writer(writer)
                .taskExecutor(taskExecutor())
//                .throttleLimit(30) // số thread tối đa
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("batch-thread-");
        executor.setCorePoolSize(20); // số lượng core thread luôn tồn tại
        executor.setMaxPoolSize(20);  // tối đa số thread đồng thời
        executor.setQueueCapacity(1000); // queue đợi
        executor.initialize();
        return executor;
    }
}
