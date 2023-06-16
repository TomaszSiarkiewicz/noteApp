package pl.note.noteapp.dtos;

public record UpdateNoteDto(
        String noteId,
        String title,
        String content
        ) {
}
