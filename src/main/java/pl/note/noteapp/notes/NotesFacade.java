package pl.note.noteapp.notes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pl.note.noteapp.dtos.*;
import pl.note.noteapp.repository.NoteRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class NotesFacade {
    private final NoteRepository noteRepository;
    private final NewNoteValidator validator;
    private final DtoTransformer dtoTransformer;

    public NotesFacade(NoteRepository noteRepository, NewNoteValidator validator, DtoTransformer dtoTransformer) {
        this.noteRepository = noteRepository;
        this.validator = validator;
        this.dtoTransformer = dtoTransformer;
    }

    public NewNoteResponseDto saveNote(NewNoteDto newNoteDto) {
        ValidationResult validated = validator.validate(newNoteDto);
        return dtoTransformer.processSave(validated, newNoteDto);
    }

    public NewNoteResponseDto updateNote(UpdateNoteDto noteDTO) {
        ValidationResult validationResult = validator.validate(noteDTO);
        return dtoTransformer.processUpdate(validationResult, noteDTO);

    }

    public FindAllDto findAllNotes() {
        return new FindAllDto(noteRepository.findAll()
                .stream().map(NoteMapper::noteToNoteFindAllDto)
                .collect(Collectors.toList()));
    }

    public Optional<NoteDto> getNoteById(String id) {
        Optional<Note> note = noteRepository.findById(id);
        return note.map(NoteMapper::noteToDto);
    }

    public Page<NoteFindAllDto> findAllNotesPaged(PageRequest pageRequest) {
        Page<Note> notePage = noteRepository.findAll(pageRequest);
        return notePage.map(
                NoteMapper::noteToNoteFindAllDto
        );
    }

    public DeleteResponseDto delete(String noteId) {
        return dtoTransformer.delete(noteId);
    }
}
