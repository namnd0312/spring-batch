package com.namnd.config;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step firstStep() {
        return this.stepBuilderFactory.get("step_1").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                System.out.println("step 1 runn");

                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Step secondStep() {
        return this.stepBuilderFactory.get("step_2").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                System.out.println("step 2 runn");

                boolean isOK = false;

                if(isOK){
                    throw new RuntimeException();
                }

                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Step thirdStep() {
        return this.stepBuilderFactory.get("step_3").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                System.out.println("step 3 runn");

                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Job firstJob() {
        return this.jobBuilderFactory.get("first_job")
//                .preventRestart() //ngăn chặn restart thực hiện lại job
                .start(this.firstStep())
                .next(secondStep())
                .next(thirdStep())
                .build();
    }


}
