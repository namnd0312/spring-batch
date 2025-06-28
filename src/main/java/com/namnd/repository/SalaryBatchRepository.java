package com.namnd.repository;


import com.namnd.model.SalaryBatch;
import com.namnd.model.SalaryBatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SalaryBatchRepository extends JpaRepository<SalaryBatch, UUID> {

    List<SalaryBatch> findAllByStatus(SalaryBatchStatus status);
}
