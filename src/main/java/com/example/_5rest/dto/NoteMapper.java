package com.example._5rest.dto;

import com.example._5rest.model.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {

    public Note toModel(NoteRequestDto dto) {
        Note note = new Note();
        note.setText(dto.getNotes());
        return note;
    }

    public NoteResponseDto dto(Note note) {
        NoteResponseDto dto = new NoteResponseDto();
        dto.setNote(note.getText());
        return dto;
    }
}
