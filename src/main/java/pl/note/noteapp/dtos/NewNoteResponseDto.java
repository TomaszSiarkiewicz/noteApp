package pl.note.noteapp.dtos;

public record NewNoteResponseDto(
        NoteDto noteDto,
        boolean isValid,
        String message
) {
}
