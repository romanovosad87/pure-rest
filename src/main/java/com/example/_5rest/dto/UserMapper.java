package com.example._5rest.dto;

import com.example._5rest.model.Note;
import com.example._5rest.model.User;
import com.example._5rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final UserRepository repository;
    private final NoteMapper noteMapper;

    public User toModel(UserRequestDto dto) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());

        return user;
    }

    public User toModel(NoteRequestDto dto, String id) {
        User user = repository.get(UUID.fromString(id));
        Note note = noteMapper.toModel(dto);
        user.addNote(note);
        return user;
    }

    public UserResponseDto toDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        List<NoteResponseDto> noteResponseDtoList = user.getNotes().stream()
                .map(noteMapper::dto)
                .toList();
        dto.setNotes(noteResponseDtoList);
        return dto;
    }
}
