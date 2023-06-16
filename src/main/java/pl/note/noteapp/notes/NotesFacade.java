package pl.note.noteapp.notes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pl.note.noteapp.dtos.*;
import pl.note.noteapp.repository.NoteRepository;

import java.util.Optional;
import java.util.stream.Collectors;

import static pl.note.noteapp.notes.NoteMapper.*;

@Component
public class NotesFacade {
    private final NoteRepository noteRepository;
    private final NewNoteValidator validator;

    public NotesFacade(NoteRepository noteRepository, NewNoteValidator validator) {
        this.noteRepository = noteRepository;
        this.validator = validator;
    }

    public NewNoteResponseDto saveNote(NewNoteDto newNoteDto) {
        ValidationResult validated = validator.validate(newNoteDto);

        if (validated.isValid()) {
            Note saved = noteRepository.save(dtoToNote(newNoteDto));
            NoteDto noteDto = noteToDto(saved);
            return new NewNoteResponseDto(noteDto, validated.isValid(), validated.getMessage());
        } else {
            return new NewNoteResponseDto(null, validated.isValid(), validated.getMessage());
        }
    }

    public FindAllDto findAllNotes() {
        return new FindAllDto(noteRepository.findAll()
                .stream().map(NoteMapper::noteToNoteFindAllDto)
                .collect(Collectors.toList()));
    }

    private Optional<Note> findById(String noteId) {
        return noteRepository.findById(noteId);
    }

    public Optional<NoteDto> updateNote(UpdateNoteDto noteDTO) {
        Optional<Note> note = findById(noteDTO.noteId());
        if (note.isEmpty()) {
            return Optional.empty();
        } else {
            Note saved = noteRepository.save(updateDtoToNote(noteDTO, note.get()));
            return Optional.of(noteToDto(saved));
        }
    }

    public Optional<NoteDto> getNoteById(String id) {
        Optional<Note> note = noteRepository.findById(id);
        return note.map(NoteMapper::noteToDto);
    }

    public Page<NoteDto> findAllNotesPaged(PageRequest pageRequest) {
        Page<Note> notePage = noteRepository.findAll(pageRequest);
        return notePage.map(
                NoteMapper::noteToDto
        );
    }
}
