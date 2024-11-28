package br.com.java.thread.async.helper.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ErrorPattern {

    private HttpStatus status;
    private String message;
    private Instant timestamp;
}
