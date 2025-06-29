package com.namnd.batch.writter;

import com.namnd.dto.SalaryRequest;
import com.namnd.model.SalaryBatchDetail;
import com.namnd.model.SalaryBatchStatus;
import com.namnd.repository.SalaryBatchDetailRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.ItemWriter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@Log4j2
public class SalaryBatchDetailWriter implements ItemWriter<SalaryBatchDetail> {

    private final SalaryBatchDetailRepository salaryBatchDetailRepository;

    private final RestTemplate restTemplate;

    public SalaryBatchDetailWriter(SalaryBatchDetailRepository salaryBatchDetailRepository, RestTemplate restTemplate) {
        this.salaryBatchDetailRepository = salaryBatchDetailRepository;
        this.restTemplate = restTemplate;
    }

    @Override
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void write(List<? extends SalaryBatchDetail> items) {
        for (SalaryBatchDetail detail : items) {

            if(!Arrays.asList(SalaryBatchStatus.PENDING, SalaryBatchStatus.PROCESSING).contains(detail.getStatus())){
                continue;
            }

            int retryCount = detail.getRetry() != null ? detail.getRetry() : 0;
            detail.setStatus(SalaryBatchStatus.PROCESSING);
            while (retryCount < 3) {
                try {
                    String url = "https://reqres.in/api/users";
                    SalaryRequest request = new SalaryRequest("hi", "22");

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.set("x-api-key", "reqres-free-v1");

                    HttpEntity<SalaryRequest> entity = new HttpEntity<>(request, headers);

                    ResponseEntity<Objects> response = restTemplate.postForEntity(url, entity, Objects.class);

                    if (response.getStatusCode().is2xxSuccessful()) {
                        detail.setStatus(SalaryBatchStatus.SUCCESS);
                        detail.setErrorMessage("");
                        break;
                    } else {
                        detail.setErrorMessage("Failed with status: " + response.getBody());
                        detail.setStatus(SalaryBatchStatus.ERROR);
                        break;
                    }
                } catch (ResourceAccessException ex) {
                    log.error("ResourceAccessException: " + ex.getMessage());
                    retryCount++;
                    detail.setRetry(retryCount);
                    detail.setErrorMessage("ResourceAccessException: " + ex.getMessage());
                    detail.setStatus(SalaryBatchStatus.ERROR);

                } catch (Exception e) {
                    log.error("Exception: " + e.getMessage());
                    detail.setErrorMessage("Exception: " + e.getMessage());
                    detail.setStatus(SalaryBatchStatus.ERROR);
                    break;
                }
            }

            salaryBatchDetailRepository.saveAndFlush(detail);
        }
    }
}
