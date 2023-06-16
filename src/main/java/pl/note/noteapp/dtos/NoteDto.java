package pl.note.noteapp.dtos;

import java.time.LocalDateTime;

public record NoteDto(
        String noteId,
        String title,
        String content,
        LocalDateTime lastEditDate
) {
}
