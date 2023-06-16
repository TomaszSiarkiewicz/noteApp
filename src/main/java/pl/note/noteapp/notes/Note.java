package pl.note.noteapp.notes;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public record Note(
        @Id
        String noteId,
        LocalDateTime creationDate,
        LocalDateTime lastEditDate,
        String title,
        String content
) {
}
