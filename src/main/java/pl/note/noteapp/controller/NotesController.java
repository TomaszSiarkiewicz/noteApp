package pl.note.noteapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.note.noteapp.dtos.FindAllDto;
import pl.note.noteapp.dtos.NewNoteDto;
import pl.note.noteapp.dtos.NoteDto;
import pl.note.noteapp.dtos.UpdateNoteDto;
import pl.note.noteapp.notes.NotesFacade;

import java.util.Optional;

@RestController
public class NotesController {
    private final NotesFacade notesFacade;

    public NotesController(NotesFacade notesFacade) {
        this.notesFacade = notesFacade;
    }

    @PostMapping("/note")
    ResponseEntity<NoteDto> result(@RequestBody NewNoteDto note) {
        NoteDto response = notesFacade.saveNote(note);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
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

    @GetMapping("/note/{noteId}")
    ResponseEntity<NoteDto> result(@PathVariable String noteId) {
        Optional<NoteDto> response = notesFacade.getNoteById(noteId);
        return response.map(value -> ResponseEntity.status(HttpStatus.OK)
                        .body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }
}
