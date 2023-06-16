package pl.note.noteapp.dtos.actuator;

import java.util.List;

public class Tag {
    private String tag;
    private List<String> values;

    public Tag() {
    }

    public Tag(String tag, List<String> values) {
        this.tag = tag;
        this.values = values;
    }

    public String getTag() {
        return tag;
    }

    public List<String> getValues() {
        return values;
    }
}
