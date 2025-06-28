package com.namnd.repository;

import com.namnd.model.SalaryBatchDetail;
import com.namnd.model.SalaryBatchStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SalaryBatchDetailRepository extends JpaRepository<SalaryBatchDetail, UUID> {

    List<SalaryBatchDetail> findAllByBatchBatchId(UUID batchId);
    Page<SalaryBatchDetail> findByStatus(SalaryBatchStatus status, Pageable pageable);
    Page<SalaryBatchDetail> findByStatusAndBatch_BatchId(SalaryBatchStatus status, UUID batchId, Pageable pageable);
    Page<SalaryBatchDetail> findByBatch_BatchId(UUID batchId, Pageable pageable);
}
