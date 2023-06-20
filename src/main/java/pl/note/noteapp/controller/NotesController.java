package pl.note.noteapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.note.noteapp.dtos.*;
import pl.note.noteapp.notes.NotesFacade;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class NotesController {
    private final NotesFacade notesFacade;

    public NotesController(NotesFacade notesFacade) {
        this.notesFacade = notesFacade;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/note")
    ResponseEntity<NewNoteResponseDto> result(@RequestBody NewNoteDto note) {
        NewNoteResponseDto response = notesFacade.saveNote(note);
        if (response.isValid()) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(response);
        }
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/note")
    ResponseEntity<NewNoteResponseDto> updateResult(@RequestBody UpdateNoteDto noteDTO) {
        NewNoteResponseDto result = notesFacade.updateNote(noteDTO);
        if (result.isValid()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/notes")
    ResponseEntity<FindAllDto> result() {
        FindAllDto response = notesFacade.findAllNotes();
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/notes/paged")
    Page<NoteFindAllDto> findAll(@RequestParam int page) {
        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by("lastEditDate").descending());
        return notesFacade.findAllNotesPaged(pageRequest);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/note/{noteId}")
    ResponseEntity<NoteDto> result(@PathVariable String noteId) {
        Optional<NoteDto> response = notesFacade.getNoteById(noteId);
        return response.map(value -> ResponseEntity.status(HttpStatus.OK)
                        .body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/note/{noteId}")
    ResponseEntity delete(@PathVariable String noteId) {
        DeleteResponseDto response = notesFacade.delete(noteId);
        if (response.isDeleted()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(response);
        }
    }
}
