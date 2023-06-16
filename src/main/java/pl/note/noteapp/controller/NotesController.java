package pl.note.noteapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.note.noteapp.dtos.*;
import pl.note.noteapp.notes.NotesFacade;

import java.util.Optional;

@RestController
public class NotesController {
    private final NotesFacade notesFacade;

    public NotesController(NotesFacade notesFacade) {
        this.notesFacade = notesFacade;
    }

    @PostMapping("/note")
    ResponseEntity<NewNoteResponseDto> result(@RequestBody NewNoteDto note) {
        NewNoteResponseDto response = notesFacade.saveNote(note);
        if (response.isValid()) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(response);
        }
    }

    @PutMapping("/note")
    ResponseEntity<NoteDto> updateResult(@RequestBody UpdateNoteDto noteDTO) {
        Optional<NoteDto> result = notesFacade.updateNote(noteDTO);
        return result.map(value -> ResponseEntity.status(HttpStatus.OK)
                        .body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    @GetMapping("/notes")
    ResponseEntity<FindAllDto> result() {
        FindAllDto response = notesFacade.findAllNotes();
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/notes/paged")
    Page<NoteDto> findAll(@RequestParam int page){
        PageRequest pageRequest = PageRequest.of(page, 4, Sort.by("lastEditDate"));
        return notesFacade.findAllNotesPaged(pageRequest);
    }

    @GetMapping("/note/{noteId}")
    ResponseEntity<NoteDto> result(@PathVariable String noteId) {
        Optional<NoteDto> response = notesFacade.getNoteById(noteId);
        return response.map(value -> ResponseEntity.status(HttpStatus.OK)
                        .body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }
}
