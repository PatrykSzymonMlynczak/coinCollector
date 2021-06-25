package com.example.demo.exceptionAdvice;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ApiError {

    private LocalDateTime timestamp;
    private String debugMessage;
    private HttpStatus status;

    public ApiError(HttpStatus status, Throwable ex) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.debugMessage = ex.getLocalizedMessage();
    }

}
