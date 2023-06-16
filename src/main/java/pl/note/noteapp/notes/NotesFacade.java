package pl.note.noteapp.notes;

import org.springframework.stereotype.Component;
import pl.note.noteapp.dtos.FindAllDto;
import pl.note.noteapp.dtos.NewNoteDto;
import pl.note.noteapp.dtos.NoteDto;
import pl.note.noteapp.dtos.UpdateNoteDto;
import pl.note.noteapp.repository.NoteRepository;

import java.util.Optional;
import java.util.stream.Collectors;

import static pl.note.noteapp.notes.NoteMapper.*;

@Component
public class NotesFacade {
    private final NoteRepository noteRepository;

    public NotesFacade(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public NoteDto saveNote(NewNoteDto noteDto) {
        Note saved = noteRepository.save(dtoToNote(noteDto));
        return noteToDto(saved);
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
}