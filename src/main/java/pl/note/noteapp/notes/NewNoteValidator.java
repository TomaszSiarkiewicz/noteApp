package pl.note.noteapp.notes;

import org.springframework.stereotype.Component;
import pl.note.noteapp.dtos.NewNoteDto;
import pl.note.noteapp.dtos.UpdateNoteDto;

@Component
class NewNoteValidator {
    private final String BLANK_MESSAGE = "Title can not be blank!";
    private final String SUCCESS_MESSAGE = "Success!";

    public ValidationResult validate(NewNoteDto noteDto) {
        if (!noteDto.title().isBlank()) {
            return new ValidationResult(true, SUCCESS_MESSAGE);
        } else {
            return new ValidationResult(false, BLANK_MESSAGE);
        }
    }
    public ValidationResult validate(UpdateNoteDto noteDto){
        return validate(new NewNoteDto(noteDto.title(), noteDto.content()));
    }
}
