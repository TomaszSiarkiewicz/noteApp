package pl.note.noteapp.dtos;

public record DeleteResponseDto(
        String noteTitle,
        boolean isDeleted,
        String message) {
}
