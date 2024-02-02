package com.zurnov.bitcoin.insights.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
public class ApiError {


    private String code;

    private HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "America/New_York")
    private Date timestamp;

    private String message;

    private String description;

    private String hpdc_request_id;

    public ApiError(String code, HttpStatus status, String message, String description, String requestId) {
        this(code, status, message);
        this.description = description;
        this.hpdc_request_id = requestId;
    }

    public ApiError(String code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
