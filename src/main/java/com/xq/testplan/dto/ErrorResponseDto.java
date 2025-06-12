package com.xq.testplan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Schema(name = "Error response")
@Data
@AllArgsConstructor
public class ErrorResponseDto {
    @Schema(example = "uri=/api/requirements/update")
    private String apiPath;

    @Schema(example = "NOT_FOUND")
    private HttpStatus errorCode;

    @Schema(description = "Error message returned from implementation logic", example = "Requirement not found with the given input data requirement uuid:'1234-5661-1235-1232-5112'")
    private String errorMessage;

    @Schema(description = "Date time when error occur", example = "2024-08-25T22:30:44.173049")
    private LocalDateTime errorTime;
}
