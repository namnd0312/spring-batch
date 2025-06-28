package com.namnd.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "salary_batch_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalaryBatchDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String employeeName;

    private String employeeId;

    private String income;

    private Integer retry;

    @Column(name = "description")
    private String desc;


    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Enumerated(EnumType.STRING)
    private SalaryBatchStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastUpdated;

    private LocalDateTime processDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id") // tên cột FK ở bảng salary_batch_detail
    private SalaryBatch batch;

    @Override
    public String toString() {
        return "SalaryBatchDetail{" +
                "id=" + id +
                ", employeeName='" + employeeName + '\'' +
                ", status=" + status +
                '}';
    }
}
