package pl.note.noteapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.note.noteapp.dtos.CounterResponseDto;
import pl.note.noteapp.metrics.MetricsService;

@RestController
public class MetricsController {
    @Autowired
    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping("/counter")
    ResponseEntity<CounterResponseDto> counterDto(HttpServletRequest request) {

        CounterResponseDto response = metricsService.checkCounter(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
