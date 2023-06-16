package pl.note.noteapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.note.noteapp.notes.Note;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {
}
