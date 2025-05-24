package pets.adoption.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pets.adoption.dto.StatsDTO;
import pets.adoption.services.StatsService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @GetMapping
    public ResponseEntity<StatsDTO> getSystemStats() {
        return ResponseEntity.ok(statsService.getSystemStats());
    }
} 