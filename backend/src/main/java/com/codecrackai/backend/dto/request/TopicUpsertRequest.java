package com.codecrackai.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopicUpsertRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String slug;

    private String description;

    private int sortOrder;

    private Long parentId;
}
