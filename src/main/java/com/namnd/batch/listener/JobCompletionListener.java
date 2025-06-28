package com.namnd.batch.listener;

import com.namnd.model.SalaryBatchStatus;
import com.namnd.model.SalaryBatch;
import com.namnd.model.SalaryBatchDetail;
import com.namnd.repository.SalaryBatchDetailRepository;
import com.namnd.repository.SalaryBatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;
import org.springframework.batch.core.BatchStatus;

import java.util.List;

@Slf4j
@Component
public class JobCompletionListener implements JobExecutionListener {

    private final SalaryBatchRepository salaryBatchRepository;
    private final SalaryBatchDetailRepository salaryBatchDetailRepository;

    public JobCompletionListener(SalaryBatchRepository salaryBatchRepository, SalaryBatchDetailRepository salaryBatchDetailRepository) {
        this.salaryBatchRepository = salaryBatchRepository;
        this.salaryBatchDetailRepository = salaryBatchDetailRepository;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("Job completed with status: {}", jobExecution.getStatus());

         if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            // Lấy tất cả các lô đang xử lý (PENDING)
            List<SalaryBatch> pendingBatch = salaryBatchRepository.findAllByStatus(SalaryBatchStatus.PENDING);
            for (SalaryBatch batch : pendingBatch) {
                List<SalaryBatchDetail> details = salaryBatchDetailRepository.findAllByBatchBatchId(batch.getBatchId());
                boolean allSuccess = details.stream().allMatch(d -> d.getStatus() == SalaryBatchStatus.SUCCESS);
                boolean anyError = details.stream().anyMatch(d -> d.getStatus() == SalaryBatchStatus.ERROR);

                if (allSuccess) {
                    batch.setStatus(SalaryBatchStatus.SUCCESS);
                } else if (anyError) {
                    batch.setStatus(SalaryBatchStatus.ERROR);
                } else {
                    batch.setStatus(SalaryBatchStatus.PROCESSING);
                }
                salaryBatchRepository.save(batch);
            }
        }
    }
}


