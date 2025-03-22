package com.namnd.controller;


import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/batch")
public class BatchController {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("firstJob")
    private Job job;

    @GetMapping("/run/{id}")
    void launchBatch(@PathVariable("id") String id) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("transactionId", id) // Thêm tham số duy nhất
                .toJobParameters();

        JobExecution jobExecution = this.jobLauncher.run(job, jobParameters);
        System.out.println(jobExecution.getStatus());

    }
}
