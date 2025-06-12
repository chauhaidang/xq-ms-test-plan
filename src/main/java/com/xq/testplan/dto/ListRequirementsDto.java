package com.xq.testplan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Schema(
        name = "ListRequirementsDto",
        description = "Requirements response payload object"
)
@Builder
public class ListRequirementsDto {
    @Schema(description = "Total number of requirements", example = "5")
    int total;

    @Schema(description = "List of requirement objects")
    List<RequirementsDto> requirements;
}
