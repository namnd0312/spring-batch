package com.namnd.batch.reader;

import com.namnd.model.SalaryBatchDetail;
import com.namnd.repository.SalaryBatchDetailRepository;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

@Configuration
@Component
public class SalaryBatchDetailReader {

    private final SalaryBatchDetailRepository salaryBatchDetailRepository;

    public SalaryBatchDetailReader(SalaryBatchDetailRepository salaryBatchDetailRepository) {
        this.salaryBatchDetailRepository = salaryBatchDetailRepository;
    }

    @Bean
    @StepScope
    public RepositoryItemReader<SalaryBatchDetail> reader(@Value("#{jobParameters['batchId']}") UUID batchId) {
        RepositoryItemReader<SalaryBatchDetail> reader = new RepositoryItemReader<>();
        reader.setRepository(salaryBatchDetailRepository);
        reader.setMethodName("findByBatch_BatchId");
        reader.setArguments(Collections.singletonList(batchId));
        reader.setPageSize(20);
        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        return reader;
    }
}
