package com.xq.testplan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "Requirement",
        description = "Requirement request payload object"
)
public class RequirementsDto {
    @Schema(description = "Requirement title", example = "Requirement Title")
    @NotEmpty(message = "Title can not be null or empty")
    @Size(max = 100, message = "Title length must be less than or equal to 100")
    private String title;

    @Schema(description = "Requirement description", example = "Requirement Description")
    @NotEmpty(message = "Description can not be null or empty")
    @Size(max = 500, message = "Description length must be less than or equal to 500")
    private String description;

    @Schema(description = "Requirement scopes", example = "Scope1, Scope2")
    @NotEmpty(message = "Scopes can not be null or empty")
    @Size(max = 500, message = "Scopes length must be less than or equal to 500")
    private String scopes;

    @Schema(description = "Requirement tags", example = "Tag1, Tag2")
    @NotEmpty(message = "Tags can not be null or empty")
    @Size(max = 200, message = "Tags length must be less than or equal to 200")
    private String tags;

    @Schema(description = "Requirement references", example = "Reference1, Reference2")
    @NotEmpty(message = "References can not be null or empty")
    @Size(max = 500, message = "References length must be less than or equal to 500")
    private String references;
}
