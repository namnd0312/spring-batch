package com.namnd;

import com.namnd.model.SalaryBatch;
import com.namnd.model.SalaryBatchDetail;
import com.namnd.model.SalaryBatchStatus;
import com.namnd.repository.SalaryBatchDetailRepository;
import com.namnd.repository.SalaryBatchRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class SpringBatchApplication implements CommandLineRunner {

	@Autowired
	private SalaryBatchDetailRepository salaryBatchDetailRepository;

	@Autowired
	private SalaryBatchRepository salaryBatchRepository;


	@Override
	public void run(String... args) {

		SalaryBatch salaryBatch = new SalaryBatch();
		salaryBatch.setStatus(SalaryBatchStatus.PENDING);
		SalaryBatch salaryBatchEntity = salaryBatchRepository.save(salaryBatch);

		for (int i = 0;  i <5000; i++) {
			SalaryBatchDetail salaryBatchDetail = new SalaryBatchDetail();
			salaryBatchDetail.setBatch(salaryBatchEntity);
			salaryBatchDetail.setEmployeeId(String.valueOf(Math.random()));
			salaryBatchDetail.setEmployeeName("Nghiem duc nam"+ UUID.randomUUID());
			salaryBatchDetail.setIncome("1200000");
			salaryBatchDetail.setStatus(SalaryBatchStatus.PENDING);
			salaryBatchDetailRepository.save(salaryBatchDetail);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchApplication.class, args);
	}

}
