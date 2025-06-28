package com.namnd.batch.processor;

import com.namnd.model.SalaryBatchDetail;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


@Component
@Log4j2
public class SalaryBatchDetailProcessor implements ItemProcessor<SalaryBatchDetail, SalaryBatchDetail> {


    @Override
    public SalaryBatchDetail process(SalaryBatchDetail detail) throws Exception {
        // Có thể thêm các bước kiểm tra hợp lệ nếu cần
        log.info(detail.toString());
        return detail;
    }
}
