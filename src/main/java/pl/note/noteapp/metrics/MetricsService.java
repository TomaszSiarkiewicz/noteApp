package pl.note.noteapp.metrics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.note.noteapp.dtos.CounterResponseDto;
import pl.note.noteapp.dtos.actuator.ActuatorMetricDto;

@Service
public class MetricsService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public MetricsService() {
        restTemplate = new RestTemplate();
        objectMapper = new ObjectMapper();
    }

    public CounterResponseDto checkCounter(HttpServletRequest request) {
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .path("actuator/metrics/http.server.requests")
                .build()
                .toUriString();

        try {
            String result = restTemplate.getForObject(baseUrl, String.class);
            ActuatorMetricDto actuatorMetricDto = objectMapper.readValue(result, ActuatorMetricDto.class);
            return new CounterResponseDto(actuatorMetricDto.getMeasurements().get(0).getValue(), "Number of requests");
        } catch (JsonProcessingException | HttpClientErrorException e) {
            return new CounterResponseDto("", "Statistics unavaileable");
        }

    }
}
