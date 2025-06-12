package com.xq.testplan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(name = "LinkedRequirements")
public class LinkedRequirementsDto {
    @Schema(description = "Requirement A ID", example = "1001")
    @NotEmpty(message = "Requirement A ID can not be null or empty")
    private Long reqAId;

    @Schema(description = "Requirement B ID", example = "1002")
    @NotEmpty(message = "Requirement B ID can not be null or empty")
    private Long reqBId;
}
