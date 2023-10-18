package com.example._5rest.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
public class NoteResponseDto extends RepresentationModel<NoteResponseDto> {
    private String note;
}
