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
        return processSave(validated, newNoteDto);
    }

    public NewNoteResponseDto updateNote(UpdateNoteDto noteDTO) {
        ValidationResult validationResult = validator.validate(noteDTO);
        return processUpdate(validationResult, noteDTO);

    }

    private NewNoteResponseDto processSave(ValidationResult validated, NewNoteDto newNoteDto) {
        if (validated.isValid()) {
            Note saved = noteRepository.save(dtoToNote(newNoteDto));
            NoteDto noteDto = noteToDto(saved);
            return new NewNoteResponseDto(noteDto, true, validated.message());
        } else {
            return new NewNoteResponseDto(null, false, validated.message());
        }
    }

    private NewNoteResponseDto processUpdate(ValidationResult validationResult, UpdateNoteDto noteDTO) {
        if (validationResult.isValid()){
            Optional<Note> note = findById(noteDTO.noteId());
            if (note.isEmpty()) {
                return new NewNoteResponseDto(null, false, "Incorrect note ID");
            } else {
                Note saved = noteRepository.save(updateDtoToNote(noteDTO, note.get()));
                return new NewNoteResponseDto(noteToDto(saved), true, "Success!");
            }
        }
        return new NewNoteResponseDto(null, false, validationResult.message());
    }

    public FindAllDto findAllNotes() {
        return new FindAllDto(noteRepository.findAll()
                .stream().map(NoteMapper::noteToNoteFindAllDto)
                .collect(Collectors.toList()));
    }

    private Optional<Note> findById(String noteId) {
        return noteRepository.findById(noteId);
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
