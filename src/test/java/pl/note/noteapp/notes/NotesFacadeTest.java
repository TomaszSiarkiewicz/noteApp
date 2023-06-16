package pl.note.noteapp.notes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import pl.note.noteapp.dtos.FindAllDto;
import pl.note.noteapp.dtos.NewNoteDto;
import pl.note.noteapp.dtos.NoteDto;
import pl.note.noteapp.dtos.UpdateNoteDto;
import pl.note.noteapp.repository.NoteRepository;

import static org.assertj.core.api.Assertions.assertThat;

public class NotesFacadeTest {
    NoteRepository noteRepository = new InMemoryNotesDatabaseImplementation();

    NotesFacade notesFacade = new NotesFacade(noteRepository);

    @AfterEach
    public void cleanUp(){
        noteRepository.deleteAll();
    }
    @Test
    public void should_save_new_note(){
        //given
        NewNoteDto newNote = new NewNoteDto("title", "content");
        //when
        NoteDto noteDTO = notesFacade.saveNote(newNote);
        //then
        assertThat(noteDTO).isNotNull();
        assertThat(noteDTO.title()).isEqualTo(newNote.title());
    }
    @Test
    public void should_update_note(){
        //given
        NewNoteDto newNote = new NewNoteDto("title", "content");
        NoteDto noteDtoToBeUpdated = notesFacade.saveNote(newNote);
        UpdateNoteDto updateDto = new UpdateNoteDto(noteDtoToBeUpdated.noteId(),"new title", "new content");
        //when
        NoteDto result = notesFacade.updateNote(updateDto).get();
        //then
        assertThat(result.noteId()).isEqualTo(noteDtoToBeUpdated.noteId());
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
}
