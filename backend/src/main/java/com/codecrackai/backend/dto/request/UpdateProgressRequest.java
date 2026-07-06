package com.codecrackai.backend.dto.request;

import com.codecrackai.backend.entity.enums.ProgressStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProgressRequest {

    private ProgressStatus status;

    @Min(1)
    @Max(5)
    private Integer confidenceRating;

    private Boolean bookmarked;

    private String personalNotes;
}
