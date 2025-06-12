package com.xq.testplan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "Response")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto {
    private String statusCode;
    private String statusMsg;
    private String uuid;
}
