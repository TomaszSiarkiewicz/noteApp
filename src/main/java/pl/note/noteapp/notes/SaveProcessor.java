package pl.note.noteapp.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.note.noteapp.dtos.*;
import pl.note.noteapp.repository.NoteRepository;

import java.util.Optional;

import static pl.note.noteapp.notes.NoteMapper.*;
import static pl.note.noteapp.notes.NoteMapper.noteToDto;

@Component
class SaveProcessor {
    @Autowired
    private final NoteRepository noteRepository;

    public SaveProcessor(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }


    NewNoteResponseDto processSave(ValidationResult validated, NewNoteDto newNoteDto) {
        if (validated.isValid()) {
            Note saved = noteRepository.save(dtoToNote(newNoteDto));
            NoteDto noteDto = noteToDto(saved);
            return new NewNoteResponseDto(noteDto, true, validated.message());
        } else {
            return new NewNoteResponseDto(null, false, validated.message());
        }
    }

    NewNoteResponseDto processUpdate(ValidationResult validationResult, UpdateNoteDto noteDTO) {
        if (validationResult.isValid()) {
            Optional<Note> note = noteRepository.findById(noteDTO.noteId());
            if (note.isEmpty()) {
                return new NewNoteResponseDto(null, false, "Incorrect note ID");
            } else {
                Note saved = noteRepository.save(updateDtoToNote(noteDTO, note.get()));
                return new NewNoteResponseDto(noteToDto(saved), true, "Success!");
            }
        }
        return new NewNoteResponseDto(null, false, validationResult.message());
    }

    public DeleteResponseDto delete(String noteId) {
        Optional<Note> note = noteRepository.findById(noteId);

        if (note.isPresent()){
            noteRepository.deleteById(noteId);
            return new DeleteResponseDto(note.get().title(), true, "Success!");
        }
        return new DeleteResponseDto("", false, "Wrong Id");
    }
}
