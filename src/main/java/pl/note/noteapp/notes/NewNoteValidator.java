package pl.note.noteapp.notes;

import org.springframework.stereotype.Component;
import pl.note.noteapp.dtos.NewNoteDto;

@Component
public class NewNoteValidator {
    private final String BLANK_MESSAGE = "Title can not be blank!";
    private final String SUCCESS_MESSAGE = "Success!";

    public ValidationResult validate(NewNoteDto noteDto) {
        if (!noteDto.title().isBlank()) {
            return new ValidationResult(true, SUCCESS_MESSAGE);
        } else {
            return new ValidationResult(false, BLANK_MESSAGE);
        }
    }
}
