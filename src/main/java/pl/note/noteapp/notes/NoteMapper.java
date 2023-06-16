package pl.note.noteapp.notes;

import org.springframework.stereotype.Component;
import pl.note.noteapp.dtos.NewNoteDto;
import pl.note.noteapp.dtos.NoteDto;
import pl.note.noteapp.dtos.NoteFindAllDto;
import pl.note.noteapp.dtos.UpdateNoteDto;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
class NoteMapper {

    public static Note updateDtoToNote(UpdateNoteDto dto, Note note) {
        return new Note(
                dto.noteId(),
                note.creationDate(),
                LocalDateTime.now(),
                dto.title(),
                dto.content()
        );
    }

    public static Note dtoToNote(NewNoteDto dto) {
        return new Note(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                dto.title(),
                dto.content()
        );
    }

    public static NoteDto noteToDto(Note note) {
        return new NoteDto(
                note.noteId(),
                note.title(),
                note.content(),
                note.lastEditDate()
        );
    }
    public static NoteFindAllDto noteToNoteFindAllDto(Note note){
        return new NoteFindAllDto(
                note.noteId(),
                note.title()
        );
    }
}
