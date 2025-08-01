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
    private  Job salaryJob;

    @GetMapping("/run/{id}")
    void launchBatch(@PathVariable("id") String id) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
//        String batchId = "f0be5aaf-fcb6-42d2-aa81-04e429f5ebca";
        JobParameters params = new JobParametersBuilder()
//				.addLong("timestamp", System.currentTimeMillis()) // để tránh bị trùng job instance
                .addString("batchId", id)
                .toJobParameters();

        jobLauncher.run(salaryJob, params);


    }
}
