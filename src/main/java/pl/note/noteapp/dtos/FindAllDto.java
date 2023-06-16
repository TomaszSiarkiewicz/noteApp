package pl.note.noteapp.dtos;

import java.util.List;

public record FindAllDto(
        List<NoteFindAllDto> notes
) {
}
