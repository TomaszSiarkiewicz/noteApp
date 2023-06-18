package pl.note.noteapp.notes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import pl.note.noteapp.dtos.*;
import pl.note.noteapp.repository.NoteRepository;

import static org.assertj.core.api.Assertions.assertThat;

public class NotesFacadeTest {
    NoteRepository noteRepository = new InMemoryNotesDatabaseImplementation();
    NewNoteValidator validator = new NewNoteValidator();
    DtoTransformer dtoTransformer = new DtoTransformer(noteRepository);

    NotesFacade notesFacade = new NotesFacade(noteRepository, validator, dtoTransformer);

    @AfterEach
    public void cleanUp(){
        noteRepository.deleteAll();
    }
    @Test
    public void should_save_new_note(){
        //given
        NewNoteDto newNote = new NewNoteDto("title", "content");
        //when
        NewNoteResponseDto newNoteResponseDto = notesFacade.saveNote(newNote);
        //then
        assertThat(newNoteResponseDto).isNotNull();
        assertThat(newNoteResponseDto.noteDto().title()).isEqualTo(newNote.title());
    }
    @Test
    public void should_update_note(){
        //given
        NewNoteDto newNote = new NewNoteDto("title", "content");
        NewNoteResponseDto noteDtoToBeUpdated = notesFacade.saveNote(newNote);
        UpdateNoteDto updateDto = new UpdateNoteDto(noteDtoToBeUpdated.noteDto().noteId(),"new title", "new content");
        //when
        NoteDto result = notesFacade.updateNote(updateDto).noteDto();
        //then
        assertThat(result.noteId()).isEqualTo(noteDtoToBeUpdated.noteDto().noteId());
    }
    @Test
    public void should_find_all_saved_notes(){
        //given
        NewNoteDto newNote = new NewNoteDto("title", "content");
        for (int i = 0; i < 5; i++) {
            notesFacade.saveNote(newNote);
        }
        //when
        FindAllDto allNotes = notesFacade.findAllNotes();
        //then
        assertThat(allNotes.notes().size()).isEqualTo(5);
    }
    @Test
    public void should_delete_note(){
        //given
        NewNoteDto newNote = new NewNoteDto("title", "content");
        //when
        NewNoteResponseDto newNoteResponseDto = notesFacade.saveNote(newNote);
        String noteId = newNoteResponseDto.noteDto().noteId();
        notesFacade.delete(noteId);
        //then
        assertThat(notesFacade.getNoteById(noteId)).isEmpty();
    }
}
