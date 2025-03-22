# Springbatch tự khởi động
Trong Spring Boot với Spring Batch:
- Nếu không có bất kỳ cấu hình nào để kiểm soát việc chạy job,
- Spring Boot sẽ tự động chạy tất cả các job được định nghĩa khi ứng dụng khởi động.
- Để tắt tính năng tự động này:
spring:
  batch:
   job:
    enabled: false

# Tự động tạo các table cần thiết cho việc quản lý springbatch DB
spring:
    batch:
        jdbc:
            initialize-schema: always

#
🏛 1. Kiến trúc lưu trữ Job và Step

Spring Batch sử dụng các bảng chính trong database để lưu thông tin thực thi:

📌 (1) Tầng chính
•	JobLauncher: Kích hoạt job với JobParameters.
•	JobRepository: Lưu trữ trạng thái thực thi Job & Step.
•	Job & Step Execution: Chứa thông tin quá trình chạy Job & Step.

📌 (2) Các bảng chính trong DB
BATCH_JOB_INSTANCE	Lưu thông tin job đã chạy (mỗi job chỉ chạy 1 lần với cùng JobParameters).
BATCH_JOB_EXECUTION	Ghi nhận từng lần chạy của job, trạng thái (STARTED, COMPLETED, FAILED).
BATCH_JOB_EXECUTION_PARAMS	Lưu các tham số JobParameters dùng để chạy job.
BATCH_STEP_EXECUTION	Ghi nhận quá trình thực thi từng step trong job.
BATCH_STEP_EXECUTION_CONTEXT	Lưu thông tin context của step trong quá trình chạy.

⚙ 2. Quy trình hoạt động

💡 Ví dụ: Khi bạn chạy job first_job, Spring Batch thực hiện như sau:

1️⃣ JobLauncher gọi Job
•	JobLauncher.run(firstJob(), jobParameters)
•	Tạo một JobInstance mới nếu chưa có với JobParameters này.

2️⃣ Spring Batch kiểm tra trạng thái job
•	Nếu job với JobParameters này đã COMPLETED, job sẽ không chạy lại.
•	Nếu job chưa hoàn thành, nó tạo JobExecution mới.

3️⃣ Spring Batch lưu thông tin vào database
•	Tạo bản ghi trong BATCH_JOB_EXECUTION (trạng thái STARTED).
•	Tạo BATCH_STEP_EXECUTION khi từng step bắt đầu chạy.
•	Khi step hoàn thành, cập nhật trạng thái (COMPLETED, FAILED…).

4️⃣ Khi job hoàn tất
•	Cập nhật trạng thái BATCH_JOB_EXECUTION.
•	Nếu job thất bại, Spring Batch có thể restart từ step bị lỗi.

🔎 Sự khác nhau giữa Job Instance và Job Execution trong Spring Batch
| Đặc điểm                 | Job Instance                                             | Job Execution                                   |
|--------------------------|------------------------------------------------------    |-------------------------------------------------|
| Định nghĩa               | Đại diện cho một job với một bộ JobParameters cụ thể     | Đại diện cho một lần chạy của Job Instance      |
| Khi nào tạo mới?         | Khi một job chạy với JobParameters chưa từng có trước đó | Mỗi lần job được chạy, dù có bị lỗi hay restart |
| Bảng trong DB            | BATCH_JOB_INSTANCE                                       | BATCH_JOB_EXECUTION                             |
| Trường khóa chính        | JOB_INSTANCE_ID                                          | JOB_EXECUTION_ID                                |
| Trạng thái               | Không có trạng thái (COMPLETED, FAILED...)               | Có trạng thái (STARTED, COMPLETED, FAILED...)   |
| Restart job có tạo mới?  | ❌ Không                                                 | ✅ Có                                           |


🔎 Sự khác nhau giữa Jbatch_status và exit_status trong Spring Batch
+------------------+--------------------------------------------+-----------------------------------------+
| Đặc điểm         | batch_status                               | exit_status                             |
+------------------+--------------------------------------------+-----------------------------------------+
| Định nghĩa      | Trạng thái tổng thể của Job/Step           | Trạng thái kết thúc chi tiết của Job/Step |
+------------------+--------------------------------------------+-----------------------------------------+
| Lưu trong DB    | Cột `BATCH_STATUS` trong bảng Batch DB     | Cột `EXIT_CODE` trong bảng Batch DB    |
+------------------+--------------------------------------------+-----------------------------------------+
| Giá trị mặc định| STARTED, COMPLETED, FAILED,...            | Mặc định giống batch_status, có thể tùy chỉnh |
+------------------+--------------------------------------------+-----------------------------------------+
| Có thể ghi đè?  | ❌ Không, Spring Batch tự động quản lý    | ✅ Có, có thể đặt trạng thái tùy chỉnh |
+------------------+--------------------------------------------+-----------------------------------------+
| Công dụng       | Quản lý trạng thái chung của Job/Step     | Cung cấp chi tiết về kết quả thực thi  |
+------------------+--------------------------------------------+-----------------------------------------+
| Ví dụ           | COMPLETED, FAILED                         | COMPLETED_WITH_WARNINGS, DATABASE_ERROR |
+------------------+--------------------------------------------+-----------------------------------------+
