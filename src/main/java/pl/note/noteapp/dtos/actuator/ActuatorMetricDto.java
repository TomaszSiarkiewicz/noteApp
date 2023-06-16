package pl.note.noteapp.dtos.actuator;

import java.util.List;

public class ActuatorMetricDto {
    private String name;
    private String baseUnit;
    private List<Measurement> measurements;
    private List<Tag> availableTags;

    public ActuatorMetricDto() {
    }

    public ActuatorMetricDto(String name, String baseUnit, List<Measurement> measurements, List<Tag> availableTags) {
        this.name = name;
        this.baseUnit = baseUnit;
        this.measurements = measurements;
        this.availableTags = availableTags;
    }

    public String getName() {
        return name;
    }

    public String getBaseUnit() {
        return baseUnit;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public List<Tag> getAvailableTags() {
        return availableTags;
    }
}

