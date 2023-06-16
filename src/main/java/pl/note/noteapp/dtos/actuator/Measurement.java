package pl.note.noteapp.dtos.actuator;

public class Measurement {
    private String statistic;
    private String value;

    public Measurement() {
    }

    public Measurement(String statistic, String value) {
        this.statistic = statistic;
        this.value = value;
    }

    public String getStatistic() {
        return statistic;
    }

    public String getValue() {
        return value;
    }
}
