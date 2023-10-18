package com.example._5rest.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserResponseDto extends RepresentationModel<UserResponseDto> {
    private UUID id;
    private String firstName;
    private String lastName;
    private List<NoteResponseDto> notes = new ArrayList<>();
}
